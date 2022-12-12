--liquibase formatted sql

--changeset sslepnev:1
ALTER TABLE users
    ALTER COLUMN registered_at TYPE TIMESTAMP(0);

--changeset sslepnev:2
ALTER TABLE booking_request
    ALTER COLUMN created_at TYPE TIMESTAMP(0);