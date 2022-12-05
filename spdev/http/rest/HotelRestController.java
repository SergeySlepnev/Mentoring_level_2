package com.spdev.http.rest;

import com.spdev.dto.ContentReadDto;
import com.spdev.service.HotelContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelRestController {

    private final HotelContentService hotelContentService;

    @GetMapping(value = "/{hotelId}/content/{contentId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findContent(@PathVariable("hotelId") Integer hotelId,
                              @PathVariable("contentId") Integer contentId) {
        return hotelContentService.findContent(hotelId)
                .stream()
                .filter(content -> content.getId().equals(contentId))
                .findFirst()
                .map(ContentReadDto::getContent)
                .orElseThrow();
    }
}