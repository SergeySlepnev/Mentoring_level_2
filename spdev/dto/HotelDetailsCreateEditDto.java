package com.spdev.dto;

import com.spdev.entity.enums.Star;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class HotelDetailsCreateEditDto {

    Integer hotelId;

    @NotBlank(message = "{error.blank.phone}")
    @Size(min = 3, max = 128, message = "{error.length.phone}")
    String phoneNumber;

    @NotBlank(message = "{error.blank.country}")
    @Size(min = 1, max = 128, message = "{error.length.country}")
    String country;

    @NotBlank(message = "{error.blank.locality}")
    @Size(min = 1, max = 128, message = "{error.length.locality}")
    String locality;

    @NotBlank(message = "{error.blank.area}")
    @Size(min = 1, max = 128, message = "{error.length.area}")
    String area;

    @NotBlank(message = "{error.blank.street}")
    @Size(min = 1, max = 128, message = "{error.length.street}")
    String street;

    @NotNull(message = "{error.null.floor_count}")
    @Positive(message = "{error.negative.floor_count}")
    Integer floorCount;

    @NotNull(message = "{error.null.star}")
    Star star;

    String description;
}