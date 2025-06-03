package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends javax.swing.JFrame {
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JLabel jLabel0;
    private javax.swing.JLabel jForgotPassword;

    public SignIn() {
        initComponents();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabel0 = new JLabel("Sign In");
        jLabel0.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabel0.setHorizontalAlignment(SwingConstants.CENTER);

        // Use rounded text field
        jTextField1 = new RoundedTextField(20);
        jTextField1.setBackground(Color.WHITE);
        setPlaceholder(jTextField1, "Enter name or email");

        // Use rounded password field
        jPasswordField1 = new RoundedPasswordField(20);
        jPasswordField1.setBackground(Color.WHITE);
        setPasswordPlaceholder(jPasswordField1, "Enter password");

        jForgotPassword = new JLabel("Forgot password?");
        jForgotPassword.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jForgotPassword.setForeground(Color.BLACK);
        jForgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Use rounded button for sign in
        jButton1 = new RoundedButton("Sign In", 35);
        jButton1.setBackground(new Color(255, 204, 0));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        // Use rounded button for create account (transparent)
        jButton2 = new RoundedButton("Does not have an account?  Create account", 15);
        jButton2.setBackground(new Color(0, 0, 0, 0)); // Transparent
        jButton2.setForeground(Color.BLACK);
        jButton2.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jButton2.addActionListener(this::jButton2ActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign In");

        // Set frame background
        getContentPane().setBackground(Color.WHITE); // JFrame background color

        // Set fixed heights
        Dimension fieldSize = new Dimension(260, 40);
        jTextField1.setPreferredSize(fieldSize);
        jPasswordField1.setPreferredSize(fieldSize);
        jButton1.setPreferredSize(new Dimension(200, 40));

        // Layout setup
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabel0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                .addComponent(jForgotPassword)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addGap(100)
                .addComponent(jLabel0)
                .addGap(20)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGap(15)
                .addComponent(jForgotPassword)
                .addGap(20)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        if (username.equals("Enter name or email")) username = "";
        
        String password = new String(jPasswordField1.getPassword());
        if (password.equals("Enter password")) password = "";

        if (DatabaseUtil.validateUser(username, password)) {
            this.dispose();
            new Home(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        new SignUp().setVisible(true);
    }
}