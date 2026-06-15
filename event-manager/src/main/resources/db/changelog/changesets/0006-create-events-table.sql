-- liquibase formatted sql

-- changeset author:1
CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_at TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes >= 30),
    max_places INTEGER NOT NULL CHECK (max_places > 0),
    occupied_places INTEGER NOT NULL DEFAULT 0 CHECK (occupied_places >= 0),
    status VARCHAR(50) NOT NULL DEFAULT 'WAIT_START',
    location_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_events_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE RESTRICT,
    CONSTRAINT fk_events_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
    );