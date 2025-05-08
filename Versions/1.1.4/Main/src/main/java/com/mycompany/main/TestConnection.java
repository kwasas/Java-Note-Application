package com.mycompany.main;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/noteapp_schema";
        String user = "noteapp_user";
        String password = "noteapp_password";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Sumakses!");
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}