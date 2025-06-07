package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddNote extends javax.swing.JFrame {
    private JButton jButtonCancel;
    private JButton jButtonSave;
    private JTextArea jTextAreaNote;
    private JScrollPane jScrollPaneNote;
    private JLabel jLabelTitle;
    private JComboBox<String> jComboBoxCategory;
    private JButton jButtonNewCategory;
    private int noteId = -1;
    private String currentUser;

    public interface SaveListener {
        void onNoteSaved(Note note);
    }

    private SaveListener saveListener;

    public void setSaveListener(SaveListener listener) {
        this.saveListener = listener;
    }

    public AddNote(String username) {
        this.currentUser = username;
        initComponents();
        loadCategories();
        Main.setWindowSize(this, 400, 500);
        Main.centerWindow(this);
    }

    public AddNote(Note note, String username) {
        this.currentUser = username;
        this.noteId = note.getId();
        initComponents();
        loadCategories();
        jTextAreaNote.setText(note.getContent());
        jComboBoxCategory.setSelectedItem(note.getCategory());
        Main.setWindowSize(this, 400, 500);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabelTitle = new JLabel(noteId == -1 ? "Add New Note" : "Edit Note");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jButtonCancel = new JButton("Back");
        jButtonCancel.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);

        jButtonSave = new JButton(noteId == -1 ? "Save" : "Update");
        jButtonSave.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);

        jTextAreaNote = new JTextArea(20, 50);
        jTextAreaNote.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextAreaNote.setLineWrap(true);
        jTextAreaNote.setWrapStyleWord(true);

        jScrollPaneNote = new JScrollPane(jTextAreaNote);

        jComboBoxCategory = new JComboBox<>();
        jComboBoxCategory.setEditable(true);
        jComboBoxCategory.setFont(new Font("Arial", Font.PLAIN, 14));

        jButtonNewCategory = new JButton("+");
        jButtonNewCategory.setFont(new Font("Arial", Font.BOLD, 12));
        jButtonNewCategory.setPreferredSize(new Dimension(30, 30));
        jButtonNewCategory.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(this, "Enter new category name:");
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                jComboBoxCategory.addItem(newCategory.trim());
                jComboBoxCategory.setSelectedItem(newCategory.trim());
            }
        });

        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.add(new JLabel("Category:"), BorderLayout.WEST);
        categoryPanel.add(jComboBoxCategory, BorderLayout.CENTER);
        categoryPanel.add(jButtonNewCategory, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(noteId == -1 ? "Add Note" : "Edit Note");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(categoryPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(categoryPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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

    private void loadCategories() {
        jComboBoxCategory.removeAllItems();
        jComboBoxCategory.addItem(""); // Empty category
        for (String category : DatabaseUtil.getCategories(currentUser)) {
            jComboBoxCategory.addItem(category);
        }
    }

    private void jButtonCancelActionPerformed(ActionEvent evt) {
        this.dispose();
        new Home(currentUser).setVisible(true);
    }

    private void jButtonSaveActionPerformed(ActionEvent evt) {
        String content = jTextAreaNote.getText().trim();
        String category = (String) jComboBoxCategory.getSelectedItem();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Note cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success;
            Note note;

            if (noteId == -1) {
                success = DatabaseUtil.addNote(currentUser, content, category);
                note = new Note(-1, content, category);
            } else {
                success = DatabaseUtil.updateNote(currentUser, noteId, content, category);
                note = new Note(noteId, content, category);
            }

            if (success) {
                if (saveListener != null) {
                    saveListener.onNoteSaved(note);
                }
                JOptionPane.showMessageDialog(this,
                    noteId == -1 ? "Note saved successfully" : "Note updated successfully");
                this.dispose();
                new Home(currentUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to save note.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Critical Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}