package com.spdev.dto;

import lombok.Value;

@Value
public class HotelContentReadDTO {

    Integer id;
    Integer hotelId;
    String link;
    String type;
}
