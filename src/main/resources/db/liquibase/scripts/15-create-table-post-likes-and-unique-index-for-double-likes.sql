-- liquibase formatted sql

-- changeset danil-kim:19
create table public.post_likes (
    post_like_id uuid not null,
    post_id uuid not null,
    user_id uuid not null,
    constraint post_likes_pkey primary key (post_like_id),
    constraint fk_post_likes_post_id foreign key (post_id)
        references public.posts(post_id) on delete cascade,
    constraint fk_post_likes_user_id foreign key (user_id)
        references public.users(user_id) on delete cascade
);

-- changeset danil-kim:20
create unique index idx_post_likes_post_id_user_id
    on public.post_likes (post_id, user_id);