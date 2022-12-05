package com.spdev.dto;

import com.spdev.entity.enums.Status;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class HotelCreateEditDto {

    Integer ownerId;

    @NotBlank(message = "{error.blank.hotel_name}")
    @Size(min = 1, max = 128, message = "{error.length.hotel_name}")
    String name;

    @NotNull(message = "{error.null.available}")
    Boolean available;

    @NotNull(message = "{error.null.status}")
    Status status;
}