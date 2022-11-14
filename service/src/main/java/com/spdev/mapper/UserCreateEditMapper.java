package com.spdev.mapper;

import com.spdev.dto.UserCreateEditDTO;
import com.spdev.entity.PersonalInfo;
import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDTO, User> {

    @Override
    public User map(UserCreateEditDTO object) {
        var user = new User();
        copy(object, user);

        return user;
    }

    @Override
    public User map(UserCreateEditDTO fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private static void copy(UserCreateEditDTO object, User user) {
        var personalInfo = new PersonalInfo();
        personalInfo.setFirstname(object.getFirstName());
        personalInfo.setLastname(object.getLastName());
        personalInfo.setBirthDate(object.getBirthdate());

        user.setRole(Role.USER);
        user.setEmail(object.getEmail());
        user.setPassword(object.getPassword());
        user.setPersonalInfo(personalInfo);
        user.setPhone(object.getPhone());
        user.setPhotoLink(object.getPhotoLink());
        user.setStatus(Status.NEW);
        user.setRegisteredAt(LocalDateTime.now());
    }
}