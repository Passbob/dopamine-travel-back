package com.web.dopamine.repository;

import com.web.dopamine.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    
    // 특정 도에 속한 시/군 목록 조회
    List<City> findByProvinceNo(Integer provinceNo);
} 