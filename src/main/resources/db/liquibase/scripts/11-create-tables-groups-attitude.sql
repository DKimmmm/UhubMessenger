-- liquibase formatted sql

-- changeset danil-kim:13
create table "public".group_images (
    group_image_id uuid primary key default gen_random_uuid(),
    group_id uuid not null,
    image_id uuid not null,
    foreign key (group_id) references groups(group_id) on delete cascade,
    foreign key (image_id) references images(image_id) on delete cascade
);

-- changeset danil-kim:14
create table "public".group_users (
    group_user_id uuid primary key default gen_random_uuid(),
    group_id uuid not null,
    user_id uuid not null,
    foreign key (group_id) references groups(group_id) on delete cascade,
    foreign key (user_id) references users(user_id) on delete cascade
);

-- changeset danil-kim:15
create table "public".group_posts (
    group_post_id uuid primary key default gen_random_uuid(),
    group_id uuid not null,
    post_id uuid not null,
    foreign key (group_id) references groups(group_id) on delete cascade,
    foreign key (post_id) references posts(post_id) on delete cascade
);