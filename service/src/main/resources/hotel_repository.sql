CREATE DATABASE hotel_repository;

CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    role       VARCHAR(64)  NOT NULL,
    email      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(64)  NOT NULL,
    phone      VARCHAR(32)  NOT NULL UNIQUE,
    firstname  VARCHAR(128) NOT NULL,
    lastname   VARCHAR(128) NOT NULL,
    birth_date DATE         NOT NULL,
    photo_link VARCHAR(1024),
    status     VARCHAR(32)  NOT NULL
);

CREATE TABLE hotel
(
    id        SERIAL PRIMARY KEY,
    owner_id  INT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    name      VARCHAR(128)                                NOT NULL,
    available BOOLEAN                                     NOT NULL,
    status    VARCHAR(64)                                 NOT NULL
);

CREATE TABLE hotel_details
(
    id           SERIAL PRIMARY KEY,
    hotel_id     INT          NOT NULL UNIQUE REFERENCES hotel (id),
    phone_number VARCHAR(32)  NOT NULL UNIQUE,
    country      VARCHAR(128) NOT NULL,
    locality     VARCHAR(128) NOT NULL,
    area         VARCHAR(128) NOT NULL,
    street       VARCHAR(128) NOT NULL,
    floor_count  INT          NOT NULL,
    star         VARCHAR(16)  NOT NULL,
    description  TEXT         NOT NULL
);

CREATE TABLE room
(
    id          SERIAL PRIMARY KEY,
    hotel_id    INT REFERENCES hotel (id) ON DELETE CASCADE NOT NULL,
    room_no     INT                                         NOT NULL,
    type        VARCHAR(64)                                 NOT NULL,
    square      FLOAT                                       NOT NULL,
    bed_count   INT                                         NOT NULL,
    cost        NUMERIC(10, 2)                              NOT NULL,
    available   BOOLEAN                                     NOT NULL,
    floor       INT                                         NOT NULL,
    description TEXT                                        NOT NULL
);

CREATE TABLE booking_request
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP                 NOT NULL,
    hotel_id   INT REFERENCES hotel (id) NOT NULL,
    room_id    INT REFERENCES room (id)  NOT NULL,
    user_id    INT REFERENCES users (id) NOT NULL,
    check_in   DATE                      NOT NULL,
    check_out  DATE                      NOT NULL,
    status     VARCHAR(32)               NOT NULL
);

CREATE TABLE review
(
    id          BIGSERIAL PRIMARY KEY,
    hotel_id    INT REFERENCES hotel (id) ON DELETE CASCADE NOT NULL,
    user_id     INT REFERENCES users (id)                   NOT NULL,
    created_at  TIMESTAMP                                   NOT NULL,
    rating      INT                                         NOT NULL,
    description TEXT
);

CREATE TABLE hotel_content
(
    id       SERIAL PRIMARY KEY,
    hotel_id INT REFERENCES hotel (id) ON DELETE CASCADE NOT NULL,
    link     VARCHAR(1024)                               NOT NULL UNIQUE,
    type     VARCHAR(32)                                 NOT NULL
);

CREATE TABLE room_content
(
    id      SERIAL PRIMARY KEY,
    room_id INT REFERENCES room (id) ON DELETE CASCADE NOT NULL,
    link    VARCHAR(1024)                              NOT NULL UNIQUE,
    type    VARCHAR(32)                                NOT NULL
);

CREATE TABLE review_content
(
    id        SERIAL PRIMARY KEY,
    review_id INT REFERENCES review (id) ON DELETE CASCADE NOT NULL,
    link      VARCHAR(1024)                                NOT NULL UNIQUE,
    type      VARCHAR(32)                                  NOT NULL,
    UNIQUE (review_id, link)
);