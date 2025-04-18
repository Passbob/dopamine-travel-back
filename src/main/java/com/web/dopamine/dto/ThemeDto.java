package com.web.dopamine.dto;

import com.web.dopamine.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {
    private Integer no;
    private String name;
    
    public static ThemeDto fromEntity(Theme theme) {
        return ThemeDto.builder()
                .no(theme.getNo())
                .name(theme.getName())
                .build();
    }
} 