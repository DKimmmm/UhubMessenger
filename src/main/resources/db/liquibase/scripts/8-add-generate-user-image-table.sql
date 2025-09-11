-- liquibase formatted sql

-- changeset danil-kim:10
ALTER TABLE "public".user_images
ALTER COLUMN user_image_id SET DEFAULT gen_random_uuid();