package com.web.dopamine.controller;

import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.common.Constants;
import com.web.dopamine.dto.CourseDto;
import com.web.dopamine.entity.Result;
import com.web.dopamine.service.AITravelService;
import com.web.dopamine.service.RandomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.Api.BASE_PATH + "/ai-travel")
@RequiredArgsConstructor
public class AITravelController {

    private final AITravelService aiTravelService;
    private final RandomService randomService;
    
    /**
     * 특정 도/시에 대한 AI 여행 코스 생성 및 조회
     */
    @PostMapping("/generate")
    public ApiResponse<Result> generateTravelCourse(
            @RequestParam Integer provinceNo,
            @RequestParam Integer cityNo,
            @RequestParam Integer themeNo,
            @RequestParam Integer constraintNo

    ) {
        Result result = aiTravelService.generateTravelCourse(provinceNo, cityNo, themeNo, constraintNo);
        return ApiResponse.success(result);
    }
} 