package com.web.dopamine.controller;

import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.common.Constants;
import com.web.dopamine.dto.ThemeDto;
import com.web.dopamine.service.ThemeService;
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
public class ThemeController {

    private final ThemeService themeService;

    /**
     * 모든 테마 목록 API
     */
    @GetMapping("/themes")
    public ApiResponse<List<ThemeDto>> getAllThemes() {
        log.info("모든 테마 목록 API 호출");
        return ApiResponse.success(themeService.getAllThemes());
    }
} 