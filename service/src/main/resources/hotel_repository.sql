CREATE DATABASE hotel_repository;

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
--     населённый пункт
    locality     VARCHAR(128)              NOT NULL,
--     район
    area         VARCHAR(128)              NOT NULL,
    class        VARCHAR(1)                NOT NULL,
    phone_number VARCHAR(32)               NOT NULL,
    description  VARCHAR(4096)             NOT NULL,
    is_available VARCHAR(16)               NOT NULL,
    hotel_status VARCHAR(64)               NOT NULL
);

CREATE TABLE room
(
    id            SERIAL PRIMARY KEY,
    hotel_id      INT REFERENCES hotel (id) NOT NULL,
    room_no       VARCHAR(4)                NOT NULL,
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
    rating      INT                       NOT NULL,
    description VARCHAR(4096)
);

-- Таблица для хранения ссылок не все фото- и видеофайлы приложения
-- (для отелей, номеров и отзывов), кроме аваторов.
CREATE TABLE photo_video
(
    id        BIGSERIAL PRIMARY KEY,
    hotel_id  INT REFERENCES hotel (id),
    room_id   INT REFERENCES room (id),
    review_id BIGINT REFERENCES review (id),
    link      VARCHAR(1024) NOT NULL
);