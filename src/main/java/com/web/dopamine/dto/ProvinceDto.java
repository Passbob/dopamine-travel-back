package com.web.dopamine.dto;

import com.web.dopamine.entity.Province;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto {
    private Integer no;
    private String name;
    
    // Entity -> DTO 변환
    public static ProvinceDto fromEntity(Province province) {
        return ProvinceDto.builder()
                .no(province.getNo())
                .name(province.getName())
                .build();
    }
} 