package com.web.dopamine.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Integer no;
    private Integer cityNo;
    private Integer provinceNo;
    private Integer themeNo;
    private Integer constraintNo;
    private String location;
    private PlaceDto course1;
    private PlaceDto course2;
    private PlaceDto course3;
    private PlaceDto course4;
    private PlaceDto course5;
    private PlaceDto course6;
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceDto {
        private String name;
        private String description;
        private Double lat;
        private Double lng;
        private Integer day;
        private Integer order;
    }
} 