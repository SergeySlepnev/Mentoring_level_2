package com.dmdev.unit.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.util.LocalDateFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
@Tag("mapper")
class CreateUserMapperTest {

    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    private final CreateUserDto validCreatUserDto = CreateUserDto.builder()
            .name("Random Name")
            .birthday("2022-12-22")
            .email("RandomEmail@gmail.com")
            .password("EncryptedWord")
            .role("USER")
            .gender("MALE")
            .build();

    private final CreateUserDto invalidCreateUserDto = CreateUserDto.builder()
            .name("Random Name")
            .birthday(null)
            .email("RandomEmail@gmail.com")
            .password("EncryptedWord")
            .role("USER")
            .gender("MALE")
            .build();

    @Test
    void itShouldMapFromCreateUserDtoToUser() {

        // Если в поле birthday передать null, то при вызове  createUserMapper.map(validCreatUserDto) бросается NPE в LocalDateFormatter.format(String str).
        // Каким образом можно замокать это поведение, чтобы обойти NPE? Я пытался сделать это:

//        Mockito.when(LocalDateFormatter.format(Mockito.anyString())).thenReturn(null);
//        Mockito.doReturn(null).when(LocalDateFormatter.format(Mockito.anyString()));
//        Mockito.verify(LocalDateFormatter.format(validCreatUserDto.getBirthday())).

        var expectedUser = createUserMapper.map(validCreatUserDto);


        Assertions.assertAll(
                () -> assertThat(expectedUser.getName()).isEqualTo("Random Name"),
                () -> assertThat(expectedUser.getBirthday()).isEqualTo(LocalDateFormatter.format("2022-12-22")),
                () -> assertThat(expectedUser.getEmail()).isEqualTo("RandomEmail@gmail.com"),
                () -> assertThat(expectedUser.getRole()).isEqualTo(Role.USER),
                () -> assertThat(expectedUser.getGender()).isEqualTo(Gender.MALE)
        );
    }

    //Это скорее интеграционный тест, правильно?
    @Test
    void itShouldThrowNPEIfBirthdayNull() {
        assertThrows(NullPointerException.class, () -> createUserMapper.map(invalidCreateUserDto));
    }
}