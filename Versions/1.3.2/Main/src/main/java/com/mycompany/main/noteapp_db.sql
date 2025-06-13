- Drop existing database if it exists (optional - only use if you want a fresh start)
DROP DATABASE IF EXISTS noteapp_schema;

-- Create the database
CREATE DATABASE noteapp_schema;

-- Use the database
USE noteapp_schema;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create notes table with category support
CREATE TABLE notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create database user and grant privileges
CREATE USER IF NOT EXISTS 'noteapp_user'@'localhost' IDENTIFIED BY 'noteapp_password';
GRANT ALL PRIVILEGES ON noteapp_schema.* TO 'noteapp_user'@'localhost';
FLUSH PRIVILEGES;

-- Optional: Insert some test data
INSERT INTO users (username, email, password) VALUES 
('testuser', 'test@example.com', 'hashed_password_here');

INSERT INTO notes (user_id, content, category) VALUES
(1, 'This is my first note', 'General'),
(1, 'Shopping list for the week', 'Shopping'),
(1, 'Project ideas for the app', 'Ideas');