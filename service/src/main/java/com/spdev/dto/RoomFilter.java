package com.spdev.dto;

import com.spdev.entity.enums.RoomType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RoomFilter(RoomType type,
                         Boolean available,
                         BigDecimal costFrom,
                         BigDecimal costTo) {

}
