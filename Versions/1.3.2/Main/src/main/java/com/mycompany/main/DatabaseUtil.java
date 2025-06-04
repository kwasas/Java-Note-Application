package com.mycompany.main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/noteapp_schema?" +
                                      "useSSL=false&" +
                                      "allowPublicKeyRetrieval=true&" +
                                      "serverTimezone=UTC";

    private static final String DB_USER = "noteapp_user";
    private static final String DB_PASSWORD = "noteapp_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }   
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // User related methods
    public static boolean addUser(String username, String firstName, String lastName, String email, String password) {
        String sql = "INSERT INTO users (username, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateUser(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            stmt.setString(3, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
            return false;
        }
    }

    public static boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    public static boolean emailExists(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            return false;
        }
    }

    public static User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateUser(String oldUsername, String newUsername, String firstName, String lastName, String email, String newPassword) {
        String sql = "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ?, password = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, newPassword);
            stmt.setString(6, oldUsername);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateUserPassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    public static boolean verifyUserEmail(String username, String email) {
        String sql = "SELECT * FROM users WHERE username = ? AND email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error verifying user email: " + e.getMessage());
            return false;
        }
    }

    public static String getUsernameFromInput(String usernameOrEmail) {
        String sql = "SELECT username FROM users WHERE username = ? OR email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("username") : null;
            }
        } catch (SQLException e) {
            System.err.println("Error getting username: " + e.getMessage());
            return null;
        }
    }

    // Note related methods
    public static List<Note> getUserNotes(String username) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT n.id, n.content, n.category FROM notes n JOIN users u ON n.user_id = u.id WHERE u.username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(new Note(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getString("category")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting notes: " + e.getMessage());
        }
        return notes;
    }

    public static List<String> getCategories(String username) {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT n.category FROM notes n JOIN users u ON n.user_id = u.id WHERE u.username = ? AND n.category IS NOT NULL";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String category = rs.getString("category");
                    if (category != null && !category.isEmpty()) {
                        categories.add(category);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        return categories;
    }

    public static boolean addNote(String username, String content, String category) {
        String sql = "INSERT INTO notes (user_id, content, category) VALUES " +
                    "((SELECT id FROM users WHERE username = ?), ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, content);
            
            if (category == null || category.trim().isEmpty()) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, category);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding note: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateNote(String username, int noteId, String newContent, String newCategory) {
        String sql = "UPDATE notes SET content = ?, category = ? WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newContent);
            
            if (newCategory == null || newCategory.trim().isEmpty()) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, newCategory);
            }
            
            stmt.setInt(3, noteId);
            stmt.setString(4, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating note: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteNote(String username, int noteId) {
        String sql = "DELETE FROM notes WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, noteId);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting note: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean resetPassword(String token, String newPassword) {
        // First verify the token exists and is valid
        String verifyTokenSql = "SELECT username FROM password_reset_tokens WHERE token = ? AND expiry_time > NOW()";
        String updatePasswordSql = "UPDATE users SET password = ? WHERE username = ?";
        String deleteTokenSql = "DELETE FROM password_reset_tokens WHERE token = ?";

        try (Connection conn = getConnection()) {
            // Verify token
            String username = null;
            try (PreparedStatement verifyStmt = conn.prepareStatement(verifyTokenSql)) {
                verifyStmt.setString(1, token);
                try (ResultSet rs = verifyStmt.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    }
                }
            }

            if (username == null) {
                return false; // Invalid or expired token
            }

            // Update password
            try (PreparedStatement updateStmt = conn.prepareStatement(updatePasswordSql)) {
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, username);
                int updated = updateStmt.executeUpdate();

                if (updated > 0) {
                    // Delete the used token
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteTokenSql)) {
                        deleteStmt.setString(1, token);
                        deleteStmt.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
        }
        return false;
    }
}

class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

class Note {
    private int id;
    private String content;
    private String category;

    public Note(int id, String content, String category) {
        this.id = id;
        this.content = content;
        this.category = category;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
}