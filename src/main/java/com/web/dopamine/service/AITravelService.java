package com.web.dopamine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.dopamine.dto.CourseDto;
import com.web.dopamine.entity.City;
import com.web.dopamine.entity.Province;
import com.web.dopamine.entity.Result;
import com.web.dopamine.repository.CityRepository;
import com.web.dopamine.repository.ProvinceRepository;
import com.web.dopamine.repository.ResultRepository;
import com.web.dopamine.repository.ThemeRepository;
import com.web.dopamine.repository.ConstraintRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class AITravelService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final ResultRepository resultRepository;
    private final ThemeRepository themeRepository;
    private final ConstraintRepository constraintRepository;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    /**
     * AI를 통해 여행 코스를 생성하고 저장합니다.
     */
    @Transactional
    public Result generateTravelCourse(Integer provinceNo, Integer cityNo, Integer themeNo, Integer constraintNo) {
        // 도/시 정보 조회
        Province province = provinceRepository.findById(provinceNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 provinceNo입니다: " + provinceNo));
        
        City city = cityRepository.findById(cityNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 cityNo입니다: " + cityNo));

        // 이미 정확히 동일한 지역, 테마, 제약조건의 결과가 있는지 확인하고 20% 확률로 기존 결과 반환
        List<Result> exactResults = resultRepository.findByProvinceNoAndCityNoAndThemeNoAndConstraintNo(
                provinceNo, cityNo, themeNo, constraintNo);
        if (!exactResults.isEmpty() && Math.random() < 0.2) {
            log.info("20% 확률로 정확히 일치하는 기존 여행 코스 결과 반환 - provinceNo: {}, cityNo: {}, themeNo: {}, constraintNo: {}", 
                    provinceNo, cityNo, themeNo, constraintNo);
            return exactResults.get(0);
        }

        // AI에 지역 정보 전달하고 여행 코스 생성 요청
        String location = province.getName() + " " + city.getName();
        String[] courses = generateAITravelCourse(location, themeNo, constraintNo);

        // 결과 저장
        Result result = Result.builder()
                .provinceNo(provinceNo)
                .cityNo(cityNo)
                .location(location)
                .themeNo(themeNo)
                .constraintNo(constraintNo)
                .course1(courses[0])
                .course2(courses[1])
                .course3(courses[2])
                .course4(courses[3])
                .course5(courses[4])
                .course6(courses[5])
                .createdAt(LocalDateTime.now())
                .build();
        
        return resultRepository.save(result);
    }

    /**
     * OpenAI API를 호출하여 여행 코스를 생성합니다.
     */
    private String[] generateAITravelCourse(String location, Integer themeNo, Integer constraintNo) {
        // 테마와 제약조건의 이름 가져오기
        String themeName = themeRepository.findById(themeNo)
                .map(theme -> theme.getName())
                .orElse("일반 여행");
                
        String constraintName = constraintRepository.findById(constraintNo)
                .map(constraint -> constraint.getName())
                .orElse("제약 없음");
                
        log.info("여행 코스 생성 - 위치: {}, 테마: {}, 제약조건: {}", location, themeName, constraintName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1200);

        String prompt = "한국의 " + location + "에 1박 2일 여행 갈 계획입니다. " +
                "테마는 " + themeName + "이고 제약조건은 " + constraintName + "입니다. " +
                "이 조건에 맞는 추천할만한 관광 코스 6곳을 추천해주세요. " +
                "거리와 동선을 고려하여 추천해주세요. " +
                "첫날 3곳(day:1), 둘째날 3곳(day:2)으로 구성해 주시고, " +
                "각 장소에 대해 이름, 간략한 설명, 위도(lat), 경도(lng) 정보를 포함해야 합니다. " +
                "설명에는 각 장소의 2-3가지 주요 특징이나 매력적인 포인트를 포함해주세요. 예를 들면 '유명한 볼거리, 역사적 의미, 주변 맛집, 특별한 체험' 등을 언급해주세요. " +
                "순서(order)는 첫째날은 1부터 3까지, 둘째날은 1부터 3까지 설정해주세요. " +
                "응답은 반드시 다음과 같은 JSON 형식의 배열로 제공해주세요: " +
                "[{\"name\":\"장소명\",\"description\":\"설명\",\"lat\":위도,\"lng\":경도,\"day\":1,\"order\":1}, ...] " +
                "예를 들어 서울 여행이라면 다음과 같은 형식으로 응답해주세요: " +
                "[{\"name\":\"경복궁\",\"description\":\"조선시대의 대표적인 궁궐로 아름다운 건축양식과 수문장 교대식을 볼 수 있으며, 주변에는 인사동과 북촌한옥마을이 있습니다.\",\"lat\":37.5796,\"lng\":126.9770,\"day\":1,\"order\":1}, " +
                "정확한 위도와 경도 값을 제공해주세요.";

        requestBody.put("messages", Arrays.asList(
            Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, request, String.class);

        return parseOpenAIResponse(response.getBody());
    }

    /**
     * OpenAI API 응답을 파싱합니다.
     */
    private String[] parseOpenAIResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            String content = rootNode.path("choices").get(0).path("message").path("content").asText();
            
            // JSON 배열 문자열 추출
            int startIndex = content.indexOf('[');
            int endIndex = content.lastIndexOf(']') + 1;
            
            if (startIndex >= 0 && endIndex > startIndex) {
                String jsonArrayStr = content.substring(startIndex, endIndex);
                JsonNode placesArray = objectMapper.readTree(jsonArrayStr);
                
                String[] courses = new String[6];
                for (int i = 0; i < 6 && i < placesArray.size(); i++) {
                    courses[i] = placesArray.get(i).toString();
                }
                return courses;
            } else {
                throw new RuntimeException("응답에서 JSON 배열을 찾을 수 없습니다.");
            }
            
        } catch (JsonProcessingException e) {
            log.error("OpenAI 응답 파싱 중 오류 발생", e);
            throw new RuntimeException("여행 코스 생성에 실패했습니다.", e);
        }
    }
} 