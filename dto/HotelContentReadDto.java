package com.spdev.dto;

import lombok.Value;

@Value
public class HotelContentReadDto {

    Integer id;

    Integer hotelId;

    String link;

    String type;
}