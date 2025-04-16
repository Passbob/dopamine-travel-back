package com.web.dopamine.controller;

import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.common.Constants;
import com.web.dopamine.dto.CourseDto;
import com.web.dopamine.dto.ProvinceDto;
import com.web.dopamine.dto.CityDto;
import com.web.dopamine.service.RandomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.Api.BASE_PATH + Constants.Api.RANDOM)
@RequiredArgsConstructor
public class RandomController {

    private final RandomService randomService;

    // 랜덤 도 추천
    @GetMapping(Constants.Api.PROVINCE)
    public ApiResponse<ProvinceDto> getRandomProvince() {
        return ApiResponse.success(randomService.getRandomProvince());
    }

    // 랜덤 시/군 추천 (특정 도에 속한)
    @GetMapping(Constants.Api.CITY)
    public ApiResponse<CityDto> getRandomCity(@RequestParam Integer provinceNo) {
        return ApiResponse.success(randomService.getRandomCityByProvince(provinceNo));
    }

    // 랜덤 여행 코스 추천 (특정 도와 시에 속한)
    @GetMapping("/course")
    public ApiResponse<CourseDto> getRandomCourse(
            @RequestParam Integer provinceNo,
            @RequestParam Integer cityNo) {
        return ApiResponse.success(randomService.getRandomCourse(provinceNo, cityNo));
    }
} 