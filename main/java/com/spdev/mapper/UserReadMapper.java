package com.spdev.mapper;

import com.spdev.dto.UserReadDTO;
import com.spdev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDTO> {

    @Override
    public UserReadDTO map(User object) {
        return new UserReadDTO(
                object.getId(),
                object.getRole(),
                object.getEmail(),
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