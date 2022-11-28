INSERT INTO users(id, role, username, password, firstname, lastname, birth_date, phone, image, status, registered_at)
VALUES (1, 'ADMIN', 'AdminEmail@gmail.com', '{noop}123', 'Sergey', 'Sidorov', '1985-04-22', '8-835-66-99-333',
        'AdminAvatar.jpg', 'APPROVED', '2022-09-08 10:00'),
       (2, 'USER', 'FirstUser@gmail.com', '123', 'Natalya', 'Stepanova', '2001-10-22', '+3-958-98-89-000',
        'UserAvatar.jpg', 'NEW', '2022-10-12 11:22'),
       (3, 'USER', 'SecondUser@gmail.com', '123', 'Michail', 'Malyshev', '2008-10-22',
        '+3-958-98-89-654', 'UserAvatar.jpg', 'NEW', '2022-10-21 13:10'),
       (4, 'OWNER', 'FirstOwner@gmail.com', '123', 'Andrey', 'Petrov', '2000-03-15', '+3-958-98-89-555',
        'UserAvatar.jpg', 'NEW', '2022-11-18 10:00'),
       (5, 'OWNER', 'SecondOwner@gmail.com', '123', 'Jack', 'Ivanov', '2000-03-15', '+3-958-98-89-666',
        'UserAvatar.jpg',
        'NEW', '2022-11-15 11:00');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT
INTO hotel(id, owner_id, name, available, status)
VALUES (1, (SELECT id FROM users WHERE username = 'FirstOwner@gmail.com'), 'Moscow Plaza', TRUE, 'APPROVED'),
       (2, (SELECT id FROM users WHERE username = 'FirstOwner@gmail.com'), 'Moscow Hotel', TRUE, 'APPROVED'),
       (3, (SELECT id FROM users WHERE username = 'FirstOwner@gmail.com'), 'Kiev Plaza', TRUE, 'APPROVED'),
       (4, (SELECT id FROM users WHERE username = 'FirstOwner@gmail.com'), 'Piter Plaza', TRUE, 'NEW'),
       (5, (SELECT id FROM users WHERE username = 'FirstOwner@gmail.com'), 'Minsk Plaza', TRUE, 'NEW'),
       (6, (SELECT id FROM users WHERE username = 'SecondOwner@gmail.com'), 'Warsaw Plaza', TRUE, 'APPROVED'),
       (7, (SELECT id FROM users WHERE username = 'SecondOwner@gmail.com'), 'Vien Hotel', TRUE, 'NEW'),
       (8, (SELECT id FROM users WHERE username = 'SecondOwner@gmail.com'), 'Berlin Plaza', TRUE, 'APPROVED'),
       (9, (SELECT id FROM users WHERE username = 'SecondOwner@gmail.com'), 'Paris Plaza', TRUE, 'APPROVED'),
       (10, (SELECT id FROM users WHERE username = 'SecondOwner@gmail.com'), 'Amsterdam Plaza', TRUE, 'APPROVED');
SELECT SETVAL('hotel_id_seq', (SELECT MAX(id) FROM hotel));

INSERT
INTO hotel_details(id, hotel_id, phone_number, country, locality, area, street, floor_count, star, description)
VALUES (1, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), '1111-111-111', 'Russia', 'Moscow', 'West', 'First', 15,
        'FOUR', 'Very good hotel'),
       (2, (SELECT id FROM hotel WHERE name = 'Moscow Hotel'), '0-000-0000-000-00', 'Russia', 'Moscow', 'EastSide',
        'First', 15, 'TWO', 'Not bad hotel'),
       (3, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), '2222-22-22', 'Ukraine', 'Kiev', 'West', 'Second', 20,
        'THREE', 'Nice hotel'),
       (4, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), '3333-333-333', 'Russia', 'Saint Petersburg', 'East',
        'Third', 5, 'FIVE', 'The best hotel ever'),
       (5, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), '4444-44-45', 'Belarus', 'Minsk', 'North', 'Fourth', 15,
        'TWO', 'Really good hotel!'),
       (6, (SELECT id FROM hotel WHERE name = 'Warsaw Plaza'), '4444-44-46', 'Poland', 'Warsaw', 'East', 'Third', 17,
        'TWO', 'Excellent hotel!'),
       (7, (SELECT id FROM hotel WHERE name = 'Vien Hotel'), '4444-44-47', 'Austria', 'Vien', 'North', 'Fourth', 3,
        'TWO', 'The best hotel in the world'),
       (8, (SELECT id FROM hotel WHERE name = 'Berlin Plaza'), '4444-44-48', 'Germany', 'Berlin', 'North', 'Fourth', 40,
        'TWO', 'The highest hotel in Germany'),
       (9, (SELECT id FROM hotel WHERE name = 'Paris Plaza'), '4444-44-49', 'France', 'Paris', 'North', 'Fourth', 20,
        'TWO', 'Lovely hotel!'),
       (10, (SELECT id FROM hotel WHERE name = 'Amsterdam Plaza'), '4444-50-44', 'Netherlands', 'Amsterdam', 'North',
        'Fourth', 7,
        'TWO', 'Really good hotel!');
SELECT SETVAL('hotel_details_id_seq', (SELECT MAX(id) FROM hotel_details));


INSERT INTO hotel_content (id, hotel_id, link, type)
VALUES (1, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 'Moscow Plaza.jpg', 'PHOTO'),
       (2, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 'Moscow Plaza.MP4', 'VIDEO'),
       (3, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 'Minsk Plaza.jpg', 'PHOTO'),
       (4, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 'Minsk Plaza.MP4', 'VIDEO'),
       (5, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 'Kiev Plaza.jpg', 'PHOTO'),
       (6, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 'Kiev Plaza.MP4', 'VIDEO'),
       (7, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 'Piter Plaza.jpg', 'PHOTO'),
       (8, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 'Piter Plaza.MP4', 'VIDEO');
SELECT SETVAL('hotel_content_id_seq', (SELECT MAX(id) FROM hotel_content));

INSERT INTO room(id, hotel_id, room_no, type, square, adult_bed_count, children_bed_count, cost, available, floor,
                 description)
VALUES (1, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 1, 'TRPL', 25.3, 3, 0, 1500.99, TRUE, 1,
        'Nice room in Moscow Plaza'),
       (2, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 2, 'QDPL', 45.0, 4, 2, 2500, TRUE, 1,
        'Nice room in Moscow Plaza'),
       (3, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 7, 'TWIN', 35.5, 2, 1, 1900.85, TRUE, 3,
        'Nice room in Moscow Plaza'),
       (4, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 7, 'SGL', 20.5, 1, 0, 850.58, FALSE, 5,
        'Nice room in Moscow Plaza'),
       (5, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 3, 'TRPL', 55.5, 3, 1, 1700, TRUE, 1,
        'Nice room in Kiev Plaza'),
       (6, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 4, 'QDPL', 15.0, 4, 2, 2900, TRUE, 1,
        'Nice room in Kiev Plaza'),
       (7, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 6, 'TWIN', 35.5, 2, 0, 1950.58, TRUE, 3,
        'Nice room in Kiev Plaza'),
       (8, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 12, 'SGL', 23.5, 1, 0, 950.58, FALSE, 5,
        'Nice room in Kiev Plaza'),
       (9, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 3, 'TRPL', 25.5, 3, 2, 1500, TRUE, 1,
        'Nice room in Piter Plaza'),
       (10, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 1, 'QDPL', 45.0, 4, 1, 2500, TRUE, 1,
        'Nice room in Piter Plaza'),
       (11, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 6, 'TWIN', 35.5, 2, 1, 1900.85, TRUE, 3,
        'Nice room in Piter Plaza'),
       (12, (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 5, 'SGL', 20.5, 1, 0, 850.58, FALSE, 5,
        'Nice room in Piter Plaza'),
       (13, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 1, 'TRPL', 25.5, 3, 1, 1900, TRUE, 1,
        'Nice room in Minsk Plaza'),
       (14, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 1, 'QDPL', 45.0, 4, 2, 2100, TRUE, 1,
        'Nice room in Minsk Plaza'),
       (15, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 7, 'TWIN', 35.5, 2, 1, 1100.85, TRUE, 3,
        'Nice room in Minsk Plaza'),
       (16, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 10, 'SGL', 20.5, 1, 0, 1850.58, FALSE, 5,
        'Nice room in Minsk Plaza');
SELECT SETVAL('room_id_seq', (SELECT MAX(id) FROM room));

INSERT INTO booking_request(id, created_at, hotel_id, room_id, user_id, check_in, check_out, status)
VALUES (1, '2022-10-10 12:05', (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 1, 2, '2022-10-10', '2022-10-15',
        'NEW'),
       (2, '2022-10-11 12:05', (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 6, 2, '2022-11-10', '2022-11-15',
        'NEW'),
       (3, '2022-10-12 12:05', (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 3, 2, '2022-10-22', '2022-11-30',
        'APPROVED'),
       (4, '2022-10-13 12:05', (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 1, 2, '2022-12-10', '2022-12-15',
        'APPROVED'),
       (5, '2022-10-14 12:05', (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 10, 2, '2023-01-10', '2023-12-15',
        'PAID'),
       (6, '2022-10-15 12:05', (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 4, 3, '2022-10-10', '2022-10-15',
        'CANCELED'),
       (7, '2022-10-16 12:05', (SELECT id FROM hotel WHERE name = 'Piter Plaza'), 6, 3, '2022-12-10', '2022-12-15',
        'APPROVED'),
       (8, '2022-10-17 12:05', (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 2, 3, '2023-01-10', '2023-01-15',
        'PAID'),
       (9, '2022-10-18 12:05', (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 12, 3, '2022-10-10', '2022-01-15',
        'CANCELED');
SELECT SETVAL('booking_request_id_seq', (SELECT MAX(id) FROM booking_request));

INSERT INTO review(id, hotel_id, user_id, created_at, rating)
VALUES (1, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 2, NOW(), 5),
       (2, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 3, NOW(), 3),
       (3, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 3, NOW(), 4),
       (4, (SELECT id FROM hotel WHERE name = 'Minsk Plaza'), 2, NOW(), 2),

       (5, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 2, NOW(), 3),
       (6, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 3, NOW(), 3),
       (7, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 3, NOW(), 4),
       (8, (SELECT id FROM hotel WHERE name = 'Moscow Plaza'), 2, NOW(), 2),

       (9, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 2, NOW(), 5),
       (10, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 3, NOW(), 5),
       (11, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 3, NOW(), 4),
       (12, (SELECT id FROM hotel WHERE name = 'Kiev Plaza'), 2, NOW(), 3),

       (13, (SELECT id FROM hotel WHERE name = 'Warsaw Plaza'), 2, NOW(), 2),
       (14, (SELECT id FROM hotel WHERE name = 'Warsaw Plaza'), 3, NOW(), 1),
       (15, (SELECT id FROM hotel WHERE name = 'Warsaw Plaza'), 3, NOW(), 3),
       (16, (SELECT id FROM hotel WHERE name = 'Warsaw Plaza'), 2, NOW(), 2),

       (17, (SELECT id FROM hotel WHERE name = 'Paris Plaza'), 2, NOW(), 4),
       (18, (SELECT id FROM hotel WHERE name = 'Paris Plaza'), 3, NOW(), 3),
       (19, (SELECT id FROM hotel WHERE name = 'Paris Plaza'), 3, NOW(), 3),
       (20, (SELECT id FROM hotel WHERE name = 'Paris Plaza'), 2, NOW(), 3),

       (21, (SELECT id FROM hotel WHERE name = 'Amsterdam Plaza'), 2, NOW(), 5),
       (22, (SELECT id FROM hotel WHERE name = 'Amsterdam Plaza'), 3, NOW(), 5),
       (23, (SELECT id FROM hotel WHERE name = 'Amsterdam Plaza'), 3, NOW(), 5),
       (24, (SELECT id FROM hotel WHERE name = 'Amsterdam Plaza'), 2, NOW(), 4),

       (25, (SELECT id FROM hotel WHERE name = 'Vien Hotel'), 2, NOW(), 5),
       (26, (SELECT id FROM hotel WHERE name = 'Vien Hotel'), 3, NOW(), 5),
       (27, (SELECT id FROM hotel WHERE name = 'Vien Hotel'), 3, NOW(), 4),
       (28, (SELECT id FROM hotel WHERE name = 'Vien Hotel'), 2, NOW(), 4);
SELECT SETVAL('review_id_seq', (SELECT MAX(id) FROM review));

INSERT INTO review_content(id, review_id, link, type)
VALUES (1, 1, 'Photo_1.jpg', 'PHOTO'),
       (2, 2, 'Video_1.jpg', 'VIDEO'),
       (3, 4, 'Video_2.jpg', 'PHOTO');
SELECT SETVAL('review_content_id_seq', (SELECT MAX(id) FROM review_content));