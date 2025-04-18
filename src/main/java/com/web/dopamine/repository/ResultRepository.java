package com.web.dopamine.repository;

import com.web.dopamine.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    
    /**
     * 도/시, 테마, 제약조건으로 결과를 검색합니다.
     */
    List<Result> findByProvinceNoAndCityNoAndThemeNoAndConstraintNo(
            Integer provinceNo, Integer cityNo, Integer themeNo, Integer constraintNo);
    
    /**
     * 도/시로 결과를 검색합니다.
     */
    List<Result> findByProvinceNoAndCityNo(Integer provinceNo, Integer cityNo);
} 