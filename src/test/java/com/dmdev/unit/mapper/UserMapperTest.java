package com.dmdev.unit.mapper;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.UserMapper;
import com.dmdev.util.LocalDateFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@Tag("mapper")
class UserMapperTest {

    private final UserMapper userMapper = UserMapper.getInstance();

    private final User user = User.builder()
            .id(1)
            .name("Random Name")
            .birthday(LocalDateFormatter.format("2022-12-22"))
            .email("RandomEmail@gmail.com")
            .password("EncryptedWord")
            .role(Role.USER)
            .gender(Gender.MALE)
            .build();

    @Test
    void shouldMapFromUserToUserDto() {
        var expectedUserDto = userMapper.map(user);

        Assertions.assertAll(
                () -> assertThat(expectedUserDto.getName()).isEqualTo("Random Name"),
                () -> assertThat(expectedUserDto.getBirthday()).isEqualTo(LocalDateFormatter.format("2022-12-22")),
                () -> assertThat(expectedUserDto.getEmail()).isEqualTo("RandomEmail@gmail.com"),
                () -> assertThat(expectedUserDto.getRole()).isEqualTo(Role.USER),
                () -> assertThat(expectedUserDto.getGender()).isEqualTo(Gender.MALE)
        );
    }
}