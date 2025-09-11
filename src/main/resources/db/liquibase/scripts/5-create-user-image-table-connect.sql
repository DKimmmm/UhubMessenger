-- liquibase formatted sql

-- changeset danil-kim:5
CREATE TABLE "public".user_images (
    user_image_id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    image_id uuid NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images(image_id) ON DELETE CASCADE
);