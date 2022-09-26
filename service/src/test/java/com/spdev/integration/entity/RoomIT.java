package com.spdev.integration.entity;

import com.spdev.entity.Room;
import com.spdev.entity.enums.IsAvailable;
import com.spdev.entity.enums.RoomType;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.spdev.integration.util.TestEntityUtil.FIRST_VALID_ROOM;
import static com.spdev.integration.util.TestEntityUtil.INVALID_ROOM;
import static com.spdev.integration.util.TestEntityUtil.SECOND_VALID_ROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Tag("Testing mapping functionality of Room.class")
class RoomIT extends IntegrationTestBase {

    @Test
    void shouldSaveNewValidRoomInDb() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in shouldSaveNewValidRoomInDb() was opened {}:", testTransaction);

            log.info("Test valid Room entity is in a transient state {}:", TestEntityUtil.SECOND_VALID_ROOM);
            var expectedId = session.save(TestEntityUtil.SECOND_VALID_ROOM);

            assertThat(expectedId).isNotNull();
            assertThat(TestEntityUtil.SECOND_VALID_ROOM.getId()).isEqualTo(expectedId);
            log.info("Test shouldSaveNewValidRoomInDb() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowSQLExceptionIfRoomInvalid() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var invalidRoom = INVALID_ROOM;
            log.info("Test invalid Room entity is in a transient state {}:", invalidRoom);

            assertThrows(ConstraintViolationException.class, () -> session.save(invalidRoom));
            log.info("Test shouldThrowSQLExceptionIfRoomInvalid is passed");

            // если здесь вызывать session.getTransaction().commit(), то тест падает.
            // Это из-за ConstraintViolationException?
        }
    }

    @Test
    void shouldGetExistingRoom() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedRoom = session.get(Room.class, FIRST_VALID_ROOM.getId());

            assertThat(expectedRoom).isNotNull();
            assertThat(FIRST_VALID_ROOM).isEqualTo(expectedRoom);
            log.info("Test shouldGetExistingRoom() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldGetNullIfRoomNotExisting() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedRoom = session.get(Room.class, 10);
            assertThat(expectedRoom).isNull();
            log.info("Test shouldGetNullIfRoomNotExisting() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingRoom() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedRoom = session.get(Room.class, FIRST_VALID_ROOM.getId());
            expectedRoom.setHotelId(1);
            expectedRoom.setRoomNo(1);
            expectedRoom.setType(RoomType.TRPL);
            expectedRoom.setSquare(25.1);
            expectedRoom.setIsAvailable(IsAvailable.NO);
            expectedRoom.setDescription("Very good!");
            session.update(expectedRoom);
            var actualRoom = session.get(Room.class, FIRST_VALID_ROOM.getId());

            assertThat(actualRoom).isNotNull();
            assertThat(actualRoom).isEqualTo(expectedRoom);
            log.info("Test shouldUpdateExistingRoom() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionWhileUpdateIfRoomNotExist() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            assertThrows(TransientObjectException.class, () -> session.update(new Room()));
            log.info("Test shouldTrowExceptionWhileUpdateIfRoomNotExist() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingRoom() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var firstExpectedRoom = session.save(SECOND_VALID_ROOM);
            assertThat(firstExpectedRoom).isNotNull();
            session.delete(SECOND_VALID_ROOM);
            var expectedRoom = session.get(Room.class, SECOND_VALID_ROOM.getId());

            assertThat(expectedRoom).isNull();
            log.info("Test shouldDeleteExistingRoom() is passed");
            session.getTransaction().commit();
        }
    }
}