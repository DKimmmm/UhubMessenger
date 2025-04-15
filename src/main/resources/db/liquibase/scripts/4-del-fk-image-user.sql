-- liquibase formatted sql

-- changeset danil-kim:4
ALTER TABLE public.images
DROP COLUMN user_id;
--ALTER TABLE images
--DROP CONSTRAINT fk_images_user_id;

