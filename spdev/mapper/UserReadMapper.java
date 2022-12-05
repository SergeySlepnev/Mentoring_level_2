package com.spdev.mapper;

import com.spdev.dto.UserReadDto;
import com.spdev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getRole(),
                object.getUsername(),
                object.getPassword(),
                object.getPersonalInfo().getFirstname(),
                object.getPersonalInfo().getLastname(),
                object.getPersonalInfo().getBirthDate(),
                object.getPhone(),
                object.getImage(),
                object.getStatus(),
                object.getRegisteredAt());
    }
}