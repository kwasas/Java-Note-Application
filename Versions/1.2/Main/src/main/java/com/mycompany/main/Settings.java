package com.mycompany.main;

import javax.swing.*;
import java.awt.*;

public class Settings extends javax.swing.JFrame {
    private javax.swing.JButton jButtonSignOut;
    private javax.swing.JLabel jLabelTitle;

    public Settings() {
        initComponents();
        Main.setWindowSize(this, 380, 596); // Screen Size
        Main.centerWindow(this); // Center the window
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Settings");
        jLabelTitle.setFont(new java.awt.Font("Arial", Font.BOLD, 18)); // Set font size and style
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jButtonSignOut = new JButton("Sign Out");
        jButtonSignOut.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jButtonSignOut.setBackground(new Color(255, 204, 0)); // Yellow background
        jButtonSignOut.setFocusPainted(false);
        jButtonSignOut.addActionListener(_ -> {
            // Navigate back to the SignIn screen
            this.dispose();
            new SignIn().setVisible(true);
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Settings");

        // Layout setup
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(100, 100, 100)
                    .addComponent(jButtonSignOut, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                    .addGap(100, 100, 100))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addGap(50)
                .addComponent(jButtonSignOut, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
}