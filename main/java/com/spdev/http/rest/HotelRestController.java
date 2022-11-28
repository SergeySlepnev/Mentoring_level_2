package com.spdev.http.rest;

import com.spdev.service.HotelContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelRestController {

    private final HotelContentService hotelContentService;

    @GetMapping(value = "/{id}/content", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findContent(@PathVariable("id") Integer id) {
        return hotelContentService.findContent(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}