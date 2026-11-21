-- Reference schema only. The no-download Java app does not connect to this SQL schema.
CREATE DATABASE pharmacy_db;
USE pharmacy_db;
CREATE TABLE suppliers (supplier_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, phone VARCHAR(20), email VARCHAR(100), address VARCHAR(255));
CREATE TABLE medicines (medicine_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(120) NOT NULL, category VARCHAR(30), supplier_id INT, batch_no VARCHAR(50), quantity INT, price DECIMAL(10,2), expiry_date DATE, reorder_level INT, FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id));
CREATE TABLE sales (sale_id INT AUTO_INCREMENT PRIMARY KEY, sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, customer_name VARCHAR(100), total_amount DECIMAL(10,2));
CREATE TABLE sale_items (item_id INT AUTO_INCREMENT PRIMARY KEY, sale_id INT, medicine_id INT, quantity INT, unit_price DECIMAL(10,2), line_total DECIMAL(10,2));
