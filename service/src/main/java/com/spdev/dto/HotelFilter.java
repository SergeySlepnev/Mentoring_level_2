package com.spdev.dto;

import com.spdev.entity.enums.Star;
import com.spdev.entity.enums.Status;
import lombok.Builder;

@Builder
public record HotelFilter(String name,
                          Status status,
                          Boolean available,
                          String country,
                          String locality,
                          Star star) {

}
