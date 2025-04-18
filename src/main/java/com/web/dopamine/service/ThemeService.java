package com.web.dopamine.service;

import com.web.dopamine.dto.ThemeDto;
import com.web.dopamine.entity.Theme;
import com.web.dopamine.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    /**
     * 모든 테마 목록을 반환합니다.
     */
    public List<ThemeDto> getAllThemes() {
        log.info("모든 테마 목록 조회");
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 테마를 ID로 조회합니다.
     */
    public ThemeDto getThemeById(Integer themeNo) {
        log.info("테마 조회: {}", themeNo);
        Theme theme = themeRepository.findById(themeNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 테마 번호입니다: " + themeNo));
        return ThemeDto.fromEntity(theme);
    }
} 