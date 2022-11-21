package com.spdev.mapper;

import com.spdev.dto.HotelCreateEditDTO;
import com.spdev.entity.Hotel;
import com.spdev.entity.User;
import com.spdev.entity.enums.Status;
import com.spdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelCreateEditMapper implements Mapper<HotelCreateEditDTO, Hotel> {

    private final UserRepository userRepository;

    @Override
    public Hotel map(HotelCreateEditDTO object) {
        var hotel = new Hotel();
        hotel.setOwner(getUser(object.getOwnerId()));
        hotel.setName(object.getName());
        hotel.setAvailable(false);
        hotel.setStatus(Status.NEW);

        return hotel;
    }

    @Override
    public Hotel map(HotelCreateEditDTO fromObject, Hotel toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(HotelCreateEditDTO object, Hotel hotel) {
        hotel.setOwner(getUser(object.getOwnerId()));
        hotel.setName(object.getName());
        hotel.setAvailable(object.getAvailable());
        hotel.setStatus(object.getStatus());
    }

    private User getUser(Integer userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}