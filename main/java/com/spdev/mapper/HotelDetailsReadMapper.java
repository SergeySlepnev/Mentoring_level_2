package com.spdev.mapper;

import com.spdev.dto.HotelDetailsReadDTO;
import com.spdev.entity.HotelDetails;
import org.springframework.stereotype.Component;

@Component
public class HotelDetailsReadMapper implements Mapper<HotelDetails, HotelDetailsReadDTO> {

    @Override
    public HotelDetailsReadDTO map(HotelDetails object) {
        return new HotelDetailsReadDTO(
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