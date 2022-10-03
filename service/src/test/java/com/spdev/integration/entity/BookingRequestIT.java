package com.spdev.integration.entity;

import com.spdev.entity.BookingRequest;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingRequestIT {

    private static final Integer EXISTING_REQUEST_1 = 1;
    private static final Integer NONEXISTENT_REQUEST_ID = 999_999;

    @Test
    void shouldSaveValidBookingRequestInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validBookingRequest = TestEntityUtil.validBookingRequest();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validBookingRequest.setUser(validUser);
            validBookingRequest.setHotel(validHotel);
            validBookingRequest.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validBookingRequest);
            var actualBookingRequest = session.get(BookingRequest.class, EXISTING_REQUEST_1);

            assertThat(actualBookingRequest).isNotNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveIfBookingRequestInvalid() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidBookingRequest = TestEntityUtil.getInvalidBookingRequest();

            assertThrows(PropertyValueException.class, () -> session.save(invalidBookingRequest));
        }
    }

    @Test
    void shouldGetNullIfBookingRequestIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualBookingRequest = session.get(BookingRequest.class, NONEXISTENT_REQUEST_ID);

            assertThat(actualBookingRequest).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingBookingRequest() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validBookingRequest = TestEntityUtil.validBookingRequest();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validBookingRequest.setUser(validUser);
            validBookingRequest.setHotel(validHotel);
            validBookingRequest.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validBookingRequest);
            var expectedBookingRequest = session.get(BookingRequest.class, EXISTING_REQUEST_1);
            expectedBookingRequest.setCreatedAt(LocalDateTime.of(2022, 3, 10, 15, 10));
            expectedBookingRequest.setCheckIn(LocalDate.of(2022, 3, 15));
            expectedBookingRequest.setCheckOut(LocalDate.of(2022, 3, 18));
            session.update(expectedBookingRequest);
            session.flush();
            session.clear();
            var actualBookingRequest = session.get(BookingRequest.class, EXISTING_REQUEST_1);

            assertThat(actualBookingRequest.getCreatedAt()).isEqualTo(LocalDateTime.of(2022, 3, 10, 15, 10));
            assertThat(actualBookingRequest.getCheckIn()).isEqualTo(LocalDate.of(2022, 3, 15));
            assertThat(actualBookingRequest.getCheckOut()).isEqualTo(LocalDate.of(2022, 3, 18));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfBookingRequestNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new BookingRequest()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingBookingRequest() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validBookingRequest = TestEntityUtil.validBookingRequest();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validBookingRequest.setUser(validUser);
            validBookingRequest.setHotel(validHotel);
            validBookingRequest.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validBookingRequest);
            var savedBookingRequest = session.get(BookingRequest.class, EXISTING_REQUEST_1);

            assertThat(savedBookingRequest).isNotNull();
            session.delete(validBookingRequest);
            session.flush();
            session.clear();
            var actualBookingRequest = session.get(BookingRequest.class, EXISTING_REQUEST_1);

            assertThat(actualBookingRequest).isNull();
            session.getTransaction().commit();
        }
    }
}