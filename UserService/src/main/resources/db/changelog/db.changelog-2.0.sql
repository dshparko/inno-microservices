--liquibase formatted sql

--changeset dshparko:1
INSERT INTO users (name, surname, email, birth_date)
VALUES ('Alice', 'Ivanova', 'alice@example.com', '1990-05-12'),
       ('Bob', 'Petrov', 'bob@example.com', '1985-11-23'),
       ('Charlie', 'Sidorov', 'charlie@example.com', '1992-07-08');

--changeset dshparko:2
INSERT INTO card_info (user_id, number, holder, expiration_date)
VALUES (1, '1234-5678-9012-3456', 'Alice Ivanova', '2026-12-31'),
       (2, '2345-6789-0123-4567', 'Bob Petrov', '2025-06-30'),
       (3, '3456-7890-1234-5678', 'Charlie Sidorov', '2027-03-15');