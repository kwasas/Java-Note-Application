package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends javax.swing.JFrame {
    private javax.swing.JButton jAddNoteButton;
    private javax.swing.JTextField jSearchField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jSettingsButton;
    private javax.swing.JList<String> jListNotes;
    private javax.swing.JScrollPane jScrollPaneNotes;
    private javax.swing.DefaultListModel<String> listModel;

    private String currentUser; // The currently logged-in user

    public Home(String username) {
        this.currentUser = username;
        initComponents();
        loadNotes();
        Main.setWindowSize(this, 320, 480); // Screen Size
        Main.centerWindow(this); // Center the window
    }

    private void initComponents() {
        jLabel1 = new JLabel("Home");
        jLabel1.setFont(new java.awt.Font("Arial", Font.BOLD, 18)); // Set font size and style
        jLabel1.setHorizontalAlignment(SwingConstants.LEFT);

        jSearchField = new JTextField(20);
        jSearchField.setBorder(BorderFactory.createTitledBorder("Search"));

        jSettingsButton = new JButton(new ImageIcon("./src/main/resources/assets/settings.png"));  // Placeholder for the settings icon
        jSettingsButton.setPreferredSize(new Dimension(100, 50)); // 100px wide, 50px tall
        jSettingsButton.setBorderPainted(false);
        jSettingsButton.setContentAreaFilled(false);
        jSettingsButton.addActionListener(evt -> {
            // Open the Settings screen
            this.dispose();
            new Settings().setVisible(true);
        });

        jAddNoteButton = new JButton(new ImageIcon("./src/main/resources/assets/addNoteButton.png")); // Placeholder for the add note button icon
        jAddNoteButton.setBorderPainted(false);
        jAddNoteButton.setContentAreaFilled(false);
        jAddNoteButton.addActionListener(evt -> {
            // Open the AddNote screen for creating a new note
            this.dispose();
            new AddNote(currentUser).setVisible(true);
        });

        jListNotes = new JList<>();
        jScrollPaneNotes = new JScrollPane(jListNotes);
        listModel = new DefaultListModel<>();
        jListNotes.setModel(listModel);
        jListNotes.addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting() && jListNotes.getSelectedIndex() != -1) {
                // Open the AddNote screen for editing the selected note
                int selectedIndex = jListNotes.getSelectedIndex();
                String selectedNote = listModel.getElementAt(selectedIndex);
                this.dispose();
                new AddNote(selectedNote, selectedIndex, currentUser).setVisible(true);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home");

        // Layout setup
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSettingsButton))
                .addComponent(jSearchField)
                .addComponent(jScrollPaneNotes, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSettingsButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSearchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneNotes, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void loadNotes() {
        // Clear existing notes
        listModel.clear();

        // Load notes from database with error handling
        try {
            List<String> notes = DatabaseUtil.getUserNotes(currentUser);
            if (notes != null) {
                for (String note : notes) {
                    if (note != null) {
                        listModel.addElement(note);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading notes: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}