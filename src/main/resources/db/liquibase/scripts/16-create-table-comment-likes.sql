-- liquibase formatted sql

-- changeset danil-kim:21
create table public.comment_likes (
    comment_like_id uuid not null,
    comment_id uuid not null,
    user_id uuid not null,
    constraint comment_likes_pkey primary key (comment_like_id),
    constraint fk_comment_likes_comment_id foreign key (comment_id)
        references public.comments(comment_id) on delete cascade,
    constraint fk_comment_likes_user_id foreign key (user_id)
        references public.users(user_id) on delete cascade
);

-- changeset danil-kim:22
create unique index idx_comment_likes_comment_id_user_id
    on public.comment_likes (comment_id, user_id);