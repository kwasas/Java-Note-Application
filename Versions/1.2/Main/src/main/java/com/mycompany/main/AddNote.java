package com.mycompany.main;

import javax.swing.*;
import java.awt.*;

public class AddNote extends javax.swing.JFrame {
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JTextArea jTextAreaNote;
    private javax.swing.JScrollPane jScrollPaneNote;
    private javax.swing.JLabel jLabelTitle;

    private int noteIndex = -1;
    private String currentUser;

    public interface SaveListener {
        void onNoteSaved(String updatedNote, int noteIndex);
    }

    private SaveListener saveListener;

    public void setSaveListener(SaveListener listener) {
        this.saveListener = listener;
    }

    public AddNote(String username) {
        this.currentUser = username;
        initComponents();
        Main.setWindowSize(this, 380, 596);
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
        jLabelTitle = new JLabel(noteIndex == -1 ? "Add New Note" : "Edit Note");
        jLabelTitle.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jButtonCancel = new JButton("Back");
        jButtonCancel.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);

        jButtonSave = new JButton(noteIndex == -1 ? "Save" : "Update");
        jButtonSave.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);

        jTextAreaNote = new JTextArea(20, 50);
        jTextAreaNote.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTextAreaNote.setLineWrap(true);
        jTextAreaNote.setWrapStyleWord(true);

        jScrollPaneNote = new JScrollPane(jTextAreaNote);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(noteIndex == -1 ? "Add Note" : "Edit Note");

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
        String content = jTextAreaNote.getText().trim();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Note cannot be empty", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Attempting to save note for user: " + currentUser);
        System.out.println("Note content length: " + content.length());

        try {
            boolean success;
            if (noteIndex == -1) {
                System.out.println("Adding new note...");
                success = DatabaseUtil.addNote(currentUser, content);
            } else {
                System.out.println("Updating note at index: " + noteIndex);
                success = DatabaseUtil.updateNote(currentUser, noteIndex, content);
            }

            if (success) {
                System.out.println("Database operation successful");
                if (saveListener != null) {
                    saveListener.onNoteSaved(content, noteIndex);
                }
                JOptionPane.showMessageDialog(this,
                    noteIndex == -1 ? "Note saved successfully" : "Note updated successfully");
                this.dispose();
                new Home(currentUser).setVisible(true);
            } else {
                System.out.println("Database operation failed (returned false)");
                JOptionPane.showMessageDialog(this,
                    "Failed to save note. Check console for details.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR saving note:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Critical Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
