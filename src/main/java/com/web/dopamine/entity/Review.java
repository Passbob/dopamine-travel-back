package com.web.dopamine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Builder;
@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Integer no;
    
    @Column(name = "result_no")
    private Integer resultNo;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "content")
    private String content;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "count")
    private Integer count;
    
    @Column(name = "create_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt=LocalDateTime.now();
    
    @Column(name = "city_no")
    private Integer cityNo;
    
    @Column(name = "privince_no")
    private Integer provinceNo;
    
    @ManyToOne
    @JoinColumn(name = "result_no", insertable = false, updatable = false)
    private Result result;
    
    @ManyToOne
    @JoinColumn(name = "city_no", insertable = false, updatable = false)
    private City city;
    
    @ManyToOne
    @JoinColumn(name = "privince_no", insertable = false, updatable = false)
    private Province province;

    @Builder
    public Review(Integer no, Integer resultNo, String title, String content, Integer rating, Integer count, LocalDateTime createAt, Integer cityNo, Integer provinceNo, Result result, City city, Province province) {
        this.no = no;
        this.resultNo = resultNo;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.count = count;
        this.createAt = createAt;
        this.cityNo = cityNo;
        this.provinceNo = provinceNo;
        this.result = result;
        this.city = city;
        this.province = province;
    }
} 