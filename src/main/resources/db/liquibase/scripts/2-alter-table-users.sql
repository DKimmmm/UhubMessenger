-- liquibase formatted sql

-- changeset danil-kim:2
create table if not exists public."users"(
    user_id uuid primary key,
    "name" varchar(50) not null,
    lastname varchar(80) not null,
    phone varchar(20),
    email varchar(50),
    password varchar(100) not null,
    approved_phone boolean not null default false,
    approved_email boolean not null default false
);