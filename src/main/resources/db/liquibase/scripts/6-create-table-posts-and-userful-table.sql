-- liquibase formatted sql

-- changeset danil-kim:6
CREATE TABLE "public".posts (
    post_id uuid PRIMARY KEY,
    description varchar(2000),
    tittle varchar(255)
);


-- changeset danil-kim:7
CREATE TABLE "public".post_images (
    post_image_id uuid PRIMARY KEY,
    post_id uuid NOT NULL,
    image_id uuid NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images(image_id) ON DELETE CASCADE);