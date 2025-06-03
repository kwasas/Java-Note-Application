package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings extends javax.swing.JFrame {
    private JButton jButtonSignOut;
    private JButton jButtonEditProfile;
    private JLabel jLabelTitle;
    private String currentUser;

    public Settings(String username) {
        this.currentUser = username;
        initComponents();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Settings");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 24));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jButtonSignOut = new JButton("Sign Out");
        jButtonSignOut.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonSignOut.setBackground(new Color(255, 204, 0));
        jButtonSignOut.setFocusPainted(false);
        jButtonSignOut.addActionListener(this::jButtonSignOutActionPerformed);

        jButtonEditProfile = new JButton("Edit Profile");
        jButtonEditProfile.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonEditProfile.setBackground(new Color(100, 150, 255));
        jButtonEditProfile.setForeground(Color.WHITE);
        jButtonEditProfile.setFocusPainted(false);
        jButtonEditProfile.addActionListener(this::jButtonEditProfileActionPerformed);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                        .addComponent(jButtonEditProfile, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(jButtonSignOut, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGap(50, 50, 50))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap(50, 50)
                .addComponent(jLabelTitle)
                .addGap(50)
                .addComponent(jButtonEditProfile, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addComponent(jButtonSignOut, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
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
}