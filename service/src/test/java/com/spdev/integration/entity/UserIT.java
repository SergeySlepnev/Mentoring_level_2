package com.spdev.integration.entity;

import com.spdev.entity.User;
import com.spdev.entity.enums.Status;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserIT {

    private static final Integer EXISTING_USER_1 = 1;
    private static final Integer NONEXISTENT_USER_ID = 999_999;

    @Test
    void shouldSaveNewValidUserInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();

            var actualUser = session.save(validUser);

            assertThat(actualUser).isNotNull();
            assertThat(actualUser).isEqualTo(EXISTING_USER_1);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveIfUserInvalid() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidUser = TestEntityUtil.getInvalidUser();

            assertThrows(PropertyValueException.class, () -> session.save(invalidUser));
        }
    }

    @Test
    void shouldGetNullIfUserIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualUser = session.get(User.class, NONEXISTENT_USER_ID);

            assertThat(actualUser).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingUser() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            session.save(validUser);
            var expectedUser = session.get(User.class, EXISTING_USER_1);

            expectedUser.setEmail("After_update@gmail.com");
            expectedUser.setPassword("After_update_password");
            expectedUser.setUserStatus(Status.BLOCKED);
            session.update(expectedUser);
            session.flush();
            session.clear();
            var actualUser = session.get(User.class, EXISTING_USER_1);

            assertThat(actualUser.getEmail()).isEqualTo("After_update@gmail.com");
            assertThat(actualUser.getPassword()).isEqualTo("After_update_password");
            assertThat(actualUser.getUserStatus()).isEqualTo(Status.BLOCKED);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfUserNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new User()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingUser() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var savedUser = session.save(validUser);
            assertThat(savedUser).isNotNull();

            session.delete(validUser);
            session.flush();
            session.clear();
            var actualUser = session.get(User.class, EXISTING_USER_1);

            assertThat(actualUser).isNull();
            session.getTransaction().commit();
        }
    }
}