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
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel);

        // Create top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Back button (left-aligned)
        jButtonBack = new JButton(new ImageIcon("C:\\Users\\KennethWayneNAsas\\Documents\\GitHub\\Java-Note-Application\\Versions\\1.3.1\\Main\\src\\main\\resources\\assets\\arrow_back.png"));
        jButtonBack.setBorder(BorderFactory.createEmptyBorder());
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setBorderPainted(false);
        jButtonBack.setFocusPainted(false);
        jButtonBack.addActionListener(e -> {
            this.dispose();
            new Settings(currentUser).setVisible(true);
        });

        // Title label (centered)
        jLabelTitle = new JLabel("Edit Profile", SwingConstants.CENTER);
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));

        topPanel.add(jButtonBack, BorderLayout.WEST);
        topPanel.add(jLabelTitle, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create center panel with form elements
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Username field
        jTextFieldUsername = new RoundedTextField(20);
        jTextFieldUsername.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15))
        );

        // Email field
        jTextFieldEmail = new RoundedTextField(20);
        jTextFieldEmail.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15))
        );

        // Password fields
        jPasswordField = new RoundedPasswordField(20);
        jPasswordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15))
        );

        jPasswordFieldConfirm = new RoundedPasswordField(20);
        jPasswordFieldConfirm.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15))
        );

        // Save button (now matches Settings button style)
        // Save button (now exactly matches SignUp's Create Account button)
        jButtonSave = new RoundedButton("Save Changes", 35);
        jButtonSave.setBackground(new Color(255, 204, 0)); // Same yellow color
        jButtonSave.setForeground(Color.WHITE);
        jButtonSave.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonSave.setFocusPainted(false);
        jButtonSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set preferred size to match SignUp button (200x40)
        jButtonSave.setPreferredSize(new Dimension(200, 40));
        jButtonSave.setMinimumSize(new Dimension(200, 40));
        jButtonSave.setMaximumSize(new Dimension(200, 40));
        
        jButtonSave.addActionListener(e -> saveProfile());

        // Add components to center panel with proper spacing
        Dimension fieldSize = new Dimension(300, 45);
        
        centerPanel.add(jTextFieldUsername);
        jTextFieldUsername.setMaximumSize(fieldSize);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        centerPanel.add(jTextFieldEmail);
        jTextFieldEmail.setMaximumSize(fieldSize);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        centerPanel.add(jPasswordField);
        jPasswordField.setMaximumSize(fieldSize);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        centerPanel.add(jPasswordFieldConfirm);
        jPasswordFieldConfirm.setMaximumSize(fieldSize);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Centered save button
        centerPanel.add(jButtonSave);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

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