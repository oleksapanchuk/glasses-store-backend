CREATE DATABASE ecommerce_glasses_shop;
USE ecommerce_glasses_shop;
CREATE TABLE `addresses` (
                             `address_id` bigint NOT NULL AUTO_INCREMENT,
                             `street` varchar(255) DEFAULT NULL,
                             `city` varchar(100) DEFAULT NULL,
                             `state_id` bigint DEFAULT NULL,
                             `country_id` bigint DEFAULT NULL,
                             `zip_code` varchar(10) DEFAULT NULL,
                             PRIMARY KEY (`address_id`),
                             KEY `state_id` (`state_id`),
                             KEY `country_id` (`country_id`),
                             CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`state_id`) REFERENCES `states` (`state_id`),
                             CONSTRAINT `addresses_ibfk_2` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `countries` (
                             `country_id` bigint NOT NULL AUTO_INCREMENT,
                             `country_name` varchar(100) DEFAULT NULL,
                             PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `discounts` (
                             `discount_id` bigint NOT NULL,
                             `discount_percent` bigint DEFAULT NULL,
                             PRIMARY KEY (`discount_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order_items` (
                               `order_item_id` bigint NOT NULL AUTO_INCREMENT,
                               `order_item_quantity` int DEFAULT NULL,
                               `order_item_unit_price` decimal(10,2) DEFAULT NULL,
                               `order_id` bigint DEFAULT NULL,
                               `product_id` bigint DEFAULT NULL,
                               PRIMARY KEY (`order_item_id`),
                               KEY `order_id` (`order_id`),
                               KEY `product_id` (`product_id`),
                               CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
                               CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders` (
                          `order_id` bigint NOT NULL AUTO_INCREMENT,
                          `user_id` bigint DEFAULT NULL,
                          `order_date` date DEFAULT NULL,
                          `order_total` decimal(10,2) DEFAULT NULL,
                          `order_tracking_number` varchar(100) DEFAULT NULL,
                          `order_total_quantity` int DEFAULT NULL,
                          `order_total_price` decimal(10,2) DEFAULT NULL,
                          `order_status` varchar(100) DEFAULT NULL,
                          `order_date_created` datetime(6) DEFAULT NULL,
                          `shipping_address_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`order_id`),
                          KEY `user_id` (`user_id`),
                          KEY `shipping_address` (`shipping_address_id`),
                          CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                          CONSTRAINT `shipping_address` FOREIGN KEY (`shipping_address_id`) REFERENCES `addresses` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product_categories` (
                                      `category_id` bigint NOT NULL AUTO_INCREMENT,
                                      `category_name` varchar(100) DEFAULT NULL,
                                      PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product_has_category` (
                                        `product_id` bigint NOT NULL,
                                        `category_id` bigint NOT NULL,
                                        PRIMARY KEY (`product_id`,`category_id`),
                                        KEY `product_category_ibfk_2` (`category_id`),
                                        CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
                                        CONSTRAINT `product_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `products` (
                            `product_id` bigint NOT NULL AUTO_INCREMENT,
                            `product_sku` varchar(255) DEFAULT NULL,
                            `product_name` varchar(100) DEFAULT NULL,
                            `product_description` text,
                            `product_price` decimal(10,2) DEFAULT NULL,
                            `product_quantity` int DEFAULT NULL,
                            `product_active` bit(1) DEFAULT b'1',
                            `product_date_created` datetime(6) DEFAULT NULL,
                            `product_last_updated` datetime(6) DEFAULT NULL,
                            `discount_id` bigint DEFAULT NULL,
                            `product_image_url` varchar(500) DEFAULT NULL,
                            PRIMARY KEY (`product_id`),
                            KEY `discount_id` (`discount_id`),
                            CONSTRAINT `products_ibfk_2` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`discount_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `states` (
                          `state_id` bigint NOT NULL AUTO_INCREMENT,
                          `state_name` varchar(100) DEFAULT NULL,
                          `country_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`state_id`),
                          KEY `country_id` (`country_id`),
                          CONSTRAINT `states_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
                         `user_id` bigint NOT NULL AUTO_INCREMENT,
                         `user_first_name` varchar(100) DEFAULT NULL,
                         `user_last_name` varchar(100) DEFAULT NULL,
                         `user_email` varchar(100) DEFAULT NULL,
                         `address_id` bigint DEFAULT NULL,
                         PRIMARY KEY (`user_id`),
                         KEY `address_id` (`address_id`),
                         CONSTRAINT `users_ibfk_1` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

