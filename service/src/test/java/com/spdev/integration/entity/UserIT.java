package com.spdev.integration.entity;

import com.spdev.entity.PersonalInfo;
import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.spdev.integration.util.TestEntityUtil.FIRST_VALID_USER;
import static com.spdev.integration.util.TestEntityUtil.INVALID_USER;
import static com.spdev.integration.util.TestEntityUtil.SECOND_VALID_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Tag("Testing mapping functionality of User.class")
class UserIT extends IntegrationTestBase {

    @Test
    void shouldSaveNewValidUserInDb() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in shouldSaveNewValidUserInDb() was opened {}:", testTransaction);

            log.info("Test valid user entity is in a transient state {}:", TestEntityUtil.SECOND_VALID_USER);
            var expectedId = session.save(TestEntityUtil.SECOND_VALID_USER);

            assertThat(expectedId).isNotNull();
            assertThat(TestEntityUtil.SECOND_VALID_USER.getId()).isEqualTo(expectedId);
            log.info("Test shouldSaveNewValidUserInDb() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowSQLExceptionIfUserInvalid() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var invalidUser = INVALID_USER;
            log.info("Test invalid user entity is in a transient state {}:", invalidUser);

            assertThrows(ConstraintViolationException.class, () -> session.save(invalidUser));
            log.info("Test shouldThrowSQLExceptionIfUserInvalid is passed");
            // если здесь вызывать session.getTransaction().commit(), то тест падает.
            // Это из-за ConstraintViolationException?
        }
    }

    @Test
    void shouldGetExistingUser() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedUser = session.get(User.class, FIRST_VALID_USER.getId());

            assertThat(expectedUser).isNotNull();
            assertThat(FIRST_VALID_USER).isEqualTo(expectedUser);
            log.info("Test shouldGetExistingUser() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldGetNullIfUserNotExisting() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedUser = session.get(User.class, 10);

            assertThat(expectedUser).isNull();
            log.info("Test shouldGetNullIfUserNotExisting() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingUser() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedUser = session.get(User.class, FIRST_VALID_USER.getId());
            expectedUser.setRole(Role.ADMIN);
            expectedUser.setEmail("EmailAfterUpdate@gmail.com");
            expectedUser.setPassword("PasswordAfterUpdate");
            expectedUser.setPersonalInfo(PersonalInfo.builder()
                    .firstname("FirstNameAfterUpdate")
                    .lastname("LastNameAfterUpdate")
                    .birthDate(LocalDate.of(2000, 10, 20))
                    .build());
            expectedUser.setPhone("PhoneAfterUpdate");
            expectedUser.setPhotoLink("PhotoAfterUpdate.jpg");
            expectedUser.setUserStatus(Status.BLOCKED);
            session.update(expectedUser);
            var actualUser = session.get(User.class, FIRST_VALID_USER.getId());

            assertThat(actualUser).isNotNull();
            assertThat(actualUser).isEqualTo(expectedUser);
            log.info("Test shouldUpdateExistingUser() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionWhileUpdateIfUserNotExist() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            assertThrows(TransientObjectException.class, () -> session.update(new User()));
            log.info("Test shouldTrowExceptionWhileUpdateIfUserNotExist() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingUser() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var firstExpectedUser = session.save(SECOND_VALID_USER);
            assertThat(firstExpectedUser).isNotNull();
            session.delete(SECOND_VALID_USER);
            var expectedUser = session.get(User.class, SECOND_VALID_USER.getId());

            assertThat(expectedUser).isNull();
            log.info("Test shouldDeleteExistingUser() is passed");
            session.getTransaction().commit();
        }
    }
}