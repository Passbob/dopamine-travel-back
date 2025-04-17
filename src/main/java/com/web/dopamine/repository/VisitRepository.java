package com.web.dopamine.repository;

import com.web.dopamine.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {
    
    /**
     * 오늘 날짜와 IP로 방문 기록을 찾습니다.
     */
    @Query("SELECT v FROM Visit v WHERE v.visitDate = :visitDate AND v.ip = :ip")
    Optional<Visit> findByVisitDateAndIp(@Param("visitDate") LocalDate visitDate, @Param("ip") String ip);
    
    /**
     * Visit 테이블의 총 레코드 수를 반환합니다.
     */
    @Query("SELECT COUNT(v) FROM Visit v")
    long countTotalVisits();
} 