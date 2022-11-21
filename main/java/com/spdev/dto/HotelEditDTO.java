package com.spdev.dto;

import com.spdev.entity.enums.Status;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Value
public class HotelEditDTO {

    Integer ownerId;

    @NotBlank
    @Max(message = "name string must be less than or equal to 128", value = 128)
    String name;

    Boolean available;

    Status status;
}