package com.spdev.service;

import com.spdev.dto.ContentReadDto;
import com.spdev.mapper.ContentReadMapper;
import com.spdev.repository.RoomContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomContentService {

    private final RoomContentRepository roomContentRepository;
    private final ContentReadMapper contentReadMapper;

    public List<ContentReadDto> findRoomContent(Integer roomId) {
        return roomContentRepository.findByRoomId(roomId).stream()
                .map(contentReadMapper::map)
                .toList();
    }
}