package com.spdev.dto;

import com.spdev.entity.enums.Star;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class HotelDetailsCreateEditDTO {

    Integer hotelId;

    @Size(min = 3, max = 32, message = "Phone number length must be between 3 and 32")
    String phoneNumber;

    @Size(min = 1, max = 128, message = "Country length must be between 1 and 128")
    String country;

    @Size(min = 1, max = 128, message = "Locality length must be between 1 and 128")
    String locality;

    @Size(min = 1, max = 128, message = "Area length must be between 1 and 128")
    String area;

    @Size(min = 1, max = 128, message = "Street length must be between 1 and 128")
    String street;

    @NotNull(message = "Floor count can't be empty")
    @Positive(message = "Floor count must be positive")
    Integer floorCount;

    @NotNull(message = "Star can't be empty")
    Star star;

    String description;
}