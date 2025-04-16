package com.web.dopamine.service;

import com.web.dopamine.dto.CityDto;
import com.web.dopamine.dto.CourseDto;
import com.web.dopamine.dto.ProvinceDto;
import com.web.dopamine.entity.City;
import com.web.dopamine.entity.Province;
import com.web.dopamine.entity.Result;
import com.web.dopamine.repository.CityRepository;
import com.web.dopamine.repository.ProvinceRepository;
import com.web.dopamine.repository.ResultRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RandomService {

    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final ResultRepository resultRepository;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    // 랜덤 도 추천
    public ProvinceDto getRandomProvince() {
        List<Province> provinces = provinceRepository.findAll();
        if (provinces.isEmpty()) {
            throw new IllegalStateException("등록된 도/시가 없습니다.");
        }
        
        Province randomProvince = provinces.get(random.nextInt(provinces.size()));
        return ProvinceDto.fromEntity(randomProvince);
    }

    // 특정 도에 속한 랜덤 시/군 추천
    public CityDto getRandomCityByProvince(Integer provinceNo) {
        List<City> cities = cityRepository.findByProvinceNo(provinceNo);
        if (cities.isEmpty()) {
            throw new IllegalStateException("해당 도/시에 등록된 시/군/구가 없습니다.");
        }
        
        City randomCity = cities.get(random.nextInt(cities.size()));
        return CityDto.fromEntity(randomCity);
    }

    // 특정 도와 시에 속한 랜덤 여행 코스 추천
    public CourseDto getRandomCourse(Integer provinceNo, Integer cityNo) {
        List<Result> courseResults = resultRepository.findByProvinceNoAndCityNo(provinceNo, cityNo);
        if (courseResults.isEmpty()) {
            throw new IllegalStateException("해당 지역에 등록된 여행 코스가 없습니다.");
        }
        
        Result randomResult = courseResults.get(random.nextInt(courseResults.size()));
        
        try {
            // JSON 문자열을 객체로 변환
            CourseDto.PlaceDto course1 = objectMapper.readValue(randomResult.getCourse1(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course2 = objectMapper.readValue(randomResult.getCourse2(), CourseDto.PlaceDto.class);
            CourseDto.PlaceDto course3 = objectMapper.readValue(randomResult.getCourse3(), CourseDto.PlaceDto.class);
            
            return CourseDto.builder()
                    .no(randomResult.getNo())
                    .cityNo(randomResult.getCityNo())
                    .provinceNo(randomResult.getProvinceNo())
                    .location(randomResult.getLocation())
                    .course1(course1)
                    .course2(course2)
                    .course3(course3)
                    .createdAt(randomResult.getCreatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("여행 코스 정보 변환 중 오류가 발생했습니다.", e);
        }
    }
} 