package com.spdev.integration.entity;

import com.spdev.entity.Room;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomIT {

    private static final Integer EXISTING_ROOM_1 = 1;
    private static final Integer NONEXISTENT_ROOM_ID = 999_999;

    @Test
    void shouldSaveValidRoomInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();

            validRoom.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            var actualRoom = session.get(Room.class, EXISTING_ROOM_1);

            assertThat(actualRoom).isNotNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveInvalidRoom() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var invalidRoom = TestEntityUtil.getInvalidRoom();

            invalidRoom.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);

            assertThrows(PropertyValueException.class, () -> session.save(invalidRoom));
        }
    }

    @Test
    void shouldGetNullIfRoomIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualRoom = session.get(Room.class, NONEXISTENT_ROOM_ID);

            assertThat(actualRoom).isNull();
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
            var validRoom = TestEntityUtil.getValidRoom();

            validRoom.setHotel(validHotel);
            validHotel.setOwner(validUser);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            var savedRoom = session.get(Room.class, EXISTING_ROOM_1);
            savedRoom.setSquare(120.55);
            savedRoom.setCost(BigDecimal.valueOf(580.25));
            savedRoom.setFloor(38);
            session.update(savedRoom);
            session.flush();
            session.clear();
            var actualRoom = session.get(Room.class, EXISTING_ROOM_1);

            assertThat(actualRoom.getSquare()).isEqualTo(120.55);
            assertThat(actualRoom.getCost()).isEqualTo(BigDecimal.valueOf(580.25));
            assertThat(actualRoom.getFloor()).isEqualTo(38);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfRoomNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new Room()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingRoom() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            session.save(validUser);
            session.save(validHotel);
            var savedRoom = session.save(validRoom);
            assertThat(savedRoom).isNotNull();
            session.delete(validRoom);
            session.flush();
            session.clear();
            var actualRoom = session.get(Room.class, EXISTING_ROOM_1);

            assertThat(actualRoom).isNull();
            session.getTransaction().commit();
        }
    }

    //???? ????????????????
    @Test
    void shouldDeleteRoomIfDeleteHotel() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validRoom = TestEntityUtil.getValidRoom();

            validHotel.setOwner(validUser);
            validRoom.setHotel(validHotel);
            session.save(validUser);
            session.save(validHotel);
            session.save(validRoom);
            var savedRoom = session.get(Room.class, EXISTING_ROOM_1);
            assertThat(savedRoom).isNotNull();
            session.delete(validHotel);
            session.flush();
            session.clear();
            var actualRoom = session.get(Room.class, EXISTING_ROOM_1);

            assertThat(actualRoom).isNull();
            session.getTransaction().commit();
        }
    }
}