package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class EditProfile extends JFrame {
    private JTextField jTextFieldFirstName;
    private JTextField jTextFieldLastName;
    private JLabel jLabelUsername; // Non-editable username display
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

        // Back button
        jButtonBack = new JButton("← Back");
        jButtonBack.setFont(new Font("Arial", Font.PLAIN, 12));
        jButtonBack.setBorderPainted(false);
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setForeground(new Color(0, 120, 215));
        jButtonBack.addActionListener(e -> {
            this.dispose();
            new Settings(currentUser).setVisible(true);
        });

        // Title panel with back button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(jButtonBack, BorderLayout.WEST);
        titlePanel.add(jLabelTitle, BorderLayout.CENTER);

        // First name field
        jTextFieldFirstName = new RoundedTextField(20);
        jTextFieldFirstName.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Last name field
        jTextFieldLastName = new RoundedTextField(20);
        jTextFieldLastName.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // Username display (non-editable)
        jLabelUsername = new JLabel();
        jLabelUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabelUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        jLabelUsername.setBackground(Color.WHITE);
        jLabelUsername.setOpaque(true);
        jLabelUsername.setPreferredSize(new Dimension(260, 45));

        // Email field
        jTextFieldEmail = new RoundedTextField(20);
        jTextFieldEmail.setBorder(new CompoundBorder(
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

        // Save button
        jButtonSave = new RoundedButton("Save Changes", 35);
        jButtonSave.setBackground(new Color(255, 204, 0));
        jButtonSave.setForeground(Color.WHITE);
        jButtonSave.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonSave.addActionListener(e -> saveProfile());

        // Layout setup
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        // Horizontal layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelUsername, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jTextFieldFirstName, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jTextFieldLastName, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jTextFieldEmail, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jPasswordField, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jPasswordFieldConfirm, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSave, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, 30))
        );

        // Vertical layout
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap(20, 20)
                .addComponent(titlePanel)
                .addGap(30)
                .addComponent(jLabelUsername, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jTextFieldFirstName, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jTextFieldLastName, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
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
            jLabelUsername.setText(user.getUsername());
            jTextFieldFirstName.setText(user.getFirstName());
            jTextFieldLastName.setText(user.getLastName());
            jTextFieldEmail.setText(user.getEmail());
        }
    }

    private void saveProfile() {
        String newUsername = jLabelUsername.getText().trim();
        String firstName = jTextFieldFirstName.getText().trim();
        String lastName = jTextFieldLastName.getText().trim();
        String newEmail = jTextFieldEmail.getText().trim();
        String newPassword = new String(jPasswordField.getPassword());
        String confirmPassword = new String(jPasswordFieldConfirm.getPassword());

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name and last name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If password fields are empty, keep the old password
        String finalPassword = newPassword.isEmpty() ? 
            DatabaseUtil.getUser(currentUser).getPassword() : newPassword;

        // Check if username was changed and if new one exists
        if (!newUsername.equals(currentUser) && DatabaseUtil.usernameExists(newUsername)) {
            JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email was changed and if new one exists
        if (!newEmail.equals(DatabaseUtil.getUser(currentUser).getEmail()) && 
            DatabaseUtil.emailExists(newEmail)) {
            JOptionPane.showMessageDialog(this, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update user in database
        if (DatabaseUtil.updateUser(currentUser, newUsername, firstName, lastName, newEmail, finalPassword)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.currentUser = newUsername;
            this.dispose();
            new Settings(newUsername).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}