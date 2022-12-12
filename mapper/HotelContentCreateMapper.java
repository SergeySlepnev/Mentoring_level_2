package com.spdev.mapper;

import com.spdev.dto.HotelContentCreateDto;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class HotelContentCreateMapper implements Mapper<HotelContentCreateDto, HotelContent> {

    private final HotelRepository hotelRepository;

    @Override
    public HotelContent map(HotelContentCreateDto object) {
        var hotelContent = new HotelContent();
        copy(object, hotelContent);

        return hotelContent;
    }

    @Override
    public HotelContent map(HotelContentCreateDto fromObject, HotelContent toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(HotelContentCreateDto object, HotelContent hotelContent) {
        hotelContent.setHotel(getHotel(object.getHotelId()));
        Optional.ofNullable(object.getLink())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(content -> hotelContent.setLink(content.getOriginalFilename()));
        hotelContent.setType(object.getContentType());
    }

    private Hotel getHotel(Integer hotelId) {
        return Optional.ofNullable(hotelId)
                .flatMap(hotelRepository::findById)
                .orElse(null);
    }
}