package com.web.dopamine.dto;

import com.web.dopamine.entity.City;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Integer no;
    private Integer provinceNo;
    private String name;
    
    // Entity -> DTO 변환
    public static CityDto fromEntity(City city) {
        return CityDto.builder()
                .no(city.getNo())
                .provinceNo(city.getProvinceNo())
                .name(city.getName())
                .build();
    }
} 