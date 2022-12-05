package com.spdev.mapper;

import com.spdev.dto.HotelDetailsReadDto;
import com.spdev.entity.HotelDetails;
import org.springframework.stereotype.Component;

@Component
public class HotelDetailsReadMapper implements Mapper<HotelDetails, HotelDetailsReadDto> {

    @Override
    public HotelDetailsReadDto map(HotelDetails object) {
        return new HotelDetailsReadDto(
                object.getId(),
                object.getHotel().getId(),
                object.getPhoneNumber(),
                object.getCountry(),
                object.getLocality(),
                object.getArea(),
                object.getStreet(),
                object.getFloorCount(),
                object.getStar(),
                object.getDescription()
        );
    }
}