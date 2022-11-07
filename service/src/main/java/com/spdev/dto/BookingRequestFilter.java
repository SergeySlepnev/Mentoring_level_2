package com.spdev.dto;

import com.spdev.entity.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookingRequestFilter(LocalDateTime createdAtFrom,
                                   LocalDateTime createdAtTo,
                                   String hotelName,
                                   String userPhone,
                                   Status status) {

}
