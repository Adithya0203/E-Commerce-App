CREATE DATABASE ecom;

use ecom;

CREATE TABLE customers (
    customerId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password LONGTEXT NOT NULL
);

CREATE TABLE products (
    productId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    stockQuantity INT NOT NULL
);

CREATE TABLE cart (
    cartId INT PRIMARY KEY AUTO_INCREMENT,
    customerId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(customerId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (productId) REFERENCES products(productId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE orders (
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    customerId INT NOT NULL,
    orderDate DATE NOT NULL,
    totalPrice DECIMAL(10, 2) NOT NULL,
    shippingAddress VARCHAR(100) NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(customerId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE orderItems (
    orderItemId INT PRIMARY KEY AUTO_INCREMENT,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (orderId) REFERENCES orders(orderId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (productId) REFERENCES products(productId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);