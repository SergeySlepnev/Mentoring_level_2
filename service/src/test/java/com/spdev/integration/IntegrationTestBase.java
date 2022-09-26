package com.spdev.integration;

import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
public abstract class IntegrationTestBase {

    private static final String CLEAN_SQL = """
            DROP TABLE IF EXISTS booking_request;
            DROP TABLE IF EXISTS photo_video;
            DROP TABLE IF EXISTS room;
            DROP TABLE IF EXISTS review;
            DROP TABLE IF EXISTS hotel;
            DROP TABLE IF EXISTS users;
            """;

    private static final String CRETE_SQL = """
            CREATE TABLE users
            (
                id          SERIAL PRIMARY KEY,
                role        VARCHAR(64)         NOT NULL,
                email       VARCHAR(128) UNIQUE NOT NULL,
                password    VARCHAR(64)         NOT NULL,
                firstname   VARCHAR(128)        NOT NULL,
                lastname    VARCHAR(128)        NOT NULL,
                phone       VARCHAR(32) UNIQUE,
                birth_date  DATE                NOT NULL,
                photo_link  VARCHAR(1024),
                user_status VARCHAR(32)         NOT NULL
            );
            CREATE TABLE hotel
            (
                id           SERIAL PRIMARY KEY,
                owner_id     INT REFERENCES users (id) NOT NULL,
                name         VARCHAR(128)              NOT NULL,
                locality     VARCHAR(128)              NOT NULL,
                area         VARCHAR(128)              NOT NULL,
                category     VARCHAR(32)               NOT NULL,
                phone_number VARCHAR(32)               NOT NULL,
                description  VARCHAR(4096)             NOT NULL,
                is_available VARCHAR(16)               NOT NULL,
                hotel_status VARCHAR(64)               NOT NULL
            );
            CREATE TABLE room
            (
                id            SERIAL PRIMARY KEY,
                hotel_id      INT REFERENCES hotel (id) NOT NULL,
                room_no       INT                NOT NULL,
                type          VARCHAR(64)               NOT NULL,
                square        FLOAT                     NOT NULL,
                number_of_bed INT                       NOT NULL,
                cost          NUMERIC(10, 2)            NOT NULL,
                is_available  VARCHAR(64)               NOT NULL,
                description   VARCHAR(4096)             NOT NULL
            );
            CREATE TABLE booking_request
            (
                id             BIGSERIAL PRIMARY KEY,
                hotel_id       INT REFERENCES hotel (id) NOT NULL,
                room_id        INT REFERENCES room (id)  NOT NULL,
                user_id        INT REFERENCES users (id) NOT NULL,
                check_in       DATE                      NOT NULL,
                check_out      DATE                      NOT NULL,
                request_status VARCHAR(32)               NOT NULL
            );
            CREATE TABLE review
            (
                id          BIGSERIAL PRIMARY KEY,
                hotel_id    INT REFERENCES hotel (id) NOT NULL,
                date        TIMESTAMP                 NOT NULL,
                rating      VARCHAR(32)               NOT NULL,
                description VARCHAR(4096)
            );
            CREATE TABLE photo_video
            (
                id        BIGSERIAL PRIMARY KEY,
                hotel_id  INT REFERENCES hotel (id),
                room_id   INT REFERENCES room (id),
                review_id BIGINT REFERENCES review (id),
                link      VARCHAR(1024) NOT NULL
            );
            """;

    @BeforeEach
    void setUp() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in setUp() was opened {}:", testTransaction);
            session.createSQLQuery(CLEAN_SQL).executeUpdate();
            session.createSQLQuery(CRETE_SQL).executeUpdate();
            session.save(TestEntityUtil.FIRST_VALID_USER);
            session.save(TestEntityUtil.FIRST_VALID_HOTEL);
            session.save(TestEntityUtil.FIRST_VALID_ROOM);
            session.save(TestEntityUtil.FIRST_VALID_BOOKING_REQUEST);
            session.save(TestEntityUtil.FIRST_VALID_REVIEW);
            session.save(TestEntityUtil.FIRST_VALID_PHOTO_VIDEO);
            session.getTransaction().commit();
        }
    }
}