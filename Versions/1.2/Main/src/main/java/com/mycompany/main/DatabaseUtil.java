package com.mycompany.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/noteapp_schema?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
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
        System.out.println("Attempting to connect to: " + DB_URL);
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        System.out.println("Connection successful!");
        connection.setAutoCommit(false);
        return connection;
    }

    // User related methods
    public static boolean addUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
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
                boolean result = rs.next();
                conn.commit();
                return result;
            }
        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean result = rs.next();
                conn.commit();
                return result;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Note related methods
    public static List<String> getUserNotes(String username) {
        List<String> notes = new ArrayList<>();
        String sql = "SELECT n.content FROM notes n JOIN users u ON n.user_id = u.user_id WHERE u.username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(rs.getString("content"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error getting notes: " + e.getMessage());
            e.printStackTrace();
        }
        return notes;
    }

    public static boolean addNote(String username, String content) {
        String sql = "INSERT INTO notes (user_id, content) VALUES ((SELECT user_id FROM users WHERE username = ?), ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username);
            stmt.setString(2, content);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                conn.commit();
                System.out.println("Successfully added note for user: " + username);
                return true;
            } else {
                conn.rollback();
                System.out.println("Failed to add note - no rows affected");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding note: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNote(String username, int noteIndex, String newContent) {
        // First get the note_id for the user's note at the specified index
        String getNoteIdSql = "SELECT note_id FROM notes WHERE user_id = " +
                            "(SELECT user_id FROM users WHERE username = ?) " +
                            "ORDER BY note_id LIMIT 1 OFFSET ?";
        
        String updateSql = "UPDATE notes SET content = ? WHERE note_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement getIdStmt = conn.prepareStatement(getNoteIdSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            
            // Get the specific note ID
            getIdStmt.setString(1, username);
            getIdStmt.setInt(2, noteIndex);
            
            ResultSet rs = getIdStmt.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                System.out.println("No note found at index " + noteIndex + " for user " + username);
                return false;
            }
            
            int noteId = rs.getInt("note_id");
            
            // Update the note
            updateStmt.setString(1, newContent);
            updateStmt.setInt(2, noteId);
            
            int rowsUpdated = updateStmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                conn.commit();
                System.out.println("Successfully updated note ID " + noteId);
                return true;
            } else {
                conn.rollback();
                System.out.println("Update failed for note ID " + noteId);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating note: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void deleteAllNotesForUser(String username) {
        String sql = "DELETE FROM notes WHERE user_id = (SELECT user_id FROM users WHERE username = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                conn.commit();
                System.out.println("Deleted " + rowsDeleted + " notes for user: " + username);
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting notes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}