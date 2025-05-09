CREATE DATABASE IF NOT EXISTS noteapp_schema;

USE noteapp_schema;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create notes table
CREATE TABLE IF NOT EXISTS notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the user (if not exists)
CREATE USER IF NOT EXISTS 'noteapp_user'@'localhost' IDENTIFIED BY 'noteapp_password';
GRANT ALL PRIVILEGES ON noteapp_schema.* TO 'noteapp_user'@'localhost';
FLUSH PRIVILEGES;

SELECT * FROM users WHERE username = 'user1';