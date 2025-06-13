package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgotPassword extends JFrame {
    private JTextField jTextFieldUsername;
    private JTextField jTextFieldEmail;
    private JPasswordField jPasswordFieldNew;
    private JPasswordField jPasswordFieldConfirm;
    private JButton jButtonReset;
    private JButton jButtonBack;
    private JLabel jLabelTitle;
    private JLabel jLabelBack;

    public ForgotPassword() {
        initComponents();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel);

        // ===== FIXED TOP PANEL (NOW PERFECTLY CENTERED) =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        
        // Back button (left-aligned)
        jButtonBack = new JButton(new ImageIcon("C:\\Users\\KennethWayneNAsas\\Documents\\GitHub\\Java-Note-Application\\Versions\\1.3.1\\Main\\src\\main\\resources\\assets\\arrow_back.png"));
        jButtonBack.setBorder(BorderFactory.createEmptyBorder());
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setFont(new Font("Arial", Font.PLAIN, 12));
        jButtonBack.setBorderPainted(false);
        jButtonBack.setForeground(new Color(0, 120, 215));
        jButtonBack.addActionListener(e -> {
            this.dispose();
            new SignIn().setVisible(true);
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // Don't expand
        gbc.anchor = GridBagConstraints.WEST; // Stick to left
        topPanel.add(jButtonBack, gbc);

        // Title label (centered)
        jLabelTitle = new JLabel("Reset Password", SwingConstants.CENTER);
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridx = 1;
        gbc.weightx = 1; // Take all remaining space
        gbc.anchor = GridBagConstraints.CENTER; // True center
        topPanel.add(jLabelTitle, gbc);

        // Empty component to balance the back button (keeps title centered)
        gbc.gridx = 2;
        gbc.weightx = 0; // Don't expand
        topPanel.add(Box.createRigidArea(new Dimension(jButtonBack.getPreferredSize().width, 1)), gbc);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL (UNCHANGED) =====
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Username field
        jTextFieldUsername = new RoundedTextField(20);
        setPlaceholder(jTextFieldUsername, "Enter your username");
        jTextFieldUsername.setMaximumSize(new Dimension(270, 42));
        centerPanel.add(jTextFieldUsername);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 7)));

        // Email field
        jTextFieldEmail = new RoundedTextField(20);
        setPlaceholder(jTextFieldEmail, "Enter your email");
        jTextFieldEmail.setMaximumSize(new Dimension(270, 42));
        centerPanel.add(jTextFieldEmail);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 7)));

        // New password field
        jPasswordFieldNew = new RoundedPasswordField(20);
        setPasswordPlaceholder(jPasswordFieldNew, "New Password");
        jPasswordFieldNew.setMaximumSize(new Dimension(270, 42));
        centerPanel.add(jPasswordFieldNew);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 7)));

        // Confirm password field
        jPasswordFieldConfirm = new RoundedPasswordField(20);
        setPasswordPlaceholder(jPasswordFieldConfirm, "Confirm Password");
        jPasswordFieldConfirm.setMaximumSize(new Dimension(270, 42));
        centerPanel.add(jPasswordFieldConfirm);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Reset button (centered)
        jButtonReset = new RoundedButton("Reset Password", 35);
        jButtonReset.setBackground(new Color(255, 204, 0));
        jButtonReset.setForeground(Color.WHITE);
        jButtonReset.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButtonReset.setMaximumSize(new Dimension(200, 45));
        jButtonReset.addActionListener(e -> resetPassword());
        centerPanel.add(jButtonReset);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        pack();
    }

    // ===== REST OF THE CODE REMAINS UNCHANGED =====
    private void resetPassword() {
        String username = jTextFieldUsername.getText().trim();
        String email = jTextFieldEmail.getText().trim();
        String newPass = new String(jPasswordFieldNew.getPassword());
        String confirmPass = new String(jPasswordFieldConfirm.getPassword());

        if (username.isEmpty() || email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!DatabaseUtil.verifyUserEmail(username, email)) {
            JOptionPane.showMessageDialog(this, "Username and email do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.updateUserPassword(username, newPass)) {
            JOptionPane.showMessageDialog(this, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new SignIn().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to reset password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void setPasswordPlaceholder(JPasswordField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setEchoChar((char)0);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char)0);
                    field.setText(placeholder);
                }
            }
        });
    }
}