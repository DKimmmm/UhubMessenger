-- liquibase formatted sql

-- changeset danil-kim:23
alter table "public".posts
add column created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;
