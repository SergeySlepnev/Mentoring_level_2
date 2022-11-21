package com.spdev.dto;

import com.spdev.entity.enums.Status;
import lombok.Value;

import javax.validation.constraints.Size;

@Value
public class HotelCreateEditDTO {

    Integer ownerId;

    @Size(min = 1, max = 128, message = "Hotel name length must be between 1 and 128")
    String name;

    Boolean available;

    Status status;
}