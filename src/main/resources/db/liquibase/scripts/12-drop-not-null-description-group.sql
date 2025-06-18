-- liquibase formatted sql

-- changeset danil-kim:16
ALTER TABLE "public"."groups"
ALTER COLUMN description DROP NOT NULL;