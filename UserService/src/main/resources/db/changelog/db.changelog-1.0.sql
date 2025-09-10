--liquibase formatted sql

--changeset dshparko:1
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(64),
    surname    VARCHAR(64),
    email      VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE
);

--changeset dshparko:2
CREATE TABLE IF NOT EXISTS card_info
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    number          VARCHAR(64) NOT NULL UNIQUE,
    holder          VARCHAR(128),
    expiration_date DATE
);

--changeset dshparko:3
CREATE INDEX idx_card_info_user_id ON card_info(user_id);