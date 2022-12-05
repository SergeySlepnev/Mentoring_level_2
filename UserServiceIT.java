package com.spdev.integration.service;

import com.spdev.dto.UserCreateEditDto;
import com.spdev.entity.enums.Role;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    public static final Integer USER_1 = 1;
    private static final Integer NONEXISTENT_USER_ID = -10;

    private final UserService userService;

    @Test
    void checkFindAll() {
        var actualUsers = userService.findAll();
        assertThat(actualUsers).hasSize(5);
    }

    @Test
    void checkFindById() {
        var actualUser = userService.findById(USER_1);

        assertThat(actualUser).isPresent();

        actualUser.ifPresent(user -> assertAll(
                () -> assertEquals("Sergey", user.getFirstName()),
                () -> assertEquals("Sidorov", user.getLastName()),
                () -> assertEquals("8-835-66-99-333", user.getPhone())
        ));
    }

    @Test
    void checkCreate() {
        var userDto = new UserCreateEditDto(
                "TestUserEmail@gail.com",
                "TestPassword",
                "TestFirstName",
                "TestLastName",
                LocalDate.of(1991, 9, 28),
                "8-258-658-55-55",
                Role.USER,
                new MockMultipartFile("avatar", new byte[]{})
        );

        var actualUser = userService.create(userDto);

        assertAll(
                () -> assertThat(actualUser.getFirstName()).isEqualTo("TestFirstName"),
                () -> assertThat(actualUser.getLastName()).isEqualTo("TestLastName"),
                () -> assertThat(actualUser.getBirthdate()).isEqualTo(LocalDate.of(1991, 9, 28)),
                () -> assertThat(actualUser.getPhone()).isEqualTo("8-258-658-55-55"),
                () -> assertThat(actualUser.getImage()).isNull()
        );
    }

    @Test
    void checkUpdate() {
        var userDto = new UserCreateEditDto(
                "TestUserEmail@gail.com",
                "TestPassword",
                "TestFirstName",
                "TestLastName",
                LocalDate.of(1991, 9, 28),
                "8-258-658-55-55",
                Role.USER,
                new MockMultipartFile("avatar", new byte[]{})
        );

        var actualUser = userService.update(USER_1, userDto);

        assertThat(actualUser).isPresent();
        actualUser.ifPresent(user ->
                assertAll(
                        () -> assertThat(user.getFirstName()).isEqualTo("TestFirstName"),
                        () -> assertThat(user.getLastName()).isEqualTo("TestLastName"),
                        () -> assertThat(user.getBirthdate()).isEqualTo(LocalDate.of(1991, 9, 28)),
                        () -> assertThat(user.getPhone()).isEqualTo("8-258-658-55-55")
                ));
    }

    @Test
    void checkDelete() {
        assertFalse(userService.delete(NONEXISTENT_USER_ID));
        assertTrue(userService.delete(USER_1));
    }
}