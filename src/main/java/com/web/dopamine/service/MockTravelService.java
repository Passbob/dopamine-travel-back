package com.web.dopamine.service;

import com.web.dopamine.entity.City;
import com.web.dopamine.entity.Province;
import com.web.dopamine.entity.Result;
import com.web.dopamine.repository.CityRepository;
import com.web.dopamine.repository.ProvinceRepository;
import com.web.dopamine.repository.ResultRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * OpenAI API를 사용하지 않고 테스트용으로 사용할 수 있는 Mock 서비스를 구현합니다.
 * 'test' 프로파일에서만 활성화됩니다.
 */
@Slf4j
@Service
@Profile("test")
@RequiredArgsConstructor
public class MockTravelService {

    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final ResultRepository resultRepository;
    private final Random random = new Random();

    /**
     * 테스트용 여행 코스를 생성합니다.
     */
    @Transactional
    public Result generateTravelCourse(Integer provinceNo, Integer cityNo) {
        // 도/시 정보 조회
        Province province = provinceRepository.findById(provinceNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 provinceNo입니다: " + provinceNo));
        
        City city = cityRepository.findById(cityNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 cityNo입니다: " + cityNo));

        // 이미 해당 지역의 결과가 있는지 확인
        List<Result> existingResults = resultRepository.findByProvinceNoAndCityNo(provinceNo, cityNo);
        if (!existingResults.isEmpty()) {
            log.info("기존 여행 코스 결과 반환 - provinceNo: {}, cityNo: {}", provinceNo, cityNo);
            return existingResults.get(0);
        }

        // 지역명
        String location = province.getName() + " " + city.getName();
        
        // 테스트용 코스 생성
        String[] courses = generateMockCourses(location);

        // 결과 저장
        Result result = Result.builder()
                .provinceNo(provinceNo)
                .cityNo(cityNo)
                .location(location)
                .course1(courses[0])
                .course2(courses[1])
                .course3(courses[2])
                .createdAt(LocalDateTime.now())
                .build();
        
        return resultRepository.save(result);
    }

    /**
     * 테스트용 여행 코스 데이터를 생성합니다.
     */
    private String[] generateMockCourses(String location) {
        String[] courses = new String[3];
        
        // 1번 코스 - 유명 관광지
        courses[0] = String.format(
            "{\"name\":\"%s 대표 관광지\",\"description\":\"이 지역의 유명한 관광 명소입니다.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":1,\"order\":1}",
            location, random.nextInt(100), random.nextInt(100)
        );
        
        // 2번 코스 - 식당
        courses[1] = String.format(
            "{\"name\":\"%s 맛집\",\"description\":\"현지 음식을 맛볼 수 있는 유명 맛집입니다.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":1,\"order\":2}",
            location, random.nextInt(100), random.nextInt(100)
        );
        
        // 3번 코스 - 공원/휴식
        courses[2] = String.format(
            "{\"name\":\"%s 공원\",\"description\":\"휴식을 취할 수 있는 아름다운 공원입니다.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":1,\"order\":3}",
            location, random.nextInt(100), random.nextInt(100)
        );
        
        return courses;
    }
} 