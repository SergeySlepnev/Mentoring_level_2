package com.spdev.mapper;

import com.spdev.dto.ContentReadDto;
import com.spdev.dto.RoomReadDto;
import com.spdev.entity.Room;
import com.spdev.repository.RoomContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomReadMapper implements Mapper<Room, RoomReadDto> {

    private final RoomContentRepository roomContentRepository;
    private final ContentReadMapper contentReadMapper;

    @Override
    public RoomReadDto map(Room object) {
        return new RoomReadDto(
                object.getId(),
                object.getHotel().getId(),
                object.getRoomNo(),
                object.getType(),
                object.getSquare(),
                object.getAdultBedCount(),
                object.getChildrenBedCount(),
                object.getCost(),
                object.getFloor(),
                object.isAvailable(),
                object.getDescription(),
                getContent(object.getId())
        );
    }

    private List<ContentReadDto> getContent(Integer roomId) {
        return roomContentRepository.findByRoomId(roomId).stream()
                .map(contentReadMapper::map)
                .toList();
    }
}