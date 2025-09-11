-- liquibase formatted sql

-- changeset danil-kim:17
ALTER TABLE "public"."groups"
ADD CONSTRAINT unique_title UNIQUE (title);