-- liquibase formatted sql

-- changeset danil-kim:11
create table "public".user_posts (
    user_posts_id uuid primary key DEFAULT gen_random_uuid(),
    user_id uuid not null,
    post_id uuid not null,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE
);