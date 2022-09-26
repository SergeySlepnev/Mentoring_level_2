package com.spdev.integration.entity;

import com.spdev.entity.BookingRequest;
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

import static com.spdev.integration.util.TestEntityUtil.FIRST_VALID_BOOKING_REQUEST;
import static com.spdev.integration.util.TestEntityUtil.INVALID_BOOKING_REQUEST;
import static com.spdev.integration.util.TestEntityUtil.SECOND_VALID_BOOKING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Tag("Testing mapping functionality of BookingRequest.class")
class BookingRequestIT extends IntegrationTestBase {

    @Test
    void shouldSaveNewValidBookingRequestInDb() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in shouldSaveNewValidBookingRequestInDb() was opened {}:", testTransaction);

            log.info("Test valid BookingRequest entity is in a transient state {}:", TestEntityUtil.SECOND_VALID_BOOKING_REQUEST);
            var expectedId = session.save(TestEntityUtil.SECOND_VALID_BOOKING_REQUEST);

            assertThat(expectedId).isNotNull();
            assertThat(TestEntityUtil.SECOND_VALID_BOOKING_REQUEST.getId()).isEqualTo(expectedId);
            log.info("Test shouldSaveNewValidBookingRequestInDb() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowSQLExceptionIfBookingRequestInvalid() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var invalidBookingRequest = INVALID_BOOKING_REQUEST;
            log.info("Test invalid BookingRequest entity is in a transient state {}:", invalidBookingRequest);

            assertThrows(ConstraintViolationException.class, () -> session.save(invalidBookingRequest));
            log.info("Test shouldThrowSQLExceptionIfBookingRequestInvalid is passed");
        }
    }

    @Test
    void shouldGetExistingBookingRequest() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedBookingRequest = session.get(BookingRequest.class, FIRST_VALID_BOOKING_REQUEST.getId());

            assertThat(expectedBookingRequest).isNotNull();
            assertThat(FIRST_VALID_BOOKING_REQUEST).isEqualTo(expectedBookingRequest);
            log.info("Test shouldGetExistingBookingRequest() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldGetNullIfBookingRequestNotExisting() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedBookingRequest = session.get(BookingRequest.class, 10);

            assertThat(expectedBookingRequest).isNull();
            log.info("Test shouldGetNullIfBookingRequestNotExisting() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingBookingRequest() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedBookingRequest = session.get(BookingRequest.class, FIRST_VALID_BOOKING_REQUEST.getId());
            expectedBookingRequest.setHotelId(1);
            expectedBookingRequest.setRoomId(1);
            expectedBookingRequest.setUserId(1);
            expectedBookingRequest.setCheckIn(LocalDate.of(2022, 10, 15));
            expectedBookingRequest.setCheckOut(LocalDate.of(2022, 10, 27));
            expectedBookingRequest.setRequestStatus(Status.BLOCKED);
            session.update(expectedBookingRequest);
            var actualBookingRequest = session.get(BookingRequest.class, FIRST_VALID_BOOKING_REQUEST.getId());

            assertThat(actualBookingRequest).isNotNull();
            assertThat(actualBookingRequest).isEqualTo(expectedBookingRequest);
            log.info("Test shouldUpdateExistingBookingRequest() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionWhileUpdateIfBookingRequestNotExist() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            assertThrows(TransientObjectException.class, () -> session.update(new BookingRequest()));
            log.info("Test shouldTrowExceptionWhileUpdateIfBookingRequestNotExist() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingBookingRequest() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var firstExpectedBookingRequest = session.save(SECOND_VALID_BOOKING_REQUEST);
            assertThat(firstExpectedBookingRequest).isNotNull();
            session.delete(SECOND_VALID_BOOKING_REQUEST);
            var expectedBookingRequest = session.get(BookingRequest.class, SECOND_VALID_BOOKING_REQUEST.getId());

            assertThat(expectedBookingRequest).isNull();
            log.info("Test shouldDeleteExistingBookingRequest() is passed");
            session.getTransaction().commit();
        }
    }
}