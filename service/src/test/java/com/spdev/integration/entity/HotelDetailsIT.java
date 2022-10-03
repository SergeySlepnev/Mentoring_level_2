package com.spdev.integration.entity;

import com.spdev.entity.HotelDetails;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HotelDetailsIT {

    private static final Integer EXISTING_HOTEL_DETAILS_1 = 1;
    private static final Integer NONEXISTENT_HOTEL_DETAILS_ID = 999_999;

    @Test
    void shouldSaveValidHotelDetailsInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validHotelDetails = TestEntityUtil.getValidHotelDetails();

            validHotelDetails.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validHotelDetails);
            var actualHotelDetails = session.get(HotelDetails.class, EXISTING_HOTEL_DETAILS_1);

            assertThat(actualHotelDetails).isNotNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveInvalidHotelDetails() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidHotelDetails = TestEntityUtil.getInvalidHotelDetails();

            assertThrows(PropertyValueException.class, () -> session.save(invalidHotelDetails));
        }
    }

    @Test
    void shouldGetNullIfHotelDetailsIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualHotelDetails = session.get(HotelDetails.class, NONEXISTENT_HOTEL_DETAILS_ID);

            assertThat(actualHotelDetails).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingHotelDetails() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validHotelDetails = TestEntityUtil.getValidHotelDetails();

            validHotelDetails.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validHotelDetails);
            var savedHotelDetails = session.get(HotelDetails.class, EXISTING_HOTEL_DETAILS_1);
            savedHotelDetails.setPhoneNumber("0-000-00-00-000");
            savedHotelDetails.setLocality("Moscow");
            savedHotelDetails.setNumbersOfFloors(150);
            session.update(savedHotelDetails);
            session.flush();
            session.clear();
            var actualHotelDetails = session.get(HotelDetails.class, EXISTING_HOTEL_DETAILS_1);

            assertThat(actualHotelDetails.getPhoneNumber()).isEqualTo("0-000-00-00-000");
            assertThat(actualHotelDetails.getLocality()).isEqualTo("Moscow");
            assertThat(actualHotelDetails.getNumbersOfFloors()).isEqualTo(150);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfHotelDetailsNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new HotelDetails()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingHotelDetails() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {

            session.beginTransaction();
            var validHotelDetails = TestEntityUtil.getValidHotelDetails();
            var savedHotelDetails = session.save(validHotelDetails);
            assertThat(savedHotelDetails).isNotNull();

            session.delete(validHotelDetails);
            session.flush();
            session.clear();
            var actualHotelDetails = session.get(HotelDetails.class, EXISTING_HOTEL_DETAILS_1);

            assertThat(actualHotelDetails).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteHotelDetailsIfDeleteHotel() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validHotelDetails = TestEntityUtil.getValidHotelDetails();

            validHotelDetails.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validHotelDetails);
            var savedHotelDetails = session.get(HotelDetails.class, EXISTING_HOTEL_DETAILS_1);
            assertThat(savedHotelDetails).isNotNull();
            session.delete(validHotel);
            session.flush();
            session.clear();
            var actualHotelDetails = session.get(HotelDetails.class, EXISTING_HOTEL_DETAILS_1);

            assertThat(actualHotelDetails).isNull();
            session.getTransaction().commit();
        }
    }
}