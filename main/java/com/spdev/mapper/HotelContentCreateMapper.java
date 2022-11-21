package com.spdev.mapper;

import com.spdev.dto.HotelContentCreateDTO;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelContentCreateMapper implements Mapper<HotelContentCreateDTO, HotelContent> {

    private final HotelRepository hotelRepository;

    @Override
    public HotelContent map(HotelContentCreateDTO object) {
        var hotelContent = new HotelContent();
        hotelContent.setHotel(getHotel(object.getHotelId()));
        hotelContent.setLink(object.getLink().getOriginalFilename());
        hotelContent.setType(object.getContentType());

        return hotelContent;
    }

    private Hotel getHotel(Integer hotelId) {
        return Optional.ofNullable(hotelId)
                .flatMap(hotelRepository::findById)
                .orElse(null);
    }
}