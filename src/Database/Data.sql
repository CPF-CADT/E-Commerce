USE e_commerce;

-- Insert User Data (Staff and Customers)
INSERT INTO User (userId, firstname, lastname, email, password, street, city, state, postalCode, country, phoneNumber, dateOfBirth) 
VALUES
('S001', 'John', 'Doe', 'john.doe@example.com', 'password123', '123 Main St', 'New York', 'NY', '10001', 'USA', '555-0101', '1985-05-01'),
('S002', 'Jane', 'Smith', 'jane.smith@example.com', 'password123', '456 Oak St', 'Los Angeles', 'CA', '90001', 'USA', '555-0102', '1990-08-12'),
('S003', 'Alice', 'Johnson', 'alice.johnson@example.com', 'password123', '789 Pine St', 'Chicago', 'IL', '60001', 'USA', '555-0103', '1988-01-15'),
('S004', 'Bob', 'Brown', 'bob.brown@example.com', 'password123', '101 Maple St', 'Houston', 'TX', '77001', 'USA', '555-0104', '1986-03-22'),
('S005', 'Charlie', 'Davis', 'charlie.davis@example.com', 'password123', '202 Cedar St', 'Phoenix', 'AZ', '85001', 'USA', '555-0105', '1992-07-09'),
('C006', 'David', 'Martinez', 'david.martinez@example.com', 'password123', '303 Birch St', 'San Francisco', 'CA', '94101', 'USA', '555-0106', '1991-11-20'),
('C007', 'Eva', 'Miller', 'eva.miller@example.com', 'password123', '404 Cherry St', 'Dallas', 'TX', '75201', 'USA', '555-0107', '1993-05-30'),
('C008', 'Frank', 'Wilson', 'frank.wilson@example.com', 'password123', '505 Walnut St', 'Miami', 'FL', '33101', 'USA', '555-0108', '1989-02-14'),
('C009', 'Grace', 'Moore', 'grace.moore@example.com', 'password123', '606 Elm St', 'Denver', 'CO', '80201', 'USA', '555-0109', '1994-04-23'),
('C010', 'Henry', 'Taylor', 'henry.taylor@example.com', 'password123', '707 Ash St', 'Seattle', 'WA', '98101', 'USA', '555-0110', '1987-09-11');

-- Insert Staff Data
INSERT INTO Staff (staffId, position)
VALUES
('S001', 'Manager'),
('S002', 'Vice President'),
('S003', 'Teacher'),
('S004', 'Class Monitor'),
('S005', 'Assistant');

-- Insert Customer Data
INSERT INTO Customer (customerId)
VALUES
('C006'),
('C007'),
('C008'),
('C009'),
('C010');

-- Insert VIP Customers
INSERT INTO VIPCustomer (vipCustomerId, customerId, voucherBalance, discountCard, vipCardExpiry)
VALUES
('V001', 'C006', 100.00, 0.10, '2025-05-01'),
('V002', 'C007', 150.00, 0.15, '2025-06-01'),
('V003', 'C008', 200.00, 0.20, '2025-07-01'),
('V004', 'C009', 250.00, 0.25, '2025-08-01'),
('V005', 'C010', 300.00, 0.30, '2025-09-01');

-- Insert Categories
INSERT INTO Category (categoryId, name)
VALUES
('CAT01', 'Electronics'),
('CAT02', 'Accessories');

-- Insert Products
INSERT INTO Product (productId, name, price, stock, categoryId, description)
VALUES
('P001', 'Laptop', 1200.00, 50, 'CAT01', 'High-performance laptop with 16GB RAM and 512GB SSD'),
('P002', 'Smartphone', 799.99, 100, 'CAT01', 'Latest model smartphone with a 48MP camera'),
('P003', 'Headphones', 199.99, 150, 'CAT02', 'Noise-cancelling over-ear headphones');

-- Insert Reviews
INSERT INTO Review (reviewId, productId, userId, rating, comment)
VALUES
('R001', 'P001', 'C006', 5, 'Excellent laptop! Fast and reliable.'),
('R002', 'P002', 'C007', 4, 'Great smartphone, but battery life could be better.'),
('R003', 'P003', 'C008', 5, 'Amazing sound quality and comfortable fit!');

-- Insert Cart Data
INSERT INTO Cart (cartId, productId, quantity)
VALUES
('C006', 'P001', 1),
('C007', 'P002', 2),
('C008', 'P003', 1);

-- Insert Orders
INSERT INTO `Order` (orderId, cartId, orderDate, paymentDate, paymentMethod, status)
VALUES
('O001', 'C006', '2025-03-01', '2025-03-01', 'Credit Card', 'PENDING'),
('O002', 'C007', '2025-02-28', '2025-02-28', 'Debit Card', 'COMPLETED'),
('O003', 'C008', '2025-03-02', '2025-03-02', 'PayPal', 'PENDING');

-- Insert Shipping Data
INSERT INTO Shipping (shippingId, orderId, shippingDate, deliveryDate, carrier, trackingNumber, status, shippingAddress)
VALUES
('SH001', 'O001', '2025-03-02', '2025-03-05', 'FedEx', 'TRACK12345', 'IN TRANSIT', '303 Birch St, San Francisco, CA, 94101');

