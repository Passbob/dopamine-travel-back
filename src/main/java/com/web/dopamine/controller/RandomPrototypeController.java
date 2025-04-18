package com.web.dopamine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.common.Constants;
import com.web.dopamine.dto.CityDto;
import com.web.dopamine.dto.ConstraintDto;
import com.web.dopamine.dto.CourseDto;
import com.web.dopamine.dto.ProvinceDto;
import com.web.dopamine.dto.ThemeDto;
import com.web.dopamine.entity.City;
import com.web.dopamine.entity.Province;
import com.web.dopamine.entity.Result;
import com.web.dopamine.entity.Visit;
import com.web.dopamine.repository.CityRepository;
import com.web.dopamine.repository.ProvinceRepository;
import com.web.dopamine.repository.ResultRepository;
import com.web.dopamine.repository.VisitRepository;
import com.web.dopamine.service.AITravelService;
import com.web.dopamine.service.ConstraintService;
import com.web.dopamine.service.MockTravelService;
import com.web.dopamine.service.ThemeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Constants.Api.BASE_PATH + "/prototype")
@RequiredArgsConstructor
public class RandomPrototypeController {

    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final ResultRepository resultRepository;
    private final VisitRepository visitRepository;
    private final ThemeService themeService;
    private final ConstraintService constraintService;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();
    
    // 환경에 따라 다른 서비스 주입
    @Autowired(required = false)
    private AITravelService aiTravelService;
    
    @Autowired(required = false)
    private MockTravelService mockTravelService;

    /**
     * 모든 도/시 목록 조회 API
     * 프론트엔드에서 랜덤 선택을 위한 데이터로 사용
     */
    @GetMapping("/provinces")
    public ApiResponse<List<ProvinceDto>> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        List<ProvinceDto> provinceDtos = provinces.stream()
                .map(ProvinceDto::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(provinceDtos);
    }

    /**
     * 모든 테마 목록 조회 API
     */
    @GetMapping("/themes")
    public ApiResponse<List<ThemeDto>> getAllThemes() {
        log.info("모든 테마 목록 조회");
        List<ThemeDto> themes = themeService.getAllThemes();
        return ApiResponse.success(themes);
    }

    /**
     * 모든 제약조건 목록 조회 API
     */
    @GetMapping("/constraints")
    public ApiResponse<List<ConstraintDto>> getAllConstraints() {
        log.info("모든 제약조건 목록 조회");
        List<ConstraintDto> constraints = constraintService.getAllConstraints();
        return ApiResponse.success(constraints);
    }

    /**
     * 특정 도/시에 속한 모든 시/군 목록 조회 API
     * 프론트엔드에서 랜덤 선택을 위한 데이터로 사용
     */
    @GetMapping("/cities")
    public ApiResponse<List<CityDto>> getCitiesByProvince(@RequestParam Integer provinceNo) {
        List<City> cities = cityRepository.findByProvinceNo(provinceNo);
        List<CityDto> cityDtos = cities.stream()
                .map(CityDto::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success(cityDtos);
    }

    /**
     * 여행 코스 추천 API (포스트 방식)
     * 프론트엔드에서 카드 뒤집기 애니메이션 후 비동기로 결과 요청
     * 로그도 함께 기록
     */
    @PostMapping("/travel-course")
    @Transactional
    public ApiResponse<CourseDto> getTravelCourse(
            @RequestParam Integer provinceNo, 
            @RequestParam Integer cityNo,
            @RequestParam(required = false) Integer themeNo,
            @RequestParam(required = false) Integer constraintNo,
            HttpServletRequest request) {
        
        log.info("여행 코스 추천 요청 - provinceNo: {}, cityNo: {}, themeNo: {}, constraintNo: {}", 
                provinceNo, cityNo, themeNo, constraintNo);
        
        // 기본값 설정
        if (themeNo == null) {
            themeNo = 1; // 기본 테마 (DB에 존재하는 ID 사용)
        }
        if (constraintNo == null) {
            constraintNo = 1; // 기본 제약조건 (DB에 존재하는 ID 사용)
        }
        
        // 기존 데이터가 있는지 확인
        List<Result> existingResults = resultRepository.findByProvinceNoAndCityNoAndThemeNoAndConstraintNo(
                provinceNo, cityNo, themeNo, constraintNo);
        
        Result result;
        // 50% 확률로 기존 데이터 사용 또는 새로 생성
        if (!existingResults.isEmpty() && random.nextBoolean()) {
            // 기존 데이터에서 랜덤 선택
            result = existingResults.get(random.nextInt(existingResults.size()));
            log.info("기존 데이터 사용 - resultNo: {}", result.getNo());
        } else {
            // 새로운 코스 생성 (테스트 프로파일 여부에 따라 다른 서비스 호출)
            if (mockTravelService != null) {
                result = mockTravelService.generateTravelCourse(provinceNo, cityNo, themeNo, constraintNo);
                log.info("MockTravelService로 코스 생성 - resultNo: {}", result.getNo());
            } else if (aiTravelService != null) {
                result = aiTravelService.generateTravelCourse(provinceNo, cityNo, themeNo, constraintNo);
                log.info("AITravelService로 코스 생성 - resultNo: {}", result.getNo());
            } else {
                log.error("여행 코스 생성 서비스가 없습니다.");
                return ApiResponse.error("SERVICE_UNAVAILABLE", "여행 코스 생성 서비스를 사용할 수 없습니다.");
            }
        }
        
        // 방문 로그 처리
        String clientIp = getClientIp(request);
        LocalDate today = LocalDate.now();
        
        // 오늘 해당 IP의 방문 기록 확인
        Optional<Visit> existingVisit = visitRepository.findByVisitDateAndIp(today, clientIp);
        
        if (existingVisit.isEmpty()) {
            // 1. 오늘 첫 방문인 경우
            Visit visit = Visit.builder()
                    .ip(clientIp)
                    .visitDate(today)
                    .result1No(result.getNo())
                    .build();
            visitRepository.save(visit);
            log.info("새로운 방문 기록 저장 - IP: {}, 결과1: {}", clientIp, result.getNo());
        } else {
            Visit visit = existingVisit.get();
            if (visit.getResult1No() != null && visit.getResult2No() == null) {
                // 2. 오늘 두 번째 방문인 경우
                visit.setResult2No(result.getNo());
                visitRepository.save(visit);
                log.info("두 번째 방문 결과 저장 - IP: {}, 결과2: {}", clientIp, result.getNo());
            } else if (visit.getResult1No() != null && visit.getResult2No() != null) {
                // 3. 오늘 세 번째 이상 방문인 경우 (일일 제한 초과)
                log.warn("일일 방문 횟수 초과 - IP: {}", clientIp);
                return ApiResponse.error("DAILY_LIMIT_EXCEEDED", "일일 조회 횟수를 초과했습니다. 내일 다시 시도해주세요.");
            }
        }
        
        // Result 엔티티를 CourseDto로 변환
        CourseDto courseDto = convertResultToCourseDto(result);
        
        return ApiResponse.success(courseDto);
    }
    
    /**
     * Result 엔티티를 CourseDto로 변환
     */
    private CourseDto convertResultToCourseDto(Result result) {
        try {
            CourseDto.PlaceDto course1 = objectMapper.readValue(result.getCourse1(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course2 = objectMapper.readValue(result.getCourse2(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course3 = objectMapper.readValue(result.getCourse3(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course4 = objectMapper.readValue(result.getCourse4(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course5 = objectMapper.readValue(result.getCourse5(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course6 = objectMapper.readValue(result.getCourse6(), CourseDto.PlaceDto.class);
            
            return CourseDto.builder()
                    .no(result.getNo())
                    .cityNo(result.getCityNo())
                    .provinceNo(result.getProvinceNo())
                    .themeNo(result.getThemeNo())
                    .constraintNo(result.getConstraintNo())
                    .location(result.getLocation())
                    .course1(course1)
                    .course2(course2)
                    .course3(course3)
                    .course4(course4)
                    .course5(course5)
                    .course6(course6)
                    .createdAt(result.getCreatedAt())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("코스 정보 변환 중 오류 발생", e);
            throw new RuntimeException("여행 코스 정보 변환에 실패했습니다.", e);
        }
    }
    
    /**
     * 클라이언트 IP 주소 가져오기
     */
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
} 