CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT      NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE EXTENSION IF NOT EXISTS btree_gist;


CREATE TABLE sleep_logs
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES users (id),
    day           DATE        NOT NULL,
    mood          VARCHAR(10) NOT NULL,
    start_at      TIMESTAMP   NOT NULL,
    end_at        TIMESTAMP   NOT NULL,
    total_minutes BIGINT      NOT NULL,
    UNIQUE (user_id, day, start_at, end_at),
    CONSTRAINT overlapping_sleeps
        EXCLUDE USING gist (
            user_id WITH =,
            day WITH =,
            tsrange(start_at, end_at, '[]') WITH &&
        )
) WITH (FILLFACTOR = 90);

