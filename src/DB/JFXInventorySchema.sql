
--CREATE DATABASE javafx_inventory_system
--CHARACTER SET utf8
--COLLATE utf8_general_ci;

--USE javafx_inventory_system;
--CHARSET utf8;

CREATE TABLE part (
    part_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	price DECIMAL UNSIGNED NOT NULL,
	inventory SMALLINT UNSIGNED NOT NULL,
	min_stock SMALLINT UNSIGNED NOT NULL,
	max_stock SMALLINT UNSIGNED NOT NULL,
	PRIMARY KEY (part_id)
);

CREATE TABLE in_house (
	part_id SMALLINT UNSIGNED NOT NULL,
	machine_id SMALLINT UNSIGNED NOT NULL,
	UNIQUE (machine_id),
	PRIMARY KEY (part_id),
	FOREIGN KEY (part_id) REFERENCES part (part_id)
);

CREATE TABLE outsourced (
	part_id SMALLINT UNSIGNED NOT NULL,
	company_name VARCHAR(60) NOT NULL,
	PRIMARY KEY (part_id) ,
	FOREIGN KEY (part_id) REFERENCES part (part_id)
);

CREATE TABLE product (
    product_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    price DECIMAL UNSIGNED NOT NULL,
    inventory SMALLINT UNSIGNED NOT NULL,
    min_stock SMALLINT UNSIGNED NOT NULL,
    max_stock SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY (product_id)
);

CREATE TABLE product_assoc_parts (
    product_id SMALLINT UNSIGNED NOT NULL,
    part_id SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY (product_id, part_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id),
    FOREIGN KEY (part_id) REFERENCES part (part_id)
);


