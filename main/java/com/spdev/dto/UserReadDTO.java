package com.spdev.dto;

import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
public class UserReadDTO {

    Integer id;
    Role role;
    String email;
    String password;
    String firstName;
    String lastName;
    LocalDate birthdate;
    String phone;
    String image;
    Status status;
    LocalDateTime registeredAt;
}