package com.spdev.dto;

import com.spdev.entity.enums.Role;
import com.spdev.validation.CheckContentType;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    @Email(message = "{error.email}")
    @Size(min = 1, max = 128, message = "{error.length.username}")
    String username;

    @NotBlank(message = "{error.blank.password}")
    @Size(min = 3, max = 128, message = "{error.length.password}")
    String rawPassword;

    @NotBlank(message = "{error.blank.firstname}")
    @Size(min = 3, max = 128, message = "{error.length.firstname}")
    String firstName;

    @NotBlank(message = "{error.blank.lastname}")
    @Size(min = 3, max = 128, message = "{error.length.lastname}")
    String lastName;

    @NotNull(message = "{error.birthdate.null}")
    @Past(message = "{error.birthdate}")
    LocalDate birthDate;

    @NotBlank(message = "{error.blank.phone}")
    @Size(min = 3, max = 128, message = "{error.length.phone}")
    String phone;

    @NotNull(message = "{error.role.null}")
    Role role;

    @CheckContentType(message = "{error.invalid_content_type}")
    MultipartFile image;
}