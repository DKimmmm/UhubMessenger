-- liquibase formatted sql

-- changeset kodi:1
create table if not exists public.images (
    image_id uuid primary key,
    file_name varchar(255),
    content_type varchar(255),
    size integer
);

---- changeset danil-kim:1
--create table if not exists public.users(
--    user_id uuid primary key,
--    "name" varchar(50) not null,
--    lastname varchar(80) not null,
--    phone varchar(20),
--    email varchar(50),
--    password varchar(100) not null,
--    approved_phone boolean not null default false,
--    approved_email boolean not null default false
--);
--
---- changeset danil-kim:2
--ALTER TABLE public.images
--    ADD COLUMN IF NOT EXISTS user_id UUID,
--    ADD CONSTRAINT fk_images_user_id
--        FOREIGN KEY (user_id)
--        REFERENCES public.users(user_id)
--        ON DELETE CASCADE;
--
--
