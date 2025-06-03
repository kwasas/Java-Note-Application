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

    public ForgotPassword() {
        initComponents();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Reset Password");
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
            new SignIn().setVisible(true);
        });

        // Username field (matches SignUp style)
        jTextFieldUsername = new RoundedTextField(20);
        setPlaceholder(jTextFieldUsername, "Your Username");

        // Email field (matches SignUp style)
        jTextFieldEmail = new RoundedTextField(20);
        setPlaceholder(jTextFieldEmail, "Your Email");

        // New password field (matches SignUp style)
        jPasswordFieldNew = new RoundedPasswordField(20);
        setPasswordPlaceholder(jPasswordFieldNew, "New Password");

        // Confirm password field (matches SignUp style)
        jPasswordFieldConfirm = new RoundedPasswordField(20);
        setPasswordPlaceholder(jPasswordFieldConfirm, "Confirm Password");

        // Reset button (matches SignUp button style)
        jButtonReset = new RoundedButton("Reset Password", 35);
        jButtonReset.setBackground(new Color(255, 204, 0));
        jButtonReset.setForeground(Color.WHITE);
        jButtonReset.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonReset.addActionListener(e -> resetPassword());

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
                    .addComponent(jPasswordFieldNew, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldConfirm, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonReset, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jPasswordFieldNew, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jPasswordFieldConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(30)
                .addComponent(jButtonReset, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE)
        );

        pack();
    }

    private void resetPassword() {
        String username = jTextFieldUsername.getText().trim();
        String email = jTextFieldEmail.getText().trim();
        String newPass = new String(jPasswordFieldNew.getPassword());
        String confirmPass = new String(jPasswordFieldConfirm.getPassword());

        // Validation
        if (username.isEmpty() || email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verify username and email match
        if (!DatabaseUtil.verifyUserEmail(username, email)) {
            JOptionPane.showMessageDialog(this, "Username and email do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update password
        if (DatabaseUtil.updateUserPassword(username, newPass)) {
            JOptionPane.showMessageDialog(this, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new SignIn().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to reset password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add these helper methods (same as in SignUp)
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

