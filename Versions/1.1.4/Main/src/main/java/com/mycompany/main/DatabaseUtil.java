package com.mycompany.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    // In DatabaseUtil.java change to:
    private static final String DB_URL = "jdbc:mysql://localhost:3306/noteapp_schema";
    private static final String DB_USER = "noteapp_user";
    private static final String DB_PASSWORD = "noteapp_password";
    
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
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected); // Debug line
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error adding user:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
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
                return rs.next();
            }
        } catch (SQLException e) {
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
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Note related methods
    public static List<String> getUserNotes(String username) {
        List<String> notes = new ArrayList<>();
        String sql = "SELECT n.content FROM notes n JOIN users u ON n.user_id = u.id WHERE u.username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(rs.getString("content"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }
    
    public static boolean addNote(String username, String content) {
        String sql = "INSERT INTO notes (user_id, content) VALUES ((SELECT id FROM users WHERE username = ?), ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, content);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateNote(String username, int noteIndex, String newContent) {
        // This is a simplified approach - in a real app, you'd use note IDs
        List<String> notes = getUserNotes(username);
        if (noteIndex < 0 || noteIndex >= notes.size()) {
            return false;
        }
        
        // Delete all notes and reinsert them with the updated one
        // This is not efficient but works for this example
        deleteAllNotesForUser(username);
        for (int i = 0; i < notes.size(); i++) {
            if (i == noteIndex) {
                addNote(username, newContent);
            } else {
                addNote(username, notes.get(i));
            }
        }
        return true;
    }
    
    private static void deleteAllNotesForUser(String username) {
        String sql = "DELETE FROM notes WHERE user_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}