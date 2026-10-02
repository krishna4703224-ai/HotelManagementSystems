# HotelManagementSystems
A desktop-based Hotel Management System developed using Java AWT, Java Swing, and MySQL to simplify and automate hotel operations. The application provides a user-friendly interface for managing guests, rooms, reservations, check-in/check-out, billing, and hotel records.
## 🏨 Hotel Management System

A desktop-based **Hotel Management System** developed using **Java AWT, Java Swing, and MySQL** to simplify and automate hotel operations. The application provides a user-friendly interface for managing guests, rooms, reservations, check-in/check-out, billing, and hotel records.

### 🔹 Key Features

* Guest registration and management
* Room management with room availability and status
* Check-in and Check-out management
* Room reservation and booking
* Automatic billing and payment details
* Guest details and booking records
* MySQL database integration for data storage
* Interactive dashboard with a user-friendly GUI
* CRUD operations for hotel records

### 🛠️ Technologies Used

* **Java**
* **Java AWT & Swing**
* **MySQL / MySQL Workbench**
* **JDBC**
* **IntelliJ IDEA**

### 🎯 Project Objective

The main objective of this project is to provide an efficient computerized solution for managing hotel operations while reducing manual work and maintaining guest, room, booking, and billing records in a centralized MySQL database.




CREATE DATABASE hotel_management;
USE hotel_management;
CREATE TABLE checkins (
    id INT PRIMARY KEY AUTO_INCREMENT,

    booking_id VARCHAR(30) NOT NULL,

    guest_name VARCHAR(100) NOT NULL,

    phone VARCHAR(20) NOT NULL,

    id_type VARCHAR(50) NOT NULL,

    room_number VARCHAR(20) NOT NULL,

    room_type VARCHAR(50) NOT NULL,

    check_in_date DATE NOT NULL,

    number_of_guests INT NOT NULL,

    status VARCHAR(30) DEFAULT 'Checked In',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
SHOW TABLES;
USE hotel_management;
SELECT * FROM checkins;
truncate table rooms;
USE hotel_management;

CREATE TABLE rooms (

    id INT PRIMARY KEY AUTO_INCREMENT,

    room_number VARCHAR(20) NOT NULL UNIQUE,

    room_type VARCHAR(50) NOT NULL,

    price DECIMAL(10,2) NOT NULL,

    status VARCHAR(30) DEFAULT 'Available',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);
INSERT INTO rooms
(room_number, room_type, price, status)
VALUES

('101', 'Single Room', 2500, 'Available'),

('102', 'Double Room', 3000, 'Available'),

('103', 'Delux Room', 4000, 'Available'),

('104', 'Suite Room', 4500, 'Available'),

('105', 'Single Room', 2500, 'Available'),

('106', 'Double Room', 3000, 'Available'),

('107', 'Delux Room', 4000, 'Maintenance'),

('108', 'Suite Room', 4500, 'Available'),

('109', 'Single Room', 2500, 'Available'),

('110', 'Double Room', 3000, 'Available'),

('111', 'Deluxe Room', 4000, 'Available'),

('112', 'Suite Room', 4500, 'Available'),

('113', 'Single Room', 2500, 'Available'),

('114', 'Double Room', 3000, 'Available'),

('115', 'Delux Room', 4000, 'Maintenance'),

('116', 'suite Room', 4500, 'Available'),

('117', 'Single Room', 2500, 'Available'),

('118', 'Double Room', 3000, 'Available'),

('119', 'Deluxe Room', 4000, 'Available'),

('120', 'Suite Room', 4000, 'Available');
SELECT * FROM rooms;
USE hotel_management;

SHOW TABLES;

SELECT * FROM rooms;
USE hotel_management;

SELECT * FROM rooms;

SELECT * FROM checkins;
SELECT room_number, room_type, price, status
FROM rooms
ORDER BY room_number;
USE hotel_management;
SELECT * FROM checkins;
USE hotel_management;

INSERT INTO rooms
(room_number, room_type, price, status)
VALUES
('101', 'Single', 1500, 'Available'),
('102', 'Double', 2500, 'Available'),
('103', 'Deluxe', 3500, 'Available'),
('104', 'Suite', 5000, 'Available');
USE hotel_management;

SELECT *
FROM rooms
ORDER BY room_number;
USE hotel_management;

UPDATE rooms
SET
    room_type = 'Single',
    price = 1500,
    status = 'Available'
WHERE room_number = '101';

UPDATE rooms
SET
    room_type = 'Double',
    price = 2500,
    status = 'Available'
WHERE room_number = '102';

UPDATE rooms
SET
    room_type = 'Deluxe',
    price = 3500,
    status = 'Available'
WHERE room_number = '103';

UPDATE rooms
SET
    room_type = 'Suite',
    price = 5000,
    status = 'Available'
WHERE room_number = '104';
SELECT room_number, room_type, price, status
FROM rooms
ORDER BY room_number;
SET SQL_SAFE_UPDATES = 0;

UPDATE rooms SET room_type = 'Single Room' WHERE room_type = 'Single';
UPDATE rooms SET room_type = 'Double Room' WHERE room_type = 'Double';
UPDATE rooms SET room_type = 'Deluxe Room' WHERE room_type = 'Deluxe';
UPDATE rooms SET room_type = 'Suite Room' WHERE room_type = 'Suite';

SET SQL_SAFE_UPDATES = 1;

SET SQL_SAFE_UPDATES = 0;

-- Saare rooms ko 4 categories me sahi se distribute kar rahe hain
UPDATE rooms SET room_type = 'Single Room' WHERE CAST(room_number AS UNSIGNED) BETWEEN 101 AND 105;
UPDATE rooms SET room_type = 'Double Room' WHERE CAST(room_number AS UNSIGNED) BETWEEN 106 AND 110;
UPDATE rooms SET room_type = 'Deluxe Room' WHERE CAST(room_number AS UNSIGNED) BETWEEN 111 AND 115;
UPDATE rooms SET room_type = 'Suite Room' WHERE CAST(room_number AS UNSIGNED) BETWEEN 116 AND 120;

-- Sabhi rooms ka status 'Available' kar dete hain taaki check-in me show ho sakein
UPDATE rooms SET status = 'Available';

SET SQL_SAFE_UPDATES = 1;
SELECT * FROM rooms;
DESCRIBE checkins;
USE hotel_management;
ALTER TABLE checkins
ADD COLUMN check_out_date DATE NULL AFTER check_in_date;
ALTER TABLE checkins
ADD COLUMN total_amount DECIMAL(10,2) DEFAULT 0;


<img width="959" height="562" alt="image" src="https://github.com/user-attachments/assets/e5b46013-d1ce-4b16-9d32-a9f56cd192f1" />
<img width="959" height="599" alt="image" src="https://github.com/user-attachments/assets/d6394920-6068-43ac-ac1c-39ca14727984" />












