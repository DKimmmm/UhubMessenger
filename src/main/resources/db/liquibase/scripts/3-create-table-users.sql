-- liquibase formatted sql

-- changeset danil-kim:3
alter table public.images
    ADD COLUMN IF NOT EXISTS user_id UUID,
    ADD CONSTRAINT fk_images_user_id
        FOREIGN KEY (user_id)
        REFERENCES public.users(user_id)
        ON DELETE CASCADE;