package com.web.dopamine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
@Entity
@Table(name = "inquiry")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Inquiry {
    
    @Id
    @Column(name = "no")
    private Integer no;
    
    @Column(name = "ip")
    private String ip;
    
    @Column(name = "create_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt=LocalDateTime.now();
    
    @Column(name = "title")
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    
    @Column(name = "content")
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    
    @Column(name = "email")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @Builder
    public Inquiry(Integer no, String ip, LocalDateTime createAt, String title, String content, String email) {
        this.no = no;
        this.ip = ip;
        this.createAt = createAt;
        this.title = title;
        this.content = content;
        this.email = email;
    }

} 