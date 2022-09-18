package com.dmdev.unit;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@Tag("validate")
class CreateUserValidatorTest {

    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();

    private final CreateUserDto validCreateUserDto = CreateUserDto.builder()
            .name("name")
            .birthday("2010-10-12")
            .email("email@gmail.com")
            .password("encryptedWord")
            .role(Role.USER.name())
            .gender(Gender.MALE.name())
            .build();

    private final CreateUserDto invalidCreateUserDto = CreateUserDto.builder()
            .name("name")
            .birthday("2010.10.12")
            .email("email@gmail.com")
            .password("encryptedWord")
            .role(null)
            .gender(null)
            .build();

    @Test
    void itShouldValidateCreateUserDto() {
        var expectedValidationResult = createUserValidator.validate(validCreateUserDto);
        assertTrue(expectedValidationResult.isValid());
    }

    @Test
    void itShouldNotValidateCreateUserDto() {
        var expectedValidationResult = createUserValidator.validate(invalidCreateUserDto);
        assertFalse(expectedValidationResult.isValid());
    }
}