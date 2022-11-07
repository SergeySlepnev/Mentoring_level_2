package com.spdev.dto;

import com.spdev.entity.enums.Rating;
import lombok.Builder;

@Builder
public record ReviewFilter(String hotelName,
                           Rating ratingFrom,
                           Rating ratingTo) {

}
