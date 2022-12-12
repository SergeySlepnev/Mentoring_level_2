--liquibase formatted sql

--changeset sslepnev:1
ALTER TABLE users
    ALTER COLUMN birth_date SET NOT NULL;

--changeset sslepnev:2
ALTER TABLE hotel_content
    DROP CONSTRAINT hotel_content_link_key;