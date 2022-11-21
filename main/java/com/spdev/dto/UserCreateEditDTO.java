package com.spdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@FieldNameConstants
public class UserCreateEditDTO {

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Size(min = 6, max = 64)
    String password;

    @NotBlank
    @Max(64)
    String firstName;

    @NotBlank
    @Max(64)
    String lastName;

    @Past
    LocalDate birthdate;

    @NotBlank
    String phone;

    @NotBlank
    MultipartFile image;
}