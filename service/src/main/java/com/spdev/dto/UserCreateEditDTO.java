package com.spdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Value
@FieldNameConstants
public class UserCreateEditDTO {

    String email;
    String password;
    String firstName;
    String lastName;
    LocalDate birthdate;
    String phone;
    String photoLink;
}