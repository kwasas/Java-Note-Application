package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ResetPassword extends JFrame {
    private JTextField jTextFieldToken;
    private JPasswordField jPasswordField;
    private JPasswordField jPasswordFieldConfirm;
    private JButton jButtonReset;
    private JLabel jLabelTitle;

    public ResetPassword() {
        initComponents();
        Main.setWindowSize(this, 380, 400);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Reset Password");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Token field
        jTextFieldToken = new RoundedTextField(20);
        jTextFieldToken.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Password fields
        jPasswordField = new RoundedPasswordField(20);
        jPasswordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        jPasswordFieldConfirm = new RoundedPasswordField(20);
        jPasswordFieldConfirm.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Reset button
        jButtonReset = new RoundedButton("Reset Password", 35);
        jButtonReset.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonReset.setBackground(new Color(100, 150, 255));
        jButtonReset.setForeground(Color.WHITE);
        jButtonReset.addActionListener(this::jButtonResetActionPerformed);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Reset Password");
        getContentPane().setBackground(Color.WHITE);

        // Form panel with better layout
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Add components with proper spacing
        formPanel.add(createLabel("Reset Token:"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(jTextFieldToken);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createLabel("New Password:"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(jPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createLabel("Confirm Password:"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(jPasswordFieldConfirm);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(jButtonReset);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jLabelTitle, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private void jButtonResetActionPerformed(ActionEvent evt) {
        String token = jTextFieldToken.getText().trim();
        String password = new String(jPasswordField.getPassword());
        String confirmPassword = new String(jPasswordFieldConfirm.getPassword());

        // Validation
        if (token.isEmpty()) {
            showError("Please enter your reset token");
            return;
        }

        if (password.isEmpty()) {
            showError("Password cannot be empty");
            return;
        }

        if (password.length() < 8) {
            showError("Password must be at least 8 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Attempt password reset
        if (DatabaseUtil.resetPassword(token, password)) {
            JOptionPane.showMessageDialog(this, 
                "Password reset successfully!\nYou can now sign in with your new password.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new SignIn().setVisible(true);
        } else {
            showError("Invalid or expired reset token");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}