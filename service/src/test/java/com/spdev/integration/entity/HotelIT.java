package com.spdev.integration.entity;

import com.spdev.entity.Hotel;
import com.spdev.entity.enums.Status;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HotelIT {

    private static final Integer EXISTING_HOTEL_1 = 1;
    private static final Integer NONEXISTENT_HOTEL_ID = 999_999;

    @Test
    void shouldSaveNewValidHotelInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();

            validHotel.setOwner(validUser);
            session.save(validUser);
            var actualHotel = session.save(validHotel);

            assertThat(actualHotel).isNotNull();
            assertThat(actualHotel).isEqualTo(EXISTING_HOTEL_1);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveIfHotelInvalid() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidHotel = TestEntityUtil.getInvalidHotel();

            assertThrows(PropertyValueException.class, () -> session.save(invalidHotel));
        }
    }

    @Test
    void shouldReturnNullIfHotelIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualHotel = session.get(Hotel.class, NONEXISTENT_HOTEL_ID);

            assertThat(actualHotel).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingHotel() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();

            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            var expectedHotel = session.get(Hotel.class, EXISTING_HOTEL_1);
            expectedHotel.setName("HotelNameAfterUpdate");
            expectedHotel.setHotelStatus(Status.BLOCKED);
            session.update(expectedHotel);
            session.flush();
            session.clear();
            var actualHotel = session.get(Hotel.class, EXISTING_HOTEL_1);

            assertThat(actualHotel.getName()).isEqualTo("HotelNameAfterUpdate");
            assertThat(actualHotel.getHotelStatus()).isEqualTo(Status.BLOCKED);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfHotelNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new Hotel()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingHotel() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            session.save(validUser);
            validHotel.setOwner(validUser);
            var savedHotel = session.save(validHotel);
            assertThat(savedHotel).isNotNull();

            session.delete(validHotel);
            session.flush();
            session.clear();
            var actualHotel = session.get(Hotel.class, EXISTING_HOTEL_1);

            assertThat(actualHotel).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteHotelIfDeleteUser() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();

            validUser.addHotel(validHotel);
            session.save(validUser);
            session.delete(validUser);
            var actualHotel = session.get(Hotel.class, EXISTING_HOTEL_1);

            assertThat(actualHotel).isNull();
            session.getTransaction().commit();
        }
    }
}