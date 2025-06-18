-- liquibase formatted sql

-- changeset danil-kim:18
create table "public".comments(
    comment_id uuid primary key,
    "text" varchar(255) not null,
    user_id uuid not null,
    post_id uuid not null,
    foreign key (user_id) references users(user_id) on delete cascade,
    foreign key (post_id) references posts(post_id) on delete cascade
);