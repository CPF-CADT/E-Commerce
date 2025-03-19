CREATE DATABASE IF NOT EXISTS e_commerce;
USE e_commerce;

CREATE TABLE IF NOT EXISTS User (
    userId VARCHAR(10) PRIMARY KEY NOT NULL,
    firstname VARCHAR(20) NOT NULL,
    lastname VARCHAR(20) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    postalCode VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(20),
    dateOfBirth DATE
);

CREATE TABLE IF NOT EXISTS Staff (
    staffId VARCHAR(10) PRIMARY KEY NOT NULL,
    position VARCHAR(255) NOT NULL,
    FOREIGN KEY (staffId) REFERENCES User(userId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Customer (
    customerId VARCHAR(10) PRIMARY KEY NOT NULL,
    FOREIGN KEY (customerId) REFERENCES User(userId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS VIPCustomer (
    vipCustomerId VARCHAR(10) PRIMARY KEY NOT NULL,
    customerId VARCHAR(10) UNIQUE NOT NULL,
    voucherBalance DECIMAL(10, 2) DEFAULT 0.0,
    discountCard DECIMAL(5, 2) CHECK (discountCard BETWEEN 0 AND 1), 
    vipCardExpiry DATE,
    FOREIGN KEY (customerId) REFERENCES Customer(customerId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists Category(
    categoryId varchar(10) primary key not null,
    name varchar(255) not null
);

CREATE TABLE IF NOT EXISTS Product (
    productId VARCHAR(20) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    stock INT NOT NULL CHECK (stock >= 0),
    categoryId VARCHAR(255),
    description TEXT,
    FOREIGN KEY (categoryId) REFERENCES Category(categoryId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Review (
    reviewId VARCHAR(10) PRIMARY KEY NOT NULL,
    productId VARCHAR(20) NOT NULL,
    userId VARCHAR(10) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    FOREIGN KEY (productId) REFERENCES Product(productId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Cart (
    cartId VARCHAR(10) NOT NULL,
    productId VARCHAR(20) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cartId, productId),
    FOREIGN KEY (cartId) REFERENCES User(userId) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (productId) REFERENCES Product(productId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `Order` (
    orderId VARCHAR(10) PRIMARY KEY NOT NULL,
    cartId VARCHAR(10) NOT NULL,
    orderDate DATE NOT NULL,
    paymentDate DATE DEFAULT CURRENT_DATE,
    paymentMethod VARCHAR(50) NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED', 'SHIPPED') DEFAULT 'PENDING',
    FOREIGN KEY (cartId) REFERENCES Cart(cartId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Shipping (
    shippingId VARCHAR(10) PRIMARY KEY NOT NULL,
    orderId VARCHAR(10) NOT NULL,
    shippingDate DATE NOT NULL,
    deliveryDate DATE,
    carrier VARCHAR(50) NOT NULL,
    trackingNumber VARCHAR(50) UNIQUE NOT NULL,
    status ENUM('PENDING', 'SHIPPED', 'IN TRANSIT', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shippingAddress TEXT NOT NULL,
    FOREIGN KEY (orderId) REFERENCES `Order`(orderId) ON DELETE CASCADE ON UPDATE CASCADE
);
