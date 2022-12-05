package com.spdev.mapper;

import com.spdev.dto.ContentReadDto;
import com.spdev.entity.HotelContent;
import com.spdev.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentReadMapper implements Mapper<HotelContent, ContentReadDto> {

    private final ContentService contentService;

    @Override
    public ContentReadDto map(HotelContent object) {
        return new ContentReadDto(
                object.getId(),
                getContent(object.getLink()));
    }

    private byte[] getContent(String link) {
        return contentService.getImage(link).orElseThrow();
    }
}