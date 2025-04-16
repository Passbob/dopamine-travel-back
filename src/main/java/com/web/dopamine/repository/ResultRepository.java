package com.web.dopamine.repository;

import com.web.dopamine.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    
    // 특정 도와 시에 속한 결과 목록 조회
    List<Result> findByProvinceNoAndCityNo(Integer provinceNo, Integer cityNo);
} 