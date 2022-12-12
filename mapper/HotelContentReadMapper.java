package com.spdev.mapper;

import com.spdev.dto.HotelContentReadDto;
import com.spdev.entity.HotelContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelContentReadMapper implements Mapper<HotelContent, HotelContentReadDto> {

    @Override
    public HotelContentReadDto map(HotelContent object) {
        return new HotelContentReadDto(
                object.getId(),
                object.getHotel().getId(),
                object.getLink(),
                object.getType().name()
        );
    }
}