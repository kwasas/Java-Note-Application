package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings extends javax.swing.JFrame {
    private JButton jButtonSignOut;
    private JButton jButtonEditProfile;
    private JLabel jLabelTitle;
    private JButton jButtonBack;
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

        // Back button - you can replace this with a JPEG image later
        jButtonBack = new JButton(new ImageIcon("C:\\Users\\KennethWayneNAsas\\Documents\\GitHub\\Java-Note-Application\\Versions\\1.3.1\\Main\\src\\main\\resources\\assets\\arrow_back.png"));
        jButtonBack.setBorder(BorderFactory.createEmptyBorder());
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setFont(new Font("Arial", Font.BOLD, 20));
        jButtonBack.setContentAreaFilled(false);
        jButtonBack.setBorderPainted(false);
        jButtonBack.setFocusPainted(false);
        jButtonBack.addActionListener(this::jButtonBackActionPerformed);
        // ... [keep all existing setup code until button creation]

        // Sign Out button (matches SignUp button style)
        jButtonSignOut = new RoundedButton("Sign Out", 35);
        jButtonSignOut.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonSignOut.setBackground(new Color(255, 204, 0));
        jButtonSignOut.setForeground(Color.WHITE);
        jButtonSignOut.setFocusPainted(false);
        jButtonSignOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonSignOut.addActionListener(this::jButtonSignOutActionPerformed);

        // Edit Profile button (matches SignUp button style but with different color)
        jButtonEditProfile = new RoundedButton("Profile", 35);
        jButtonEditProfile.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonEditProfile.setBackground(new Color(100, 150, 255));
        jButtonEditProfile.setForeground(Color.WHITE);
        jButtonEditProfile.setFocusPainted(false);
        jButtonEditProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonEditProfile.addActionListener(this::jButtonEditProfileActionPerformed);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButtonBack)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                        .addComponent(jButtonEditProfile, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(jButtonSignOut, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGap(50, 50, 50))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap(20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(jButtonBack)
                    .addComponent(jLabelTitle))
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

    private void jButtonBackActionPerformed(ActionEvent evt) {
        this.dispose();
        new Home(currentUser).setVisible(true);
    }
}