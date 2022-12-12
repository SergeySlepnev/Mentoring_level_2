package com.spdev.dto;

import com.spdev.entity.enums.Star;
import lombok.Value;

@Value
public class HotelDetailsReadDto {

    Integer id;
    Integer hotelId;
    String phoneNumber;
    String country;
    String locality;
    String area;
    String street;
    Integer floorCount;
    Star star;
    String description;
}