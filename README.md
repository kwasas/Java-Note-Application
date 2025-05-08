Here's the complete README in plain text format that you can copy all at once:

# NoteApp - README

## Overview
A simple Java Swing application for managing notes with MySQL database backend.

## Features
- User authentication (sign up/login)
- Create, view, update notes
- Persistent storage using MySQL

## Prerequisites
- Java JDK 8 or later
- MySQL Server 5.7 or later
- MySQL Connector/J (included in project dependencies)

## Database Setup

1. Create the database schema:
CREATE DATABASE noteapp_schema;

2. Create tables:
USE noteapp_schema;
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE notes (
    note_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

3. Create database user:
CREATE USER 'noteapp_user'@'localhost' IDENTIFIED BY 'noteapp_password';
GRANT ALL PRIVILEGES ON noteapp_schema.* TO 'noteapp_user'@'localhost';
FLUSH PRIVILEGES;

## Configuration
Update the database connection settings in DatabaseUtil.java if needed:
private static final String DB_URL = "jdbc:mysql://localhost:3306/noteapp_schema?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String DB_USER = "noteapp_user";
private static final String DB_PASSWORD = "noteapp_password";

## Running the Application
1. Compile and run the application from your IDE
2. Or use command line:
javac com/mycompany/main/*.java
java com.mycompany.main.Main

## Troubleshooting
If you encounter connection issues:
1. Verify MySQL server is running
2. Check database credentials in DatabaseUtil.java
3. Ensure the MySQL Connector/J is in your classpath

Common errors and solutions:
1. "Public Key Retrieval is not allowed" - Make sure DB_URL includes allowPublicKeyRetrieval=true
2. "Unknown column" errors - Verify your database schema matches exactly what's shown above
3. Connection failures - Check MySQL service is running and credentials are correct

## Known Issues
- First run may require MySQL driver configuration
- Some database errors may occur if schema isn't set up correctly

## Future Enhancements
- Password encryption
- Note categories/tags
- Search functionality
- Note sharing between users
- Rich text formatting in notes

## License
This project is open source.