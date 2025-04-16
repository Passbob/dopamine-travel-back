package com.web.dopamine.common;

public class Constants {
    // API 경로
    public static class Api {
        public static final String BASE_PATH = "/api";
        public static final String INQUIRY = "/inquiry";
        public static final String VISIT = "/visit";
        public static final String REVIEW = "/review";
        public static final String RESULT = "/result";
        public static final String RANDOM = "/random";
        public static final String PROVINCE = "/province";
        public static final String CITY = "/city";
    }
    
    // 응답 코드
    public static class ResponseCode {
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
        public static final String NOT_FOUND = "NOT_FOUND";
        public static final String BAD_REQUEST = "BAD_REQUEST";
    }
    
    // 메시지
    public static class Message {
        public static final String SUCCESS = "요청이 성공적으로 처리되었습니다.";
        public static final String NOT_FOUND = "요청한 리소스를 찾을 수 없습니다.";
        public static final String BAD_REQUEST = "잘못된 요청입니다.";
        public static final String SERVER_ERROR = "서버 오류가 발생했습니다.";
    }
}
