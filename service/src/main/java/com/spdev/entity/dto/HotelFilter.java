package com.spdev.entity.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HotelFilter {

    String locality;
    String name;
}
