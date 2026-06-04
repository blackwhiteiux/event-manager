-- liquibase formatted sql

-- changeset author:1
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
login VARCHAR(255) NOT NULL UNIQUE,
password_hash VARCHAR(255) NOT NULL,
role VARCHAR(50) NOT NULL
);

