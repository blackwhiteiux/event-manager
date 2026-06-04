-- liquibase formatted sql

-- changeset author:1

CREATE TABLE IF NOT EXISTS locations (
id BIGSERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
address VARCHAR(255) NOT NULL,
capacity INTEGER NOT NULL,
description TEXT
);