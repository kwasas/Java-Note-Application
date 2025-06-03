package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class EditProfile extends JFrame {
    private JTextField jTextFieldUsername;
    private JTextField jTextFieldEmail;
    private JPasswordField jPasswordField;
    private JPasswordField jPasswordFieldConfirm;
    private JButton jButtonSave;
    private JButton jButtonBack;
    private JLabel jLabelTitle;
    private String currentUser;

    public EditProfile(String username) {
        this.currentUser = username;
        initComponents();
        loadUserData();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Edit Profile");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Back button (top-left)
        jButtonBack = new JButton("← Back");
        jButtonBack.setFont(new Font("Arial", Font.PLAIN, 12));
        jButtonBack.setBorderPainted(false);
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setForeground(new Color(0, 120, 215));
        jButtonBack.addActionListener(e -> {
            this.dispose();
            new Settings(currentUser).setVisible(true);
        });

        // Username field (matches SignUp style)
        jTextFieldUsername = new RoundedTextField(20);
        jTextFieldUsername.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Email field (matches SignUp style)
        jTextFieldEmail = new RoundedTextField(20);
        jTextFieldEmail.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Password fields (matches SignUp style)
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

        // Save button (matches SignUp button style)
        jButtonSave = new RoundedButton("Save Changes", 35);
        jButtonSave.setBackground(new Color(255, 204, 0));
        jButtonSave.setForeground(Color.WHITE);
        jButtonSave.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonSave.addActionListener(e -> saveProfile());

        // Layout setup (matches SignUp exactly)
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBack, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldUsername, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldEmail, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldConfirm, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSave, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonBack)
                .addGap(20)
                .addComponent(jLabelTitle)
                .addGap(30)
                .addComponent(jTextFieldUsername, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jTextFieldEmail, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jPasswordFieldConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(30)
                .addComponent(jButtonSave, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE)
        );

        pack();
    }

    private void loadUserData() {
        User user = DatabaseUtil.getUser(currentUser);
        if (user != null) {
            jTextFieldUsername.setText(user.getUsername());
            jTextFieldEmail.setText(user.getEmail());
        }
    }

    private void saveProfile() {
        String newUsername = jTextFieldUsername.getText().trim();
        String newEmail = jTextFieldEmail.getText().trim();
        String newPassword = new String(jPasswordField.getPassword());
        String confirmPassword = new String(jPasswordFieldConfirm.getPassword());

        // Validation
        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If password fields are empty, keep the old password
        String finalPassword = newPassword.isEmpty() ? 
            DatabaseUtil.getUser(currentUser).getPassword() : newPassword;

        if (!newUsername.equals(currentUser) && DatabaseUtil.usernameExists(newUsername)) {
            JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newEmail.equals(DatabaseUtil.getUser(currentUser).getEmail()) && 
            DatabaseUtil.emailExists(newEmail)) {
            JOptionPane.showMessageDialog(this, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.updateUser(currentUser, newUsername, newEmail, finalPassword)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.currentUser = newUsername;
            this.dispose();
            new Settings(newUsername).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}