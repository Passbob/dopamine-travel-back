package com.web.dopamine.controller;

import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.common.Constants;
import com.web.dopamine.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.Api.BASE_PATH)
@RequiredArgsConstructor
public class MainController {
    private final VisitService visitService;

    /**
     * 총 방문자 수를 반환합니다.
     * 경로: /api/total-visits
     */
    @GetMapping("/total-visits")
    public ApiResponse<Integer> getTotalVisits() {
        log.info("총 방문자 수 API 호출");
        
        return visitService.getTotalVisit();
    }
}
