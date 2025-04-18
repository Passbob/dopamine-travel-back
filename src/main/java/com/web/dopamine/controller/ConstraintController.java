package com.web.dopamine.controller;

import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.common.Constants;
import com.web.dopamine.dto.ConstraintDto;
import com.web.dopamine.service.ConstraintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.Api.BASE_PATH)
@RequiredArgsConstructor
public class ConstraintController {

    private final ConstraintService constraintService;

    /**
     * 모든 제약조건 목록 API
     */
    @GetMapping("/constraints")
    public ApiResponse<List<ConstraintDto>> getAllConstraints() {
        log.info("모든 제약조건 목록 API 호출");
        return ApiResponse.success(constraintService.getAllConstraints());
    }
} 