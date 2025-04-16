package com.web.dopamine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Table(name = "city")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class City {
    
    @Id
    @Column(name = "no")
    private Integer no;
    
    @Column(name = "province_no")
    private Integer provinceNo;
    
    @Column(name = "name")
    private String name;

    @Builder
    public City(Integer no, Integer provinceNo, String name) {
        this.no = no;
        this.provinceNo = provinceNo;
        this.name = name;
    }
} 