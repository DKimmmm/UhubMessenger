-- liquibase formatted sql

-- changeset danil-kim:9
ALTER TABLE "public".post_images
ALTER COLUMN post_image_id SET DEFAULT gen_random_uuid();