package com.web.dopamine.dto;

import com.web.dopamine.entity.Constraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintDto {
    private Integer no;
    private String name;
    
    public static ConstraintDto fromEntity(Constraint constraint) {
        return ConstraintDto.builder()
                .no(constraint.getNo())
                .name(constraint.getName())
                .build();
    }
} 