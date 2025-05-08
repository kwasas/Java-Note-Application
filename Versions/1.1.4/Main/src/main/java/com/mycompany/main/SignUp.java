package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUp extends javax.swing.JFrame {
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JLabel jLabel0;
    private javax.swing.JCheckBox jCheckBox1;

    public SignUp() {
        initComponents();
        Main.setWindowSize(this, 320, 480); // Screen Size
        Main.centerWindow(this); // Center the window
    }

    private void initComponents() {
        jLabel0 = new JLabel("Create an Account");
        jLabel0.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabel0.setHorizontalAlignment(SwingConstants.CENTER);

        // Username field with placeholder
        jTextField1 = new JTextField(20);
        setPlaceholder(jTextField1, "Your Username");

        // Email field with placeholder
        jTextField2 = new JTextField(20);
        setPlaceholder(jTextField2, "Enter Email");

        // Password fields with placeholders
        jPasswordField1 = new JPasswordField(20);
        setPasswordPlaceholder(jPasswordField1, "Enter Password");

        jPasswordField2 = new JPasswordField(20);
        setPasswordPlaceholder(jPasswordField2, "Repeat Password");

        jCheckBox1 = new JCheckBox("I agree with Lemon Note Terms and Conditions.");
        jCheckBox1.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));

        jButton1 = new JButton("Create Account");
        jButton1.setBackground(new Color(255, 204, 0));
        jButton1.setForeground(Color.BLACK);
        jButton1.setFocusPainted(false);
        jButton1.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2 = new JButton("Log In");
        jButton2.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jButton2.setForeground(Color.BLUE);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign Up");

        // Layout setup (unchanged from your original)
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabel0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1)
                .addComponent(jTextField2)
                .addComponent(jPasswordField1)
                .addComponent(jPasswordField2)
                .addComponent(jCheckBox1)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel0)
                .addGap(20)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(jCheckBox1)
                .addGap(20)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    // Helper method for text field placeholders
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

    // Helper method for password field placeholders
    private void setPasswordPlaceholder(JPasswordField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setEchoChar((char)0); // Show actual text
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•'); // Show password bullets
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

    // Modified action handler to handle placeholders
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String username = jTextField1.getText().trim();
        if (username.equals("Your Username")) username = "";
        
        String email = jTextField2.getText().trim();
        if (email.equals("Enter Email")) email = "";
        
        String password = new String(jPasswordField1.getPassword());
        if (password.equals("Enter Password")) password = "";
        
        String confirmPassword = new String(jPasswordField2.getPassword());
        if (confirmPassword.equals("Repeat Password")) confirmPassword = "";

        // Rest of your validation logic remains the same...
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!jCheckBox1.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "You must agree to the Terms and Conditions", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.addUser(username, email, password)) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new SignIn().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to create account. Please try again.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Rest of your existing methods remain unchanged...
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        new SignIn().setVisible(true);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}