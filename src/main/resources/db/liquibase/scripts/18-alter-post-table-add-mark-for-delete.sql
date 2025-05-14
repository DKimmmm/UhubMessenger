-- liquibase formatted sql

-- changeset danil-kim:24
alter table "public".posts
add column remove_mark BOOLEAN DEFAULT false NOT NULL;
