-- liquibase formatted sql

-- changeset danil-kim:12
create table "public"."groups" (
    group_id uuid primary key,
    title varchar(100) not null,
    description varchar(255) not null
);