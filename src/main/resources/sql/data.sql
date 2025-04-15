
select * from public.users;

select * from "public".images;

show COLUMNS public.images;

DESCRIBE public.images;;

\d+ public.images;

SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'images';

SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'users';

SELECT
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS referenced_table,
    ccu.column_name AS referenced_column
FROM
    information_schema.table_constraints AS tc
    JOIN information_schema.key_column_usage AS kcu
        ON tc.constraint_name = kcu.constraint_name
    JOIN information_schema.constraint_column_usage AS ccu
        ON tc.constraint_name = ccu.constraint_name
WHERE
    tc.constraint_type = 'FOREIGN KEY'
    AND tc.table_name = 'images';


SELECT i.image_id, i.content_type, i.file_name, i."size"
FROM images i
JOIN user_images ui ON i.image_id = ui.image_id
WHERE ui.user_id = 'd1fd9008-2201-4223-b0c6-a9ce9914ace6';

insert into user_images(user_image_id, image_id, user_id) values
('5143597a-0370-4b39-a77e-b28a15f640a7', '06d44a7f-8ab8-49c4-9999-e19b6386d709', 'd1fd9008-2201-4223-b0c6-a9ce9914ace6');

SELECT i.image_id, i.content_type, i.file_name, i."size"
FROM images i
JOIN post_images pi ON i.image_id = pi.image_id
WHERE pi.post_id = '5143597a-0370-4b39-a77e-b28a15f640a7';

insert into posts(post_id, description, tittle) values
('5143597a-0370-4b39-a77e-b28a15f640a7', 'description','tittle')
insert into posts(post_id, description, tittle) values
('5143597a-0370-4b39-a77e-b28a15f640a6', 'description','tittle')

insert into post_images(post_image_id, image_id, post_id) values
('5143597a-0370-4b39-a77e-b28a15f640a7', '06d44a7f-8ab8-49c4-9999-e19b6386d709', '5143597a-0370-4b39-a77e-b28a15f640a7');

insert into post_images(post_image_id, image_id, post_id) values
('5143597a-0370-4b39-a77e-b28a15f640a8', '06d44a7f-8ab8-49c4-9999-e19b6386d709', '5143597a-0370-4b39-a77e-b28a15f640a7');


select * from posts

DELETE FROM post_images pi WHERE post_id = '5143597a-0370-4b39-a77e-b28a15f640a7';

select * from post_images pi where post_id = '5143597a-0370-4b39-a77e-b28a15f640a7';

SELECT i.file_name
FROM images i
JOIN post_images pi ON i.image_id = pi.image_id
WHERE pi.post_id = '5143597a-0370-4b39-a77e-b28a15f640a7';

