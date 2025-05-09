// v 1.1.4
// This is with database

package com.mycompany.main;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static void centerWindow(javax.swing.JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    public static void setWindowSize(javax.swing.JFrame frame, int width, int height) {
        frame.setSize(width, height);
        frame.setPreferredSize(new Dimension(width, height));
    }

    public static void main(String[] args) {
        // Set the look and feel to match the system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the SignIn screen as the starting point of the application
        SwingUtilities.invokeLater(() -> {
            SignUp signUp = new SignUp();
            setWindowSize(signUp, 320, 480);
            centerWindow(signUp);
            signUp.setVisible(true);
        });
    }
}