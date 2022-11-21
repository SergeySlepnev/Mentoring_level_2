package com.spdev.dto;

import lombok.Value;

@Value
public class HotelReadDTO {

    Integer id;
    String name;
    Boolean available;
}