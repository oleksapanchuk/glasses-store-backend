

/* -- DISCOUNTS -- */
insert into discounts (discount_id, discount_percent) values (1, 64);
insert into discounts (discount_id, discount_percent) values (2, 13);
insert into discounts (discount_id, discount_percent) values (3, 51);
insert into discounts (discount_id, discount_percent) values (4, 29);
insert into discounts (discount_id, discount_percent) values (5, 64);
insert into discounts (discount_id, discount_percent) values (6, 46);
insert into discounts (discount_id, discount_percent) values (7, 30);
insert into discounts (discount_id, discount_percent) values (8, 54);
insert into discounts (discount_id, discount_percent) values (9, 43);
insert into discounts (discount_id, discount_percent) values (10, 21);


/* -- COUNTRIES -- */
insert into countries (country_id, country_name) values (1, 'Greece');
insert into countries (country_id, country_name) values (2, 'Myanmar');
insert into countries (country_id, country_name) values (3, 'Philippines');
insert into countries (country_id, country_name) values (4, 'China');
insert into countries (country_id, country_name) values (5, 'Ukraine');
insert into countries (country_id, country_name) values (6, 'Cameroon');
insert into countries (country_id, country_name) values (7, 'Indonesia');
insert into countries (country_id, country_name) values (8, 'Mexico');
insert into countries (country_id, country_name) values (9, 'Japan');
insert into countries (country_id, country_name) values (10, 'Czech Republic');

/* -- STATES -- */
insert into states (state_id, state_name, country_id) values (1, 'Athens', 1);
insert into states (state_id, state_name, country_id) values (2, 'Thessaloniki', 1);
insert into states (state_id, state_name, country_id) values (3, 'Patras', 1);

insert into states (state_id, state_name, country_id) values (4, 'Kachin', 2);
insert into states (state_id, state_name, country_id) values (5, 'Shan', 2);
insert into states (state_id, state_name, country_id) values (6, 'Mandalay', 2);

insert into states (state_id, state_name, country_id) values (7, 'Cebu', 3);
insert into states (state_id, state_name, country_id) values (8, 'Pangasinan', 3);
insert into states (state_id, state_name, country_id) values (9, 'Iloilo', 3);

insert into states (state_id, state_name, country_id) values (10, 'Xinjiang', 4);
insert into states (state_id, state_name, country_id) values (11, 'Tibet', 4);
insert into states (state_id, state_name, country_id) values (12, 'Inner Mongolia', 4);

insert into states (state_id, state_name, country_id) values (13, 'Kyiv', 5);
insert into states (state_id, state_name, country_id) values (14, 'Kharkiv', 5);
insert into states (state_id, state_name, country_id) values (15, 'Lviv', 5);

insert into states (state_id, state_name, country_id) values (16, 'Adamawa', 6);
insert into states (state_id, state_name, country_id) values (17, 'Centre', 6);
insert into states (state_id, state_name, country_id) values (18, 'East', 6);

insert into states (state_id, state_name, country_id) values (19, 'Papua', 7);
insert into states (state_id, state_name, country_id) values (20, 'Sumatera', 7);
insert into states (state_id, state_name, country_id) values (21, 'Maluku', 7);

insert into states (state_id, state_name, country_id) values (22, 'State of Mexico', 8);
insert into states (state_id, state_name, country_id) values (23, 'Mexico City', 8);
insert into states (state_id, state_name, country_id) values (24, 'Jalisco', 8);

insert into states (state_id, state_name, country_id) values (25, 'Tokyo', 9);
insert into states (state_id, state_name, country_id) values (26, 'Kanagawa-ken', 9);
insert into states (state_id, state_name, country_id) values (27, 'Ōsaka-fu', 9);

insert into states (state_id, state_name, country_id) values (28, 'Prague', 10);
insert into states (state_id, state_name, country_id) values (29, 'Old Bohemian Region', 10);
insert into states (state_id, state_name, country_id) values (30, 'Karlovy Vary Region', 10);

/* -- PRODUCT CATEGORIES -- */
insert into product_categories (category_id, category_name) values (1, 'Men');
insert into product_categories (category_id, category_name) values (2, 'Women');
insert into product_categories (category_id, category_name) values (3, 'Unisex');
insert into product_categories (category_id, category_name) values (4, 'Kids');
insert into product_categories (category_id, category_name) values (5, 'Teens');
insert into product_categories (category_id, category_name) values (6, 'Adults');
insert into product_categories (category_id, category_name) values (7, 'Prescription');
insert into product_categories (category_id, category_name) values (8, 'Reading');
insert into product_categories (category_id, category_name) values (9, 'Computer');
insert into product_categories (category_id, category_name) values (10, 'Sunglasses');
insert into product_categories (category_id, category_name) values (11, 'Metal Frame');
insert into product_categories (category_id, category_name) values (12, 'Plastic Frame');
insert into product_categories (category_id, category_name) values (13, 'Wooden Frame');
insert into product_categories (category_id, category_name) values (14, 'Retro/Vintage');
insert into product_categories (category_id, category_name) values (15, 'Designer');
insert into product_categories (category_id, category_name) values (16, 'Sports');
insert into product_categories (category_id, category_name) values (17, 'Fashion');
insert into product_categories (category_id, category_name) values (18, 'Accessories');

/* -- PRODUCTS -- */
INSERT INTO `products` (`product_sku`, `product_name`, `product_description`, `product_price`, `product_quantity`, `product_active`, `product_date_created`, `product_last_updated`, `discount_id`, `product_image_url`) VALUES
('SKU001', 'Classic Aviator Sunglasses', 'Stylish and durable aviator sunglasses.', 49.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/3762932/pexels-photo-3762932.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU002', 'Retro Round Glasses', 'Vintage-inspired round glasses.', 39.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/3414327/pexels-photo-3414327.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU003', 'Modern Square Glasses', 'Contemporary square glasses for a sharp look.', 44.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/567448/pexels-photo-567448.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU004', 'Sporty Wrap Sunglasses', 'Protective wrap sunglasses for sports.', 54.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/3441119/pexels-photo-3441119.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU005', 'Elegant Cat Eye Glasses', 'Chic cat eye glasses for a sophisticated style.', 49.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/2710178/pexels-photo-2710178.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU006', 'Trendy Wayfarer Sunglasses', 'Fashionable wayfarer sunglasses for everyday wear.', 45.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/5257246/pexels-photo-5257246.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU007', 'Stylish Clubmaster Glasses', 'Timeless clubmaster glasses with a modern twist.', 47.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/5429852/pexels-photo-5429852.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU008', 'Durable Sports Glasses', 'High-performance glasses for sports and outdoor activities.', 55.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/975668/pexels-photo-975668.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU009', 'Chic Oversized Sunglasses', 'Bold oversized sunglasses for a statement look.', 48.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/947885/pexels-photo-947885.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU010', 'Versatile Rectangle Glasses', 'Classic rectangle glasses suitable for any face shape.', 42.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1112203/pexels-photo-1112203.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU011', 'Fashionable Oval Glasses', 'Elegant oval glasses for a refined look.', 43.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1223343/pexels-photo-1223343.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU012', 'Trendy Browline Glasses', 'Stylish browline glasses for a retro-inspired look.', 46.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/871495/pexels-photo-871495.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU013', 'Cool Pilot Sunglasses', 'Iconic pilot sunglasses with a modern edge.', 50.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1499477/pexels-photo-1499477.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU014', 'Sophisticated Rimless Glasses', 'Minimalist rimless glasses for a sleek look.', 51.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/762080/pexels-photo-762080.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU015', 'Stylish Semi-Rimless Glasses', 'Modern semi-rimless glasses for a subtle style.', 52.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1018134/pexels-photo-1018134.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU016', 'Cool Mirrored Sunglasses', 'Trendy mirrored sunglasses for a standout look.', 53.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/2095953/pexels-photo-2095953.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU017', 'Elegant Butterfly Sunglasses', 'Feminine butterfly sunglasses with a glamorous vibe.', 54.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1756620/pexels-photo-1756620.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU018', 'Versatile Round Sunglasses', 'Timeless round sunglasses for a vintage-inspired style.', 55.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/839011/pexels-photo-839011.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU019', 'Trendy Shield Sunglasses', 'Futuristic shield sunglasses for a bold look.', 56.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1648023/pexels-photo-1648023.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU020', 'Classic Horn-Rimmed Glasses', 'Retro horn-rimmed glasses for a distinctive style.', 57.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/354951/pexels-photo-354951.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU021', 'Stylish Geometric Glasses', 'Unique geometric glasses for a standout style.', 58.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/582039/pexels-photo-582039.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU022', 'Cool Flat Top Sunglasses', 'Edgy flat top sunglasses for a statement look.', 59.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/994848/pexels-photo-994848.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU023', 'Elegant Round Metal Glasses', 'Chic round metal glasses for a refined style.', 60.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1466844/pexels-photo-1466844.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU024', 'Trendy Clear Frame Glasses', 'Fashionable clear frame glasses for a modern look.', 61.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/3768166/pexels-photo-3768166.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU025', 'Versatile Polarized Sunglasses', 'Practical polarized sunglasses for glare reduction.', 62.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1652340/pexels-photo-1652340.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU026', 'Stylish Tortoiseshell Glasses', 'Classic tortoiseshell glasses for a timeless style.', 63.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/1752467/pexels-photo-1752467.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU027', 'Cool Gradient Sunglasses', 'Trendy gradient sunglasses for a fashionable look.', 64.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/944761/pexels-photo-944761.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU028', 'Elegant Pearl Glasses', 'Feminine pearl glasses for a chic style.', 65.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/2035764/pexels-photo-2035764.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU029', 'Trendy Neon Sunglasses', 'Bold neon sunglasses for a standout style.', 66.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/3307616/pexels-photo-3307616.jpeg?auto=compress&cs=tinysrgb&w=600'),
('SKU030', 'Versatile Clip-On Sunglasses', 'Practical clip-on sunglasses for versatile wear.', 67.99, 100, b'1', NOW(), NOW(), 1, 'https://images.pexels.com/photos/4491461/pexels-photo-4491461.jpeg?auto=compress&cs=tinysrgb&w=600');


insert into product_has_category values
(31, 2),
(32, 2),
(33, 2),
(34, 1),
(35, 2),
(36, 2),
(37, 1),
(38, 2),
(39, 2),
(40, 1),
(41, 1),
(42, 1),
(43, 2),
(44, 1),
(45, 1),
(46, 1),
(47, 1),
(48, 1),
(49, 1),
(50, 2),
(51, 2),
(52, 1),
(53, 1),
(54, 1),
(55, 1),
(56, 1),
(57, 1),
(58, 1),
(59, 1),
(60, 1);

insert into product_has_category values
(34, 1),
(37, 1),
(40, 1),
(41, 1),
(42, 1),
(44, 1),
(45, 1),
(46, 1),
(47, 1),
(48, 1),
(49, 1),
(52, 1),
(53, 1),
(54, 1),
(55, 1),
(56, 1),
(57, 1),
(58, 1),
(59, 1),
(60, 1);

DELETE FROM product_has_category
WHERE category_id = 3;

insert into product_has_category values
(31, 10),
(32, 7),
(33, 7),
(34, 7),
(35, 10),
(36, 9),
(37, 8),
(38, 8),
(39, 10),
(40, 7),
(41, 8),
(42, 9),
(43, 10),
(44, 8),
(45, 8),
(46, 10),
(47, 10),
(48, 10),
(49, 10),
(50, 7),
(51, 9),
(52, 10),
(53, 9),
(54, 8),
(55, 10),
(56, 7),
(57, 10),
(58, 7),
(59, 10),
(60, 10);

insert into product_has_category values
(31, 12),
(32, 11),
(33, 11),
(34, 11),
(35, 12),
(36, 12),
(37, 12),
(38, 12),
(39, 13),
(40, 12),
(41, 11),
(42, 13),
(43, 12),
(44, 11),
(45, 12),
(46, 11),
(47, 12),
(48, 11),
(49, 11),
(50, 11),
(51, 11),
(52, 11),
(53, 11),
(54, 13),
(55, 12),
(56, 11),
(57, 12),
(58, 12),
(59, 11),
(60, 12);


