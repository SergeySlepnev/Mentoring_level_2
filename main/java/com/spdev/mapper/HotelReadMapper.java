package com.spdev.mapper;

import com.spdev.dto.HotelReadDTO;
import com.spdev.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelReadMapper implements Mapper<Hotel, HotelReadDTO> {

    @Override
    public HotelReadDTO map(Hotel object) {
        return new HotelReadDTO(
                object.getId(),
                object.getName(),
                object.isAvailable());
    }
}