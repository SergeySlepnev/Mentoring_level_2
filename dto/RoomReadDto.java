package com.spdev.dto;

import com.spdev.entity.enums.RoomType;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public class RoomReadDto {

    Integer id;
    Integer hotelId;
    Integer roomNo;
    RoomType type;
    Double square;
    Integer adultBedCount;
    Integer childrenBedCount;
    BigDecimal cost;
    Integer floor;
    Boolean available;
    String description;
    List<ContentReadDto> content;

}
