package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddNote extends javax.swing.JFrame {
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JTextArea jTextAreaNote;
    private javax.swing.JScrollPane jScrollPaneNote;
    private javax.swing.JLabel jLabelTitle;

    private int noteIndex = -1; // To track the index of the note being edited
    private String currentUser; // The currently logged-in user

    public interface SaveListener {
        void onNoteSaved(String updatedNote, int noteIndex);
    }
    
    private SaveListener saveListener;
    
    public void setSaveListener(SaveListener listener) {
        this.saveListener = listener;
    }
    
    private void saveNote() {
        String noteContent = jTextAreaNote.getText(); // Changed from jTextArea1 to jTextAreaNote
        if (noteIndex == -1) {
            // New note
            if (DatabaseUtil.addNote(currentUser, noteContent)) {
                if (saveListener != null) {
                    saveListener.onNoteSaved(noteContent, -1);
                }
                JOptionPane.showMessageDialog(this, "Note saved successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save note", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Existing note
            if (DatabaseUtil.updateNote(currentUser, noteIndex, noteContent)) {
                if (saveListener != null) {
                    saveListener.onNoteSaved(noteContent, noteIndex);
                }
                JOptionPane.showMessageDialog(this, "Note updated successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update note", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public AddNote(String username) {
        this.currentUser = username;
        initComponents();
        Main.setWindowSize(this, 320, 480);
        Main.centerWindow(this);
    }

    public AddNote(String noteContent, int index, String username) {
        this.noteIndex = index;
        this.currentUser = username;
        initComponents();
        Main.setWindowSize(this, 320, 480);
        Main.centerWindow(this);
        jTextAreaNote.setText(noteContent);
    }

    private void initComponents() {
        jLabelTitle = new JLabel("Add Note");
        jLabelTitle.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jButtonCancel = new JButton("Back");
        jButtonCancel.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);

        jButtonSave = new JButton("Save");
        jButtonSave.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);

        jTextAreaNote = new JTextArea(20, 50);
        jTextAreaNote.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTextAreaNote.setLineWrap(true);
        jTextAreaNote.setWrapStyleWord(true);

        jScrollPaneNote = new JScrollPane(jTextAreaNote);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add Note");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPaneNote)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jButtonCancel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonSave, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneNote, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                .addContainerGap()
        );

        pack();
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        new Home(currentUser).setVisible(true);
    }

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {
        String content = jTextAreaNote.getText();
        if (!content.isEmpty()) {
            if (noteIndex >= 0) {
                if (DatabaseUtil.updateNote(currentUser, noteIndex, content)) {
                    this.dispose();
                    new Home(currentUser).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update note", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (DatabaseUtil.addNote(currentUser, content)) {
                    this.dispose();
                    new Home(currentUser).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save note", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Note cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}