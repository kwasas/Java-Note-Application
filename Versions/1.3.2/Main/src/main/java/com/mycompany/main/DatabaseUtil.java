package com.mycompany.main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public static boolean addUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
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

    public static boolean updateUser(String oldUsername, String newUsername, String newEmail, String newPassword) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newEmail);
            stmt.setString(3, newPassword);
            stmt.setString(4, oldUsername);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    // Note related methods - FIXED VERSION
    public static List<Note> getUserNotes(String username) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT n.id, n.content, n.category FROM notes n JOIN users u ON n.user_id = u.id WHERE u.username = ? ORDER BY n.id DESC";
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
            e.printStackTrace();
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

    public static boolean addNote(String username, Note note) {
        String sql = "INSERT INTO notes (user_id, content, category) VALUES " +
                    "((SELECT id FROM users WHERE username = ?), ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, note.getContent());
            
            if (note.getCategory() == null || note.getCategory().trim().isEmpty()) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, note.getCategory());
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        note.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding note: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNote(String username, Note note) {
        String sql = "UPDATE notes SET content = ?, category = ? WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note.getContent());
            
            if (note.getCategory() == null || note.getCategory().trim().isEmpty()) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, note.getCategory());
            }
            
            stmt.setInt(3, note.getId());
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

    // Password reset methods
    public static boolean setPasswordResetToken(String username, String token) {
        String sql = "UPDATE users SET reset_token = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error setting reset token: " + e.getMessage());
            return false;
        }
    }

    public static boolean resetPassword(String token, String newPassword) {
        String sql = "UPDATE users SET password = ?, reset_token = NULL WHERE reset_token = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, token);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
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
    
    public static boolean deleteAllUserNotes(String username) {
        String sql = "DELETE FROM notes WHERE user_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() >= 0; // Returns true even if no notes were deleted
        } catch (SQLException e) {
            System.err.println("Error deleting user notes: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUserAccount(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user account: " + e.getMessage());
            return false;
        }
    }
}

class User {
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
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

    public Note(String content, String category) {
        this(0, content, category);
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public String getCategory() { return category; }

    public void setId(int id) { this.id = id; }
    public void setContent(String content) { this.content = content; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}