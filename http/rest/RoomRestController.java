package com.spdev.http.rest;

import com.spdev.service.RoomContentService;
import com.spdev.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomContentService roomContentService;
    private final RoomService roomService;

    @GetMapping("{roomId}/content/{contentId}")
    public ResponseEntity<byte[]> findContent(@PathVariable("roomId") Integer roomId,
                                              @PathVariable("contentId") Integer contentId) {
        return roomContentService.findRoomContent(roomId).stream()
                .filter(content -> content.getId().equals(contentId))
                .findFirst()
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.getContent().length)
                        .body(content.getContent()))
                .orElseGet(ResponseEntity.notFound()::build);
    }
}