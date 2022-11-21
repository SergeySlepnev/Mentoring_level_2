package com.spdev.mapper;

import com.spdev.dto.HotelDetailsCreateEditDTO;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelDetails;
import com.spdev.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelDetailsCreateEditMapper implements Mapper<HotelDetailsCreateEditDTO, HotelDetails> {

    private final HotelRepository hotelRepository;

    @Override
    public HotelDetails map(HotelDetailsCreateEditDTO object) {
        var hotelDetails = new HotelDetails();
        copy(object, hotelDetails);

        return hotelDetails;
    }

    @Override
    public HotelDetails map(HotelDetailsCreateEditDTO fromObject, HotelDetails toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(HotelDetailsCreateEditDTO object, HotelDetails hotelDetails) {
        hotelDetails.setHotel(getHotel(object.getHotelId()));
        hotelDetails.setPhoneNumber(object.getPhoneNumber());
        hotelDetails.setCountry(object.getCountry());
        hotelDetails.setLocality(object.getLocality());
        hotelDetails.setArea(object.getArea());
        hotelDetails.setStreet(object.getStreet());
        hotelDetails.setFloorCount(object.getFloorCount());
        hotelDetails.setStar(object.getStar());
        hotelDetails.setDescription(object.getDescription());
    }

    private Hotel getHotel(Integer hotelId) {
        return Optional.ofNullable(hotelId)
                .flatMap(hotelRepository::findById)
                .orElse(null);
    }
}