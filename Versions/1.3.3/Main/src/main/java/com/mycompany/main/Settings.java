package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings extends javax.swing.JFrame {
    private JButton jButtonSignOut;
    private JButton jButtonEditProfile;
    private JButton jButtonDeleteAccount; // New delete account button
    private JLabel jLabelTitle;
    private JButton jButtonBack;
    private String currentUser;

    public Settings(String username) {
        this.currentUser = username;
        initComponents();
        Main.setWindowSize(this, 380, 596); // Increased height to accommodate new button
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Settings");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Back button with arrow icon
        jButtonBack = new JButton(new ImageIcon(getClass().getResource("/assets/arrow_back.png")));
        jButtonBack.setBorder(BorderFactory.createEmptyBorder());
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setFont(new Font("Arial", Font.BOLD, 20));
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setBorderPainted(false);
        jButtonBack.setFocusPainted(false);
        jButtonBack.addActionListener(this::jButtonBackActionPerformed);

        // Sign Out button (yellow)
        jButtonSignOut = new RoundedButton("Sign Out", 35);
        jButtonSignOut.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonSignOut.setBackground(new Color(255, 204, 0)); // Yellow
        jButtonSignOut.setForeground(Color.WHITE);
        jButtonSignOut.setFocusPainted(false);
        jButtonSignOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonSignOut.addActionListener(this::jButtonSignOutActionPerformed);

        // Edit Profile button (blue)
        jButtonEditProfile = new RoundedButton("Profile", 35);
        jButtonEditProfile.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonEditProfile.setBackground(new Color(100, 150, 255)); // Blue
        jButtonEditProfile.setForeground(Color.WHITE);
        jButtonEditProfile.setFocusPainted(false);
        jButtonEditProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonEditProfile.addActionListener(this::jButtonEditProfileActionPerformed);

        // Delete Account button (red) - NEW
        jButtonDeleteAccount = new RoundedButton("Delete Account", 35);
        jButtonDeleteAccount.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonDeleteAccount.setBackground(new Color(255, 80, 80)); // Red
        jButtonDeleteAccount.setForeground(Color.WHITE);
        jButtonDeleteAccount.setFocusPainted(false);
        jButtonDeleteAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonDeleteAccount.addActionListener(this::jButtonDeleteAccountActionPerformed);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        // Horizontal layout - updated to include new button
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(20,20)
                    .addComponent(jButtonBack)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap(20,40))
                .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                        .addComponent(jButtonEditProfile, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(jButtonDeleteAccount, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE) // Order doesn't matter here
                        .addComponent(jButtonSignOut, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGap(50, 50, 50))
        );

        // Vertical layout - updated to include new button
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap(20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(jButtonBack)
                    .addComponent(jLabelTitle))
                .addGap(50)
                .addComponent(jButtonEditProfile, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addComponent(jButtonDeleteAccount, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE) // Moved up
                .addGap(20)
                .addComponent(jButtonSignOut, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE) // Moved down
                .addContainerGap(50, Short.MAX_VALUE)
        );

        pack();
    }

    private void jButtonSignOutActionPerformed(ActionEvent evt) {
        this.dispose();
        new SignIn().setVisible(true);
    }

    private void jButtonEditProfileActionPerformed(ActionEvent evt) {
        this.dispose();
        new EditProfile(currentUser).setVisible(true);
    }

    private void jButtonBackActionPerformed(ActionEvent evt) {
        this.dispose();
        new Home(currentUser).setVisible(true);
    }

    // NEW METHOD: Handle account deletion
    private void jButtonDeleteAccountActionPerformed(ActionEvent evt) {
        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete your account?\nThis action cannot be undone!",
            "Confirm Account Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                
// First delete all user's notes
                                // Then delete the user account
                boolean accountDeleted = DatabaseUtil.deleteUserAccount(currentUser);
                
                if (accountDeleted) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Account deleted successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    this.dispose();
                    new SignIn().setVisible(true); // Return to sign in screen
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete account",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error deleting account: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }
}