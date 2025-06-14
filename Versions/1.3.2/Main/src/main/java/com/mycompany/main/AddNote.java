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
        getContentPane().setBackground(Color.WHITE);

        // Header panel with buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Back button (upper left) - can be replaced with an image later
        jButtonCancel = new JButton("Back");
        jButtonCancel.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonCancel.setContentAreaFilled(false); // Makes button transparent
        jButtonCancel.addActionListener(this::jButtonCancelActionPerformed);
        headerPanel.add(jButtonCancel, BorderLayout.WEST);

        // Title label (center)
        jLabelTitle = new JLabel(noteId == -1 ? "Add New Note" : "Edit Note");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(jLabelTitle, BorderLayout.CENTER);

        // Save button (upper right) - can be replaced with an image later
        jButtonSave = new JButton(noteId == -1 ? "Save" : "Update");
        jButtonSave.setFont(new Font("Arial", Font.PLAIN, 14));
        jButtonSave.setContentAreaFilled(false); // Makes button transparent
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);
        headerPanel.add(jButtonSave, BorderLayout.EAST);

        // Note content area
        jTextAreaNote = new JTextArea();
        jTextAreaNote.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextAreaNote.setLineWrap(true);
        jTextAreaNote.setWrapStyleWord(true);
        jTextAreaNote.setBackground(Color.WHITE);

        jScrollPaneNote = new JScrollPane(jTextAreaNote);
        jScrollPaneNote.setBorder(BorderFactory.createEmptyBorder());
        jScrollPaneNote.setBackground(Color.WHITE);

        // Category components
        jComboBoxCategory = new JComboBox<>();
        jComboBoxCategory.setEditable(true);
        jComboBoxCategory.setFont(new Font("Arial", Font.PLAIN, 14));
        jComboBoxCategory.setBackground(Color.WHITE);

        jButtonNewCategory = new JButton("+");
        jButtonNewCategory.setFont(new Font("Arial", Font.BOLD, 12));
        jButtonNewCategory.setPreferredSize(new Dimension(30, 30));
        jButtonNewCategory.setContentAreaFilled(false); // Makes button transparent
        jButtonNewCategory.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(this, "Enter new category name:");
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                jComboBoxCategory.addItem(newCategory.trim());
                jComboBoxCategory.setSelectedItem(newCategory.trim());
            }
        });

        JPanel categoryPanel = new JPanel(new BorderLayout(5, 0));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.add(new JLabel("Category:"), BorderLayout.WEST);
        categoryPanel.add(jComboBoxCategory, BorderLayout.CENTER);
        categoryPanel.add(jButtonNewCategory, BorderLayout.EAST);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(categoryPanel, BorderLayout.CENTER);
        mainPanel.add(jScrollPaneNote, BorderLayout.SOUTH);

        // Adjust the layout to give more space to the text area
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.NORTH);
        getContentPane().add(jScrollPaneNote, BorderLayout.CENTER);

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
                note = new Note(content, category);
                success = DatabaseUtil.addNote(currentUser, note);
                
                if (success) {
                    if (saveListener != null) {
                        saveListener.onNoteSaved(note);
                    }
                    this.dispose();
                }
            } else {
                note = new Note(noteId, content, category);
                success = DatabaseUtil.updateNote(currentUser, note);
                
                if (success) {
                    if (saveListener != null) {
                        saveListener.onNoteSaved(note);
                    }
                    this.dispose();
                }
            }

            if (!success) {
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
}