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
    
    private static final String USERNAME_PLACEHOLDER = "Enter username";
    private static final String EMAIL_PLACEHOLDER = "Enter email";
    private static final String PASSWORD_PLACEHOLDER = "Enter new password";
    private static final String CONFIRM_PASSWORD_PLACEHOLDER = "Confirm new password";

    public EditProfile(String username) {
        this.currentUser = username;
        initComponents();
        loadUserData();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel);

        // Top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));

        jButtonBack = new JButton(new ImageIcon("C:\\Users\\KennethWayneNAsas\\Documents\\GitHub\\Java-Note-Application\\Versions\\1.3.1\\Main\\src\\main\\resources\\assets\\arrow_back.png"));
        jButtonBack.setBorder(BorderFactory.createEmptyBorder());
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setBorderPainted(false);
        jButtonBack.setFocusPainted(false);
        jButtonBack.addActionListener(e -> {
            this.dispose();
            new Settings(currentUser).setVisible(true);
        });

        jLabelTitle = new JLabel("Edit Profile");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(jButtonBack, BorderLayout.WEST);
        topPanel.add(jLabelTitle, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with form elements
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30)); // Reduced bottom padding

        // Form fields with labels (left-aligned)
        addLeftAlignedField(centerPanel, "Username", jTextFieldUsername = createTextField(USERNAME_PLACEHOLDER));
        addLeftAlignedField(centerPanel, "Email", jTextFieldEmail = createTextField(EMAIL_PLACEHOLDER));
        addLeftAlignedField(centerPanel, "New Password", jPasswordField = createPasswordField(PASSWORD_PLACEHOLDER));
        addLeftAlignedField(centerPanel, "Confirm Password", jPasswordFieldConfirm = createPasswordField(CONFIRM_PASSWORD_PLACEHOLDER));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create separate panel for the save button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 145, 0)); // Padding only at bottom
        
        jButtonSave = createSaveButton();
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(jButtonSave);
        buttonPanel.add(buttonWrapper, BorderLayout.CENTER);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    private void addLeftAlignedField(JPanel parentPanel, String labelText, JComponent inputField) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(new Color(80, 80, 80));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Input field
        inputField.setMaximumSize(new Dimension(300, 42));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPanel.add(label);
        fieldPanel.add(inputField);
        parentPanel.add(fieldPanel);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new RoundedTextField(20);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15))
        );
        setPlaceholder(field, placeholder);
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new RoundedPasswordField(20);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15))
        );
        setPasswordPlaceholder(field, placeholder);
        return field;
    }

    private JButton createSaveButton() {
        JButton button = new RoundedButton("Save Changes", 35);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMinimumSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.addActionListener(e -> saveProfile());
        return button;
    }

    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    private void setPasswordPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setEchoChar((char) 0);
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                }
            }
        });
    }

    private void loadUserData() {
        User user = DatabaseUtil.getUser(currentUser);
        if (user != null) {
            jTextFieldUsername.setText(user.getUsername());
            jTextFieldUsername.setForeground(Color.BLACK);
            jTextFieldEmail.setText(user.getEmail());
            jTextFieldEmail.setForeground(Color.BLACK);
        }
    }

    private void saveProfile() {
        String newUsername = getFieldValue(jTextFieldUsername, USERNAME_PLACEHOLDER);
        String newEmail = getFieldValue(jTextFieldEmail, EMAIL_PLACEHOLDER);
        String newPassword = getPasswordValue(jPasswordField, PASSWORD_PLACEHOLDER);
        String confirmPassword = getPasswordValue(jPasswordFieldConfirm, CONFIRM_PASSWORD_PLACEHOLDER);

        // Validation
        if (newUsername.isEmpty()) {
            showError("Username cannot be empty");
            return;
        }

        if (newEmail.isEmpty()) {
            showError("Email cannot be empty");
            return;
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Update user if validation passes
        String finalPassword = newPassword.isEmpty() ? 
            DatabaseUtil.getUser(currentUser).getPassword() : newPassword;

        if (!newUsername.equals(currentUser) && DatabaseUtil.usernameExists(newUsername)) {
            showError("Username already exists");
            return;
        }

        if (!newEmail.equals(DatabaseUtil.getUser(currentUser).getEmail()) && 
            DatabaseUtil.emailExists(newEmail)) {
            showError("Email already exists");
            return;
        }

        if (DatabaseUtil.updateUser(currentUser, newUsername, newEmail, finalPassword)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.currentUser = newUsername;
            this.dispose();
            new Settings(newUsername).setVisible(true);
        } else {
            showError("Failed to update profile");
        }
    }

    private String getFieldValue(JTextField field, String placeholder) {
        String value = field.getText().trim();
        return value.equals(placeholder) ? "" : value;
    }

    private String getPasswordValue(JPasswordField field, String placeholder) {
        String value = new String(field.getPassword()).trim();
        return value.equals(placeholder) ? "" : value;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

