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



    