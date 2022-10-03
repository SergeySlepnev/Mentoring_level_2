package com.spdev.integration.entity;

import com.spdev.entity.HotelContent;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HotelContentIT {

    private static final Integer EXISTING_HOTEL_CONTENT_1 = 1;
    private static final Integer NONEXISTENT_HOTEL_CONTENT_ID = 999_999;

    @Test
    void shouldSaveValidHotelContentInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {

            session.beginTransaction();
            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validHotelContent = TestEntityUtil.getValidHotelContent();

            validHotelContent.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validHotelContent);
            var actualHotelContent = session.get(HotelContent.class, EXISTING_HOTEL_CONTENT_1);

            assertThat(actualHotelContent).isNotNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveInvalidHotelContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidHotelContent = TestEntityUtil.getInvalidHotelContent();

            assertThrows(PropertyValueException.class, () -> session.save(invalidHotelContent));
        }
    }

    @Test
    void shouldGetNullIfHotelContentIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualHotelContent = session.get(HotelContent.class, NONEXISTENT_HOTEL_CONTENT_ID);

            assertThat(actualHotelContent).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingHotelContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validHotelContent = TestEntityUtil.getValidHotelContent();

            validHotelContent.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validHotelContent);
            var savedHotelContent = session.get(HotelContent.class, EXISTING_HOTEL_CONTENT_1);
            savedHotelContent.setLink("LinkAfterUpdate.jpg");
            session.update(savedHotelContent);
            session.flush();
            session.clear();
            var actualHotelContent = session.get(HotelContent.class, EXISTING_HOTEL_CONTENT_1);

            assertThat(actualHotelContent.getLink()).isEqualTo("LinkAfterUpdate.jpg");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfHotelContentNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new HotelContent()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingHotelContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {

            session.beginTransaction();
            var validHotelContent = TestEntityUtil.getValidHotelContent();
            var savedHotelContent = session.save(validHotelContent);
            assertThat(savedHotelContent).isNotNull();

            session.delete(validHotelContent);
            session.flush();
            session.clear();
            var actualHotelContent = session.get(HotelContent.class, EXISTING_HOTEL_CONTENT_1);

            assertThat(actualHotelContent).isNull();
            session.getTransaction().commit();
        }
    }

    // Не проходит. Подробности: Key (id)=(1) is still referenced from table "hotel_content".
    // В остальных классах этот тест тоже падает. Не успел разобраться.
    @Test
    void shouldDeleteHotelContentIfDeleteHotel() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validHotelContent = TestEntityUtil.getValidHotelContent();

            validHotel.setOwner(validUser);
            validHotelContent.setHotel(validHotel);
            session.save(validUser);
            session.save(validHotel);
            session.save(validHotelContent);
            var savedHotelContent = session.get(HotelContent.class, EXISTING_HOTEL_CONTENT_1);

            assertThat(savedHotelContent).isNotNull();
            session.delete(validHotel);
            session.flush();
            session.clear();
            var actualHotelContent = session.get(HotelContent.class, EXISTING_HOTEL_CONTENT_1);

            assertThat(actualHotelContent).isNull();
            session.getTransaction().commit();
        }
    }
}