package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;
import java.awt.geom.RoundRectangle2D;

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
        Main.setWindowSize(this, 380, 596); // Screen Size
        Main.centerWindow(this); // Center the window
    }

    private void initComponents() {
        jLabel0 = new JLabel("Create Account");
        jLabel0.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabel0.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Username field with placeholder
        jTextField1 = new RoundedTextField(20);
        setPlaceholder(jTextField1, "Your Username");
        
        // Email field with placeholder
        jTextField2 = new RoundedTextField(20);
        setPlaceholder(jTextField2, "Enter Email");

        // Password fields with placeholders
        jPasswordField1 = new RoundedPasswordField(20);
        setPasswordPlaceholder(jPasswordField1, "Enter Password");

        jPasswordField2 = new RoundedPasswordField(20);
        setPasswordPlaceholder(jPasswordField2, "Repeat Password");

        jCheckBox1 = new JCheckBox("I agree with Terms and Conditions.");
        jCheckBox1.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jCheckBox1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jCheckBox1.setBackground(Color.WHITE);


        jButton1 = new RoundedButton("Create Account", 35);
        jButton1.setBackground(new Color(255, 204, 0));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFocusPainted(false);
        jButton1.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2 = new JButton("<html>Already have an account?  <font color='#FFCC00'>Log In</font</html>");
        jButton2.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jButton2.setForeground(Color.BLACK);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton2.addActionListener(this::jButton2ActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign Up");        
                
        // Standard height for all input fields
        final int FIELD_HEIGHT = 42;

        // Set fixed height for all text components
        Dimension fixedHeight = new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT); // Width flexible, height fixed

        jTextField1.setPreferredSize(fixedHeight);
        jTextField1.setMinimumSize(fixedHeight);
        jTextField1.setMaximumSize(fixedHeight);

        jTextField2.setPreferredSize(fixedHeight);
        jTextField2.setMinimumSize(fixedHeight);
        jTextField2.setMaximumSize(fixedHeight);

        jPasswordField1.setPreferredSize(fixedHeight);
        jPasswordField1.setMinimumSize(fixedHeight);
        jPasswordField1.setMaximumSize(fixedHeight);

        jPasswordField2.setPreferredSize(fixedHeight);
        jPasswordField2.setMinimumSize(fixedHeight);
        jPasswordField2.setMaximumSize(fixedHeight);

        // For buttons (if needed)
        jButton1.setPreferredSize(new Dimension(jButton1.getPreferredSize().width, 40));
        jButton1.setMinimumSize(new Dimension(jButton1.getMinimumSize().width, 40));
        jButton1.setMaximumSize(new Dimension(jButton1.getMaximumSize().width, 40));
        
        // Layout setup with added padding
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE); // JFrame background color

        // Changed: Added horizontal padding (20px on each side) by using createSequentialGroup()
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(15) // Left padding
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField2, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                .addGap(20) // Right padding
                ));

        // Changed: Added vertical padding at top and bottom
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(100) // Top padding
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
                .addGap(20) // Bottom padding
        );

        pack();
    }

    // Rest of your methods remain unchanged...
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String username = jTextField1.getText().trim();
        if (username.equals("Your Username")) username = "";
        
        String email = jTextField2.getText().trim();
        if (email.equals("Enter Email")) email = "";
        
        String password = new String(jPasswordField1.getPassword());
        if (password.equals("Enter Password")) password = "";
        
        String confirmPassword = new String(jPasswordField2.getPassword());
        if (confirmPassword.equals("Repeat Password")) confirmPassword = "";

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