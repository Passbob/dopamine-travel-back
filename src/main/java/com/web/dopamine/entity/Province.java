package com.web.dopamine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
@Entity
@Table(name = "province")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Province {
    
    @Id
    @Column(name = "no")
    private Integer no;
    
    @Column(name = "name")
    private String name;

    @Builder
    public Province(Integer no, String name) {
        this.no = no;
        this.name = name;
    }
} 