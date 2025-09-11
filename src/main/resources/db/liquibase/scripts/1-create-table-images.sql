-- liquibase formatted sql

-- changeset kodi:1
create table if not exists public.images (
    image_id uuid primary key,
    file_name varchar(255),
    content_type varchar(255),
    size integer
);