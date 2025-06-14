-- Drop existing database if it exists
DROP DATABASE IF EXISTS noteapp_schema;

-- Create the database
CREATE DATABASE noteapp_schema;

-- Use the database
USE noteapp_schema;

-- Create users table with all modifications
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    reset_token VARCHAR(255),
    token_expiry TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create notes table with all modifications
CREATE TABLE notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL DEFAULT 'Untitled',
    content TEXT NOT NULL,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create user_analytics table
CREATE TABLE user_analytics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create database user and grant privileges
CREATE USER IF NOT EXISTS 'noteapp_user'@'localhost' IDENTIFIED BY 'noteapp_password';
GRANT ALL PRIVILEGES ON noteapp_schema.* TO 'noteapp_user'@'localhost';
FLUSH PRIVILEGES;

-- Insert test data
INSERT INTO users (username, email, password) VALUES 
('testuser', 'test@example.com', 'hashed_password_here');

INSERT INTO notes (user_id, title, content, category) VALUES
(1, 'First Note', 'This is my first note', 'General'),
(1, 'Shopping List', 'Shopping list for the week', 'Shopping'),
(1, 'Project Ideas', 'Project ideas for the app', 'Ideas');