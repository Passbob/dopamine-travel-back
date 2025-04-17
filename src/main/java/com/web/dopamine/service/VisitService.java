package com.web.dopamine.service;

import com.web.dopamine.common.ApiResponse;
import com.web.dopamine.repository.VisitRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;

    /**
     * 총 방문자 수를 반환합니다.
     */
    public ApiResponse<Integer> getTotalVisit() {
        long totalCount = visitRepository.countTotalVisits();
        log.info("총 방문자 수 조회: {}", totalCount);
        return ApiResponse.success((int)totalCount);
    }
}