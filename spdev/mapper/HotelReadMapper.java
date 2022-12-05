package com.spdev.mapper;

import com.spdev.dto.HotelReadDto;
import com.spdev.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelReadMapper implements Mapper<Hotel, HotelReadDto> {

    @Override
    public HotelReadDto map(Hotel object) {
        return new HotelReadDto(
                object.getId(),
                object.getName(),
                object.isAvailable(),
                object.getStatus());
    }
}