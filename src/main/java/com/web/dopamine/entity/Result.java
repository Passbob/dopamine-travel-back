package com.web.dopamine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Builder;

@Entity
@Table(name = "result")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Result {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Integer no;
    
    @Column(name = "city_no")
    private Integer cityNo;
    
    @Column(name = "province_no")
    private Integer provinceNo;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "course1", columnDefinition = "TEXT")
    private String course1;
    
    @Column(name = "course2", columnDefinition = "TEXT")
    private String course2;
    
    @Column(name = "course3", columnDefinition = "TEXT")
    private String course3;
    
    @Column(name = "course4", columnDefinition = "TEXT")
    private String course4;
    
    @Column(name = "course5", columnDefinition = "TEXT")
    private String course5;
    
    @Column(name = "course6", columnDefinition = "TEXT")
    private String course6;
    
    @Column(name = "course7", columnDefinition = "TEXT")
    private String course7;
    
    @Column(name = "course8", columnDefinition = "TEXT")
    private String course8;
    
    @Column(name = "course9", columnDefinition = "TEXT")
    private String course9;
    
    @Column(name = "course10", columnDefinition = "TEXT")
    private String course10;
    
    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "city_no", insertable = false, updatable = false)
    private City city;
    
    @ManyToOne
    @JoinColumn(name = "province_no", insertable = false, updatable = false)
    private Province province;

    @Builder
    public Result(Integer no, Integer cityNo, Integer provinceNo, String location, String course1, String course2, String course3, String course4, String course5, String course6, String course7, String course8, String course9, String course10, LocalDateTime createdAt, City city, Province province) {
        this.no = no;
        this.cityNo = cityNo;
        this.provinceNo = provinceNo;
        this.location = location;   
        this.course1 = course1;
        this.course2 = course2;
        this.course3 = course3;
        this.course4 = course4;
        this.course5 = course5;
        this.course6 = course6;
        this.course7 = course7;
        this.course8 = course8;
        this.course9 = course9;
        this.course10 = course10;
        this.createdAt = createdAt;
        this.city = city;
        this.province = province;
    }
} 