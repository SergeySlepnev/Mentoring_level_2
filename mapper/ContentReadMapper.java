package com.spdev.mapper;

import com.spdev.dto.ContentReadDto;
import com.spdev.entity.ApplicationContent;
import com.spdev.service.ApplicationContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentReadMapper implements Mapper<ApplicationContent, ContentReadDto> {

    private final ApplicationContentService applicationContentService;

    @Override
    public ContentReadDto map(ApplicationContent object) {
        return new ContentReadDto(
                object.getId(),
                getContent(object.getLink()));
    }

    private byte[] getContent(String link) {
        return applicationContentService.getContent(link).orElseThrow();
    }
}