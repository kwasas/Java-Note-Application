package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.border.EmptyBorder;

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
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    public AddNote(Note note, String username) {
        this.currentUser = username;
        this.noteId = note.getId();
        initComponents();
        loadCategories();
        jTextAreaNote.setText(note.getContent());
        jComboBoxCategory.setSelectedItem(note.getCategory());
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(noteId == -1 ? "Add Note" : "Edit Note");
        getContentPane().setBackground(new Color(255, 255, 255));

        // Header panel with buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Back button with icon (safe loading)
        jButtonCancel = createIconButton("/assets/arrow_back.png", "Back");
        styleHeaderButton(jButtonCancel);
        jButtonCancel.addActionListener(e -> dispose());
        headerPanel.add(jButtonCancel, BorderLayout.WEST);
        
        // Title label (center)
        jLabelTitle = new JLabel(noteId == -1 ? "Add New Note" : "Edit Note");
        jLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        jLabelTitle.setForeground(Color.BLACK);
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(jLabelTitle, BorderLayout.CENTER);

        // Save button
        jButtonSave = new JButton(noteId == -1 ? "SAVE" : "UPDATE");
        styleHeaderButton(jButtonSave);
        jButtonSave.setForeground(new Color(255, 204, 0));
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);
        headerPanel.add(jButtonSave, BorderLayout.EAST);

        // Note content area
        jTextAreaNote = new JTextArea();
        jTextAreaNote.setFont(new Font("Arial", Font.PLAIN, 16));
        jTextAreaNote.setLineWrap(true);
        jTextAreaNote.setWrapStyleWord(true);
        jTextAreaNote.setBorder(new EmptyBorder(10, 10, 10, 10));

        jScrollPaneNote = new JScrollPane(jTextAreaNote);
        jScrollPaneNote.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            new EmptyBorder(5, 5, 5, 5)
        ));

        // Category components
        JPanel categoryPanel = new JPanel(new BorderLayout(10, 0));
        categoryPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        categoryPanel.setBackground(Color.WHITE);

        jComboBoxCategory = new JComboBox<>();
        jComboBoxCategory.setEditable(true);
        jComboBoxCategory.setFont(new Font("Arial", Font.PLAIN, 14));
        jComboBoxCategory.setBackground(Color.WHITE);
        jComboBoxCategory.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));

        jButtonNewCategory = new JButton("+ New Category");
        styleButton(jButtonNewCategory, new Color(52, 152, 219));
        jButtonNewCategory.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(this, "Enter new category name:");
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                jComboBoxCategory.addItem(newCategory.trim());
                jComboBoxCategory.setSelectedItem(newCategory.trim());
            }
        });

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));

        categoryPanel.add(categoryLabel, BorderLayout.WEST);
        categoryPanel.add(jComboBoxCategory, BorderLayout.CENTER);
        categoryPanel.add(jButtonNewCategory, BorderLayout.EAST);

        // Main layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(categoryPanel, BorderLayout.NORTH);
        contentPanel.add(jScrollPaneNote, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        pack();
    }
    
    // Safe icon button creation
    private JButton createIconButton(String iconPath, String fallbackText) {
        JButton button = new JButton();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            button.setIcon(icon);
            button.setText(""); // Clear text if icon loads
        } catch (Exception e) {
            // If icon fails to load, use text instead
            button.setText(fallbackText);
            System.err.println("Failed to load icon: " + iconPath);
        }
        return button;
    }

    // Style for header buttons (back/save)
    private void styleHeaderButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(null);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(5, 5, 5, 5));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void loadCategories() {
        jComboBoxCategory.removeAllItems();
        List<String> categories = DatabaseUtil.getCategories(currentUser);
        if (categories != null) {
            for (String category : categories) {
                if (category != null && !category.isEmpty()) {
                    jComboBoxCategory.addItem(category);
                }
            }
        }
    }

    private void jButtonSaveActionPerformed(ActionEvent evt) {
        String content = jTextAreaNote.getText().trim();
        String category = (String) jComboBoxCategory.getSelectedItem();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Note content cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (category == null || category.trim().isEmpty()) {
            category = "Uncategorized";
        }

        try {
            boolean success;
            Note note;

            if (noteId == -1) {
                note = DatabaseUtil.addAndReturnNote(currentUser, content, category);
                success = (note != null);
            } else {
                note = new Note(noteId, content, category);
                success = DatabaseUtil.updateNote(currentUser, note);
            }

            if (success) {
                if (saveListener != null) {
                    saveListener.onNoteSaved(note);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to save note. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Save Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}