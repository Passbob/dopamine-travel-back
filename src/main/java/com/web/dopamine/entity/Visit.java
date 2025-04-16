package com.web.dopamine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Builder;
@Entity
@Table(name = "visit")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Visit {
    
    @Id
    @Column(name = "no")
    private Integer no;
    
    @Column(name = "ip")
    private String ip;
    
    @Column(name = "visit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitDate=LocalDate.now();
    
    @Column(name = "result1_no")
    private Integer result1No;
    
    @Column(name = "result2_no")
    private Integer result2No;
    
    @ManyToOne
    @JoinColumn(name = "result1_no", insertable = false, updatable = false)
    private Result result1;
    
    @ManyToOne
    @JoinColumn(name = "result2_no", insertable = false, updatable = false)
    private Result result2;

    @Builder
    public Visit(Integer no, String ip, LocalDate visitDate, Integer result1No, Integer result2No, Result result1, Result result2) {
        this.no = no;
        this.ip = ip;
        this.visitDate = visitDate;
        this.result1No = result1No;
        this.result2No = result2No;
        this.result1 = result1;
        this.result2 = result2;
    }
    
} 