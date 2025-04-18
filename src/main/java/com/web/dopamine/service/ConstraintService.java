package com.web.dopamine.service;

import com.web.dopamine.dto.ConstraintDto;
import com.web.dopamine.entity.Constraint;
import com.web.dopamine.repository.ConstraintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConstraintService {

    private final ConstraintRepository constraintRepository;

    /**
     * 모든 제약조건 목록을 반환합니다.
     */
    public List<ConstraintDto> getAllConstraints() {
        log.info("모든 제약조건 목록 조회");
        List<Constraint> constraints = constraintRepository.findAll();
        return constraints.stream()
                .map(ConstraintDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 제약조건을 ID로 조회합니다.
     */
    public ConstraintDto getConstraintById(Integer constraintNo) {
        log.info("제약조건 조회: {}", constraintNo);
        Constraint constraint = constraintRepository.findById(constraintNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 제약조건 번호입니다: " + constraintNo));
        return ConstraintDto.fromEntity(constraint);
    }
} 