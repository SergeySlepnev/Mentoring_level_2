package com.spdev.mapper;

import com.spdev.dto.HotelContentReadDTO;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelContentReadMapper implements Mapper<HotelContent, HotelContentReadDTO> {

    private final HotelRepository hotelRepository;

    @Override
    public HotelContentReadDTO map(HotelContent object) {
        return new HotelContentReadDTO(
                object.getId(),
                object.getHotel().getId(),
                object.getLink(),
                object.getType().name()
        );
    }

    private Hotel getHotel(Integer hotelId) {
        return Optional.ofNullable(hotelId)
                .flatMap(hotelRepository::findById)
                .orElse(null);
    }
}