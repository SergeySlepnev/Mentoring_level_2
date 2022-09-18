package com.dmdev.integration.service;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.of;

@Tag("integration")
@Tag("service")
@DisplayName("Integration functionality of the service layer")
@ExtendWith(MockitoExtension.class)
class UserServiceIT extends IntegrationTestBase {

    public static final String LOGIN_TASTE_SOURCE_DATA = "/login-test-data.csv";
    private static final char COMMA = ',';
    private static final int NUM_LINES_TO_SKIP = 1;

    @InjectMocks
    private UserService userService;

    private final CreateUserDto createUserDto = CreateUserDto.builder()
            .name("name")
            .birthday("2010-10-12")
            .email("email@gmail.com")
            .password("encryptedWord")
            .role(Role.USER.name())
            .gender(Gender.MALE.name())
            .build();

    @Test
    void ifShouldCreateNewUserInDataBase() {
        var expectedUserDto = userService.create(createUserDto);

        assertAll(
                () -> assertThat(createUserDto.getName()).isEqualTo(expectedUserDto.getName()),
                () -> assertThat(createUserDto.getBirthday()).isEqualTo((expectedUserDto.getBirthday().toString())),
                () -> assertThat(createUserDto.getEmail()).isEqualTo(expectedUserDto.getEmail()),
                () -> assertThat(createUserDto.getRole()).isEqualTo(expectedUserDto.getRole().name()),
                () -> assertThat(createUserDto.getGender()).isEqualTo(expectedUserDto.getGender().name())
        );
    }

    @ParameterizedTest
    @Tag("login")
    @CsvFileSource(resources = LOGIN_TASTE_SOURCE_DATA, delimiter = COMMA, numLinesToSkip = NUM_LINES_TO_SKIP)
    void correctLoginParametrizedTest(String username, String password) {
        var maybeUser = userService.login(username, password);
        assertThat(maybeUser).isPresent();
    }

    @ParameterizedTest
    @Tag("login")
    @MethodSource("provideInvalidDataForLoginTest")
    void itShouldLoginIfEmailOrPasswordIncorrect(String email, String password) {
        var maybeUser = userService.login(email, password);
        assertThat(maybeUser).isEmpty();
    }

    private static Stream<Arguments> provideInvalidDataForLoginTest() {
        return Stream.of(
                of("ivan@gmail.com", "dummy"),
                of("Ivan@gmail.com", "123"),
                of("dummy@gmail..com", "123"),
                of("ivan@gmail.com", null),
                of(null, "123"),
                of(null, null)
        );
    }
}