-- liquibase formatted sql

-- changeset author:1
INSERT INTO users (login, password_hash, age, role)
VALUES ('user', '$2a$10$ip1A9Kb9dG3hi8AOvGrITeMJbr8aY9mChxpDq9cXKodDokS.jkkXm', 21, 'USER')
ON CONFLICT (login) DO NOTHING;

-- changeset author:2
INSERT INTO users (login, password_hash, age, role)
VALUES ('admin', '$2a$10$Oil4sSJ85nEZY5b6nhF02e6miOtmQF548VdBh6cMUMhxZMlKjZRFW', 22, 'ADMIN')
ON CONFLICT (login) DO NOTHING;