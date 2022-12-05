--liquibase formatted sql

--changeset sslepnev:1
ALTER TABLE users
    ALTER COLUMN birth_date SET NOT NULL;