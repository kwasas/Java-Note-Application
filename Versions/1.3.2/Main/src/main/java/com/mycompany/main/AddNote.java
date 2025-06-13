package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(noteId == -1 ? "Add Note" : "Edit Note");

        jLabelTitle = new JLabel(noteId == -1 ? "Add New Note" : "Edit Note");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        jButtonCancel = new JButton("Back");
        jButtonCancel.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);

        jButtonSave = new JButton(noteId == -1 ? "Save" : "Update");
        jButtonSave.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);

        jTextAreaNote = new JTextArea();
        jTextAreaNote.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextAreaNote.setLineWrap(true);
        jTextAreaNote.setWrapStyleWord(true);

        jScrollPaneNote = new JScrollPane(jTextAreaNote);
        jScrollPaneNote.setBorder(BorderFactory.createEmptyBorder());

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

        JPanel categoryPanel = new JPanel(new BorderLayout(5, 0));
        categoryPanel.add(new JLabel("Category:"), BorderLayout.WEST);
        categoryPanel.add(jComboBoxCategory, BorderLayout.CENTER);
        categoryPanel.add(jButtonNewCategory, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(jButtonCancel);
        buttonPanel.add(jButtonSave);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(categoryPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPaneNote)
                        .addComponent(buttonPanel, GroupLayout.Alignment.CENTER))
                    .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addGap(15)
                .addComponent(categoryPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jScrollPaneNote, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(15)
                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addContainerGap());
      

        pack();
    }

    private void loadCategories() {
        jComboBoxCategory.removeAllItems();
        jComboBoxCategory.addItem(""); // Empty category
        List<String> categories = DatabaseUtil.getCategories(currentUser);
        if (categories != null) {
            for (String category : categories) {
                if (category != null && !category.isEmpty()) {
                    jComboBoxCategory.addItem(category);
                }
            }
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
            "Note content cannot be empty", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        Note note;
        boolean success;

        if (noteId == -1) {
            // Create new note
            note = new Note(content, category);
            success = DatabaseUtil.addNote(currentUser, note);
            
            if (success) {
                // Get the saved note with ID from database
                List<Note> notes = DatabaseUtil.getUserNotes(currentUser);
                Note savedNote = findNewlyCreatedNote(notes, content, category);
                if (savedNote != null) {
                    note = savedNote;
                }
                
                JOptionPane.showMessageDialog(this,
                    "Note saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Update existing note
            note = new Note(noteId, content, category);
            success = DatabaseUtil.updateNote(currentUser, note);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Note updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (success) {
            if (saveListener != null) {
                saveListener.onNoteSaved(note);
            }
            this.dispose();
            new Home(currentUser).setVisible(true); // Navigate back to Home
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to save note to database", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Database error: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private Note findNewlyCreatedNote(List<Note> notes, String content, String category) {
        if (notes == null) return null;
        
        for (Note note : notes) {
            if (note.getContent().equals(content)) {
                if (category == null || category.isEmpty()) {
                    if (note.getCategory() == null) {
                        return note;
                    }
                } else {
                    if (category.equals(note.getCategory())) {
                        return note;
                    }
                }
            }
        }
        return null;
    }
}