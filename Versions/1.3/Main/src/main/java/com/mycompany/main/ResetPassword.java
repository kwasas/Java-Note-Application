package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ResetPassword extends javax.swing.JFrame {
    private JTextField jTextFieldToken;
    private JPasswordField jPasswordField;
    private JPasswordField jPasswordFieldConfirm;
    private JButton jButtonReset;
    private JLabel jLabelTitle;

    public ResetPassword() {
        initComponents();
        Main.setWindowSize(this, 400, 350);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Reset Password");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jTextFieldToken = new JTextField();
        jPasswordField = new JPasswordField();
        jPasswordFieldConfirm = new JPasswordField();

        jButtonReset = new JButton("Reset Password");
        jButtonReset.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonReset.setBackground(new Color(100, 150, 255));
        jButtonReset.setForeground(Color.WHITE);
        jButtonReset.addActionListener(this::jButtonResetActionPerformed);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Reset Password");

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.add(new JLabel("Reset Token:"));
        formPanel.add(jTextFieldToken);
        formPanel.add(new JLabel("New Password:"));
        formPanel.add(jPasswordField);
        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(jPasswordFieldConfirm);
        formPanel.add(new JLabel());
        formPanel.add(jButtonReset);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(jLabelTitle, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);

        pack();
    }

    private void jButtonResetActionPerformed(ActionEvent evt) {
        String token = jTextFieldToken.getText().trim();
        String password = new String(jPasswordField.getPassword());
        String confirmPassword = new String(jPasswordFieldConfirm.getPassword());

        if (token.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your reset token", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.resetPassword(token, password)) {
            JOptionPane.showMessageDialog(this, 
                "Password reset successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new SignIn().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid reset token", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}