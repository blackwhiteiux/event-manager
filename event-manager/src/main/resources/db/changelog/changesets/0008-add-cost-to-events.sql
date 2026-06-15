-- liquibase formatted sql

-- changeset author:1
ALTER TABLE events ADD COLUMN cost INTEGER NOT NULL DEFAULT 0;

-- changeset author:2
ALTER TABLE events ADD CONSTRAINT check_cost_positive CHECK (cost >= 0);