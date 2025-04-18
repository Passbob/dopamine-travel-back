package com.web.dopamine.service;

import com.web.dopamine.entity.City;
import com.web.dopamine.entity.Province;
import com.web.dopamine.entity.Result;
import com.web.dopamine.repository.CityRepository;
import com.web.dopamine.repository.ProvinceRepository;
import com.web.dopamine.repository.ResultRepository;
import com.web.dopamine.repository.ThemeRepository;
import com.web.dopamine.repository.ConstraintRepository;

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
    private final ThemeRepository themeRepository;
    private final ConstraintRepository constraintRepository;
    private final Random random = new Random();

    /**
     * 테스트용 여행 코스를 생성합니다.
     */
    @Transactional
    public Result generateTravelCourse(Integer provinceNo, Integer cityNo, Integer themeNo, Integer constraintNo) {
        // 도/시 정보 조회
        Province province = provinceRepository.findById(provinceNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 provinceNo입니다: " + provinceNo));
        
        City city = cityRepository.findById(cityNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 cityNo입니다: " + cityNo));

        // 이미 해당 지역의 결과가 있는지 확인
        List<Result> existingResults = resultRepository.findByProvinceNoAndCityNoAndThemeNoAndConstraintNo(
                provinceNo, cityNo, themeNo, constraintNo);
        if (!existingResults.isEmpty()) {
            log.info("기존 여행 코스 결과 반환 - provinceNo: {}, cityNo: {}, themeNo: {}, constraintNo: {}", 
                    provinceNo, cityNo, themeNo, constraintNo);
            return existingResults.get(0);
        }

        // 지역명
        String location = province.getName() + " " + city.getName();
        
        // 테스트용 코스 생성
        String[] courses = generateMockCourses(location, themeNo, constraintNo);

        // 결과 저장
        Result result = Result.builder()
                .provinceNo(provinceNo)
                .cityNo(cityNo)
                .location(location)
                .themeNo(themeNo)
                .constraintNo(constraintNo)
                .course1(courses[0])
                .course2(courses[1])
                .course3(courses[2])
                .course4(courses[3])
                .course5(courses[4])
                .course6(courses[5])
                .createdAt(LocalDateTime.now())
                .build();
        
        return resultRepository.save(result);
    }

    /**
     * 테스트용 여행 코스 데이터를 생성합니다.
     */
    private String[] generateMockCourses(String location, Integer themeNo, Integer constraintNo) {
        String[] courses = new String[6];
        
        // 테마와 제약조건 이름 가져오기
        String themeName = themeRepository.findById(themeNo)
                .map(theme -> theme.getName())
                .orElse("일반 여행");
                
        String constraintName = constraintRepository.findById(constraintNo)
                .map(constraint -> constraint.getName())
                .orElse("제약 없음");
                
        log.info("테스트 여행 코스 생성 - 위치: {}, 테마: {}, 제약조건: {}", location, themeName, constraintName);
        
        // Day 1 코스
        // 1번 코스 - 유명 관광지
        courses[0] = String.format(
            "{\"name\":\"%s %s 명소 1\",\"description\":\"%s에서 가장 유명한 %s 명소입니다. %s 동반 가능.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":1,\"order\":1}",
            location, themeName, location, themeName, constraintName, random.nextInt(100), random.nextInt(100)
        );
        
        // 2번 코스 - 식당
        courses[1] = String.format(
            "{\"name\":\"%s %s 명소 2\",\"description\":\"%s의 두 번째 %s 명소입니다. %s 동반 가능.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":1,\"order\":2}",
            location, themeName, location, themeName, constraintName, random.nextInt(100), random.nextInt(100)
        );
        
        // 3번 코스 - 공원/휴식
        courses[2] = String.format(
            "{\"name\":\"%s %s 명소 3\",\"description\":\"%s의 세 번째 %s 명소입니다. %s 동반 가능.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":1,\"order\":3}",
            location, themeName, location, themeName, constraintName, random.nextInt(100), random.nextInt(100)
        );
        
        // Day 2 코스
        // 4번 코스 - 문화시설
        courses[3] = String.format(
            "{\"name\":\"%s %s 명소 4\",\"description\":\"%s의 네 번째 %s 명소입니다. %s 동반 가능.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":2,\"order\":1}",
            location, themeName, location, themeName, constraintName, random.nextInt(100), random.nextInt(100)
        );
        
        // 5번 코스 - 쇼핑
        courses[4] = String.format(
            "{\"name\":\"%s %s 명소 5\",\"description\":\"%s의 다섯 번째 %s 명소입니다. %s 동반 가능.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":2,\"order\":2}",
            location, themeName, location, themeName, constraintName, random.nextInt(100), random.nextInt(100)
        );
        
        // 6번 코스 - 액티비티
        courses[5] = String.format(
            "{\"name\":\"%s %s 명소 6\",\"description\":\"%s의 여섯 번째 %s 명소입니다. %s 동반 가능.\",\"lat\":37.5%d,\"lng\":127.0%d,\"day\":2,\"order\":3}",
            location, themeName, location, themeName, constraintName, random.nextInt(100), random.nextInt(100)
        );
        
        return courses;
    }
} 