package com.spdev.dto;

import com.spdev.entity.enums.Status;
import lombok.Value;

@Value
public class HotelReadDto {

    Integer id;
    String name;
    Boolean available;
    Status status;
}