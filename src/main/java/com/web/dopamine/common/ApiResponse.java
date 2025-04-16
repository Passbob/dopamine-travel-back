package com.web.dopamine.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
    
    // 성공 응답 생성 메소드
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(Constants.ResponseCode.SUCCESS)
                .message(Constants.Message.SUCCESS)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(Constants.ResponseCode.SUCCESS)
                .message(message)
                .data(data)
                .build();
    }
    
    // 실패 응답 생성 메소드
    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
    
    // 자주 사용되는 오류 응답
    public static <T> ApiResponse<T> notFound() {
        return error(Constants.ResponseCode.NOT_FOUND, Constants.Message.NOT_FOUND);
    }
    
    public static <T> ApiResponse<T> badRequest() {
        return error(Constants.ResponseCode.BAD_REQUEST, Constants.Message.BAD_REQUEST);
    }
} 