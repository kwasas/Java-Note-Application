package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditProfile extends javax.swing.JFrame {
    private JTextField jTextFieldUsername;
    private JTextField jTextFieldEmail;
    private JPasswordField jPasswordField;
    private JPasswordField jPasswordFieldConfirm;
    private JButton jButtonSave;
    private JButton jButtonCancel;
    private JLabel jLabelTitle;
    private String currentUser;

    public EditProfile(String username) {
        this.currentUser = username;
        initComponents();
        loadUserData();
        Main.setWindowSize(this, 380, 500);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Edit Profile");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jTextFieldUsername = new JTextField();
        jTextFieldEmail = new JTextField();
        jPasswordField = new JPasswordField();
        jPasswordFieldConfirm = new JPasswordField();

        jButtonSave = new JButton("Save Changes");
        jButtonSave.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonSave.setBackground(new Color(100, 150, 255));
        jButtonSave.setForeground(Color.WHITE);
        jButtonSave.setFocusPainted(false);
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);

        jButtonCancel = new JButton("Cancel");
        jButtonCancel.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonCancel.setBackground(new Color(200, 200, 200));
        jButtonCancel.setFocusPainted(false);
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(jTextFieldUsername);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(jTextFieldEmail);
        formPanel.add(new JLabel("New Password:"));
        formPanel.add(jPasswordField);
        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(jPasswordFieldConfirm);
        formPanel.add(jButtonCancel);
        formPanel.add(jButtonSave);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Edit Profile");

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(jLabelTitle, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);

        pack();
    }

    private void loadUserData() {
        User user = DatabaseUtil.getUser(currentUser);
        if (user != null) {
            jTextFieldUsername.setText(user.getUsername());
            jTextFieldEmail.setText(user.getEmail());
        }
    }

    private void jButtonSaveActionPerformed(ActionEvent evt) {
        String newUsername = jTextFieldUsername.getText().trim();
        String newEmail = jTextFieldEmail.getText().trim();
        String newPassword = new String(jPasswordField.getPassword());
        String confirmPassword = new String(jPasswordFieldConfirm.getPassword());

        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username and email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If password field is empty, keep the old password
        String finalPassword = newPassword.isEmpty() ? 
            DatabaseUtil.getUser(currentUser).getPassword() : newPassword;

        if (!newUsername.equals(currentUser) && DatabaseUtil.usernameExists(newUsername)) {
            JOptionPane.showMessageDialog(this, 
                "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!jTextFieldEmail.getText().equals(DatabaseUtil.getUser(currentUser).getEmail()) && 
            DatabaseUtil.emailExists(newEmail)) {
            JOptionPane.showMessageDialog(this, 
                "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.updateUser(currentUser, newUsername, newEmail, finalPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new Settings(newUsername).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonCancelActionPerformed(ActionEvent evt) {
        this.dispose();
        new Settings(currentUser).setVisible(true);
    }
}