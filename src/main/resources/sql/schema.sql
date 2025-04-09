create table if not exists public.users(
    user_id uuid primary key,
--    user_id uuid primary key default gen_random_uuid(),
    "name" varchar(50) not null,
    lastname varchar(80) not null,
    phone varchar(20),
    email varchar(50),
    password varchar(100) not null,
    approved_phone boolean not null default false,
    approved_email boolean not null default false
);