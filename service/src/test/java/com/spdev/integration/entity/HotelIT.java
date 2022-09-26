package com.spdev.integration.entity;

import com.spdev.entity.Hotel;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.spdev.integration.util.TestEntityUtil.FIRST_VALID_HOTEL;
import static com.spdev.integration.util.TestEntityUtil.INVALID_HOTEL;
import static com.spdev.integration.util.TestEntityUtil.SECOND_VALID_HOTEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Tag("Testing mapping functionality of Hotel.class")
class HotelIT extends IntegrationTestBase {

    @Test
    void shouldSaveNewValidHotelInDb() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in shouldSaveNewValidHotelInDb() was opened {}:", testTransaction);

            log.info("Test valid Hotel entity is in a transient state {}:", TestEntityUtil.SECOND_VALID_HOTEL);
            var expectedId = session.save(TestEntityUtil.SECOND_VALID_HOTEL);

            assertThat(expectedId).isNotNull();
            assertThat(TestEntityUtil.SECOND_VALID_HOTEL.getId()).isEqualTo(expectedId);
            log.info("Test shouldSaveNewValidHotelInDb() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowSQLExceptionIfHotelInvalid() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var invalidHotel = INVALID_HOTEL;
            log.info("Test invalid Hotel entity is in a transient state {}:", invalidHotel);

            assertThrows(ConstraintViolationException.class, () -> session.save(invalidHotel));
            log.info("Test shouldThrowSQLExceptionIfHotelInvalid is passed");
        }
    }

    @Test
    void shouldGetExistingHotel() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedHotel = session.get(Hotel.class, FIRST_VALID_HOTEL.getId());

            assertThat(expectedHotel).isNotNull();
            assertThat(FIRST_VALID_HOTEL).isEqualTo(expectedHotel);
            log.info("Test shouldGetExistingHotel() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldGetNullIfHotelNotExisting() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedHotel = session.get(Hotel.class, 10);

            assertThat(expectedHotel).isNull();
            log.info("Test shouldGetNullIfHotelNotExisting() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingHotel() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedHotel = session.get(Hotel.class, FIRST_VALID_HOTEL.getId());
            expectedHotel.setOwnerId(1);
            expectedHotel.setName("NewName");
            session.update(expectedHotel);
            var actualHotel = session.get(Hotel.class, FIRST_VALID_HOTEL.getId());

            assertThat(actualHotel).isNotNull();
            assertThat(actualHotel).isEqualTo(expectedHotel);
            log.info("Test shouldUpdateExistingHotel() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionWhileUpdateIfHotelNotExist() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            assertThrows(TransientObjectException.class, () -> session.update(new Hotel()));
            log.info("Test shouldTrowExceptionWhileUpdateIfHotelNotExist() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingHotel() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var firstExpectedHotel = session.save(SECOND_VALID_HOTEL);
            assertThat(firstExpectedHotel).isNotNull();
            session.delete(SECOND_VALID_HOTEL);
            var expectedHotel = session.get(Hotel.class, SECOND_VALID_HOTEL.getId());

            assertThat(expectedHotel).isNull();
            log.info("Test shouldDeleteExistingHotel() is passed");
            session.getTransaction().commit();
        }
    }
}