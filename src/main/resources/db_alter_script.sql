ALTER TABLE products
    ADD COLUMN product_image_url VARCHAR(500);
    
ALTER TABLE products
ADD COLUMN product_rating DECIMAL(3,2) NOT NULL DEFAULT 5 CHECK (product_rating >= 0 AND product_rating <= 5);

    
ALTER TABLE orders
    ADD COLUMN order_tracking_number VARCHAR(100);

ALTER TABLE orders
    ADD COLUMN order_total_quantity INT,
    ADD COLUMN order_total_price DECIMAL(10, 2),
    ADD COLUMN order_status VARCHAR(100),
    ADD COLUMN order_date_created DATETIME(6);

ALTER TABLE orders
	ADD COLUMN shipping_address_id BIGINT,
	ADD CONSTRAINT shipping_address FOREIGN KEY (shipping_address_id) REFERENCES addresses(address_id);

ALTER TABLE `products`
DROP FOREIGN KEY `products_ibfk_1`,
DROP COLUMN `category_id`;

CREATE TABLE `product_has_category` (
  `product_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`, `category_id`),
  CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `product_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE users ADD user_role VARCHAR(60);
ALTER TABLE users
ADD COLUMN user_password VARCHAR(300),
ADD COLUMN user_image_url VARCHAR(500);

ALTER TABLE users
DROP COLUMN user_first_name,
DROP COLUMN user_last_name;

ALTER TABLE users
ADD COLUMN user_username VARCHAR(255) UNIQUE;

-- Remove the user_image_url field
ALTER TABLE users DROP COLUMN user_image_url;

-- Add new fields
ALTER TABLE usersstates
ADD COLUMN first_name VARCHAR(30),
ADD COLUMN last_name VARCHAR(30),
ADD COLUMN phone_number VARCHAR(20),
ADD COLUMN is_subscribed BOOLEAN DEFAULT FALSE,
ADD COLUMN is_verified BOOLEAN DEFAULT FALSE;

-- Drop foreign key constraints
ALTER TABLE addresses
DROP FOREIGN KEY addresses_ibfk_1,  -- Replace with actual foreign key constraint names
DROP FOREIGN KEY addresses_ibfk_2;  -- Replace with actual foreign key constraint names

-- Modify column data types
ALTER TABLE addresses
MODIFY COLUMN state_id VARCHAR(255),
MODIFY COLUMN country_id VARCHAR(255);

-- Change the name of the state_id column to state
ALTER TABLE addresses
CHANGE COLUMN state_id state VARCHAR(255);

-- Change the name of the country_id column to country
ALTER TABLE addresses
CHANGE COLUMN country_id country VARCHAR(255);

ALTER TABLE addresses
ADD COLUMN user_id bigint,  -- Adjust the data type as needed
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE products
DROP COLUMN product_rating;

ALTER TABLE states DROP FOREIGN KEY states_ibfk_1;
DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS states;

ALTER TABLE products DROP FOREIGN KEY products_ibfk_2;
DROP TABLE IF EXISTS discounts;

ALTER TABLE orders DROP COLUMN order_total;
ALTER TABLE orders DROP COLUMN order_date;



    