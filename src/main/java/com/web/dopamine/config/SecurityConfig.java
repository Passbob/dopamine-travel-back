package com.web.dopamine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (REST API 서버에서는 일반적으로 필요 없음)
            .csrf(csrf -> csrf.disable())
            
            // CORS 설정 활성화 (중요!)
            .cors(cors -> cors.configure(http))
            
            // 요청 경로별 인증 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()  // 모든 API 엔드포인트 허용
                .requestMatchers("/health", "/actuator/**").permitAll()  // 헬스 체크 및 모니터링 허용
                .anyRequest().authenticated())  // 다른 모든 요청은 인증 필요
                
            // 기본 인증 비활성화 (API 키 또는 다른 인증 방식 사용 예정)
            .httpBasic(httpBasic -> httpBasic.disable());
        
        return http.build();
    }
} 