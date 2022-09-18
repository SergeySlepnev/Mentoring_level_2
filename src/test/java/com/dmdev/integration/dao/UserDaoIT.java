package com.dmdev.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.util.LocalDateFormatter;
import com.dmdev.util.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;

@Tag("integration")
@Tag("user")
@DisplayName("Integration functionality of the DAO layer")
@ExtendWith(MockitoExtension.class)
class UserDaoIT extends IntegrationTestBase {

    @InjectMocks
    private static UserDao userDao;

    private final User validUser = User.builder()
            .id(RandomUtils.getId())
            .name(RandomUtils.randomUserName())
            .birthday(LocalDateFormatter.format(RandomUtils.randomDate()))
            .email(RandomUtils.randomEmail())
            .password(RandomUtils.randomPassword())
            .role(Role.USER)
            .gender(Gender.MALE)
            .build();

    @Nested
    @Tag("find")
    @DisplayName("Functionality for searching users in the database")
    class FindTest {

        @Test
        void itShouldFindAllUsersInDataBase() {
            userDao.save(validUser);

            var allUsers = userDao.findAll();

            assertThat(allUsers).hasSize(6);
        }

        @Test
        void itShouldFindUserById() {
            userDao.save(validUser);

            var expectedUserById = userDao.findById(validUser.getId());

            assertTrue(expectedUserById.isPresent());
        }

        @Test
        void itShouldNotFindUserIfIdNegative() {
            var expectedResult = userDao.findById(-1);

            assertThat(expectedResult).isEmpty();
        }

        @ParameterizedTest
        @MethodSource("provideCorrectArgumentsForFindingUserByEmailAndPassword")
        void itShouldFindUserByEmailAndPassword(String email, String password) {

            var expectedUserByEmailAndPassword = userDao.findByEmailAndPassword(email, password);

            assertThat(expectedUserByEmailAndPassword).isPresent();
        }

        private static Stream<Arguments> provideCorrectArgumentsForFindingUserByEmailAndPassword() {
            return Stream.of(
                    of("petr@gmail.com", "123"),
                    of("sveta@gmmail.com", "321")
            );
        }

        @ParameterizedTest
        @MethodSource("provideWrongArgumentsForFindingUserByEmailAndPassword")
        void itShouldNotFindUserIfEmailOrPasswordWrong(String email, String password) {
            var expectedUser = userDao.findByEmailAndPassword(email, password);

            assertThat(expectedUser).isEmpty();
        }

        private static Stream<Arguments> provideWrongArgumentsForFindingUserByEmailAndPassword() {
            return Stream.of(
                    of(null, "123"),
                    of("petr@gmail.com", null),
                    of("dummy", "dummy"),
                    of(null, null)
            );
        }
    }

    @Nested
    @Tag("save")
    @DisplayName("Functionality for saving a user in the database")
    class SaveTest {

        @Test
        void itShouldSaveNewValidUserInDataBase() {
            var expectedUser = userDao.save(validUser);

            assertThat(validUser).isEqualTo(expectedUser);
        }

        @ParameterizedTest
        @MethodSource("provideInvalidUsers")
        void itThrowsExceptionIfUserToSaveInvalid(User user) {
            assertThrows(SQLException.class, () -> userDao.save(user));
        }

        private static Stream<Arguments> provideInvalidUsers() {
            return Stream.of(
                    of(User.builder()
                            .id(RandomUtils.getId())
                            .name("veryLoooooooooooooooooooooooooooooooooongNameMoreThan64Characters")
                            .birthday(LocalDateFormatter.format(RandomUtils.randomDate()))
                            .email(RandomUtils.randomDate())
                            .password(RandomUtils.randomPassword())
                            .role(Role.USER)
                            .gender(Gender.MALE)
                            .build()),
                    of(User.builder()
                            .id(RandomUtils.getId())
                            .name(RandomUtils.randomUserName())
                            .birthday(LocalDateFormatter.format(RandomUtils.randomDate()))
                            .email(null)
                            .password(RandomUtils.randomPassword())
                            .role(Role.USER)
                            .gender(Gender.MALE)
                            .build()),
                    of(User.builder()
                            .id(RandomUtils.getId())
                            .name(RandomUtils.randomUserName())
                            .birthday(LocalDateFormatter.format(RandomUtils.randomDate()))
                            .email(RandomUtils.randomDate())
                            .password(null)
                            .role(Role.USER)
                            .gender(Gender.MALE)
                            .build()),
                    of(User.builder()
                            .id(RandomUtils.getId())
                            .name(RandomUtils.randomUserName())
                            .birthday(LocalDateFormatter.format(RandomUtils.randomDate()))
                            .email(RandomUtils.randomDate())
                            .password(RandomUtils.randomPassword())
                            .role(null)
                            .gender(Gender.MALE)
                            .build())
            );
        }
    }

    @Nested
    @Tag("delete")
    @DisplayName("Functionality for deleting a user from the database")
    class DeleteTest {

        @Test
        void isShouldDeleteExistedUser() {
            userDao.save(validUser);

            var deleteResult = userDao.delete(validUser.getId());

            assertThat(deleteResult).isTrue();
        }

        @Test
        void itShouldNotDeleteNonexistentUser() {
            var expectedResultIfUserNonexistent = userDao.delete(validUser.getId());
            var expectedResultIfIdNegative = userDao.delete(-5);

            assertThat(expectedResultIfUserNonexistent).isFalse();
            assertThat(expectedResultIfIdNegative).isFalse();
        }
    }

    @Tag("update")
    @DisplayName("Functionality for updating a user in the database")
    @Test
    void itShouldUpdateExistingUserInDB() {
        userDao.save(validUser);

        var maybeFindById = userDao.findById(validUser.getId());
        maybeFindById.ifPresent(user ->
        {
            user.setName("nameAfterUpdating");
            user.setBirthday(LocalDateFormatter.format("2222-12-22"));
            user.setEmail("emailAfterUpdating@gmail.com");
            user.setPassword("passwordAfterUpdating");
            user.setRole(Role.ADMIN);
            user.setGender(Gender.FEMALE);
            userDao.update(user);
        });

        //Можно ли здесь использовать get() без проверки isPresent()?
        var maybeFindByIdAfterUpdating = userDao.findById(maybeFindById.get().getId());
        maybeFindByIdAfterUpdating.ifPresent(user -> assertThat(user.getEmail()).isEqualTo("emailAfterUpdating@gmail.com"));
        maybeFindByIdAfterUpdating.ifPresent(user ->
                assertAll(
                        () -> assertThat(user.getName()).isEqualTo("nameAfterUpdating"),
                        () -> assertThat(user.getBirthday()).isEqualTo(LocalDateFormatter.format("2222-12-22")),
                        () -> assertThat(user.getEmail()).isEqualTo("emailAfterUpdating@gmail.com"),
                        () -> assertThat(user.getPassword()).isEqualTo("passwordAfterUpdating"),
                        () -> assertThat(user.getRole()).isEqualTo(Role.ADMIN),
                        () -> assertThat(user.getGender()).isEqualTo(Gender.FEMALE)
                ));
    }
}