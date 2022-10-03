package com.spdev.integration.entity;

import com.spdev.entity.RoomContent;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomContentIT {

    private static final Integer EXISTING_ROOM_CONTENT_1 = 1;
    private static final Integer NONEXISTENT_ROOM_CONTENT_ID = 999_999;

    @Test
    void shouldSaveValidRoomContentInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validRoomContent = TestEntityUtil.getValidRoomContent();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validRoomContent.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validRoomContent);
            var actualRoomContent = session.get(RoomContent.class, EXISTING_ROOM_CONTENT_1);

            assertThat(actualRoomContent).isNotNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveInvalidRoomContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidRoomContent = TestEntityUtil.getInvalidRoomContent();

            assertThrows(PropertyValueException.class, () -> session.save(invalidRoomContent));
        }
    }

    @Test
    void shouldGetNullIfRoomContentIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualRoomContent = session.get(RoomContent.class, NONEXISTENT_ROOM_CONTENT_ID);

            assertThat(actualRoomContent).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingRoomContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validRoomContent = TestEntityUtil.getValidRoomContent();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validRoomContent.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validRoomContent);
            var savedRoomContent = session.get(RoomContent.class, EXISTING_ROOM_CONTENT_1);
            savedRoomContent.setLink("LinkAfterUpdate.jpg");
            session.update(savedRoomContent);
            session.flush();
            session.clear();
            var actualRoomContent = session.get(RoomContent.class, EXISTING_ROOM_CONTENT_1);

            assertThat(actualRoomContent.getLink()).isEqualTo("LinkAfterUpdate.jpg");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfRoomContentNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new RoomContent()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingRoomContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validRoomContent = TestEntityUtil.getValidRoomContent();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validRoomContent.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validRoomContent);
            var savedRoomContent = session.save(validRoomContent);
            assertThat(savedRoomContent).isNotNull();
            session.delete(validRoomContent);
            session.flush();
            session.clear();
            var actualRoomContent = session.get(RoomContent.class, EXISTING_ROOM_CONTENT_1);

            assertThat(actualRoomContent).isNull();
            session.getTransaction().commit();
        }
    }

    // Не проходит.
    @Test
    void shouldDeleteRoomContentIfDeleteRoom() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();
            var validRoomContent = TestEntityUtil.getValidRoomContent();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            validRoomContent.setRoom(validRoom);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            session.save(validRoomContent);
            var savedRoomContent = session.get(RoomContent.class, EXISTING_ROOM_CONTENT_1);
            assertThat(savedRoomContent).isNotNull();

            session.delete(validRoom);
            session.flush();
            session.clear();
            var actualRoomContent = session.get(RoomContent.class, EXISTING_ROOM_CONTENT_1);
            assertThat(actualRoomContent).isNull();
            session.getTransaction().commit();
        }
    }
}