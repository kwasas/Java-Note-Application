package com.mycompany.main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Home extends javax.swing.JFrame {
    private JTextField jSearchField;
    private JLabel jTitleLabel;
    private JList<Note> jNotesList;
    private JScrollPane jScrollPane;
    private DefaultListModel<Note> listModel;
    private JComboBox<String> jCategoryFilter;
    private String currentUser;
    private int swipeStartX;
    private JButton addButton;

    public Home(String username) {
        this.currentUser = username;
        initComponents();
        loadNotes();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
        setupFloatingButton();
    }

    class RoundedTextField extends JTextField {
        private int cornerRadius = 20;

        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }

    private void setupFloatingButton() {
        addButton = new JButton(new ImageIcon(getClass().getResource("/assets/write.png")));
        styleFloatingButton(addButton);
        addButton.addActionListener(e -> addNewNote());
        
        // Use layered pane for proper z-ordering
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(addButton, JLayeredPane.POPUP_LAYER);
        
        positionFloatingButton();
        
        // Handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionFloatingButton();
            }
        });
    }

    private void positionFloatingButton() {
        int buttonSize = 70; // Size of the button
        int rightMargin = 30; // Distance from right edge
        int bottomMargin = 50; // Distance from bottom edge

        // Adjust these values to move the button:
        int xPosition = getWidth() - buttonSize - rightMargin;
        int yPosition = getHeight() - buttonSize - bottomMargin;

        addButton.setBounds(xPosition, yPosition, buttonSize, buttonSize);
    }

    private void styleFloatingButton(JButton button) {
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(new Color(255, 255, 255)); 
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Make it circular
        button.setPreferredSize(new Dimension(56, 56));
        button.setMaximumSize(new Dimension(56, 56));
        button.setMinimumSize(new Dimension(56, 56));
        
        // Shadow effect
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Ensure the icon is visible
        button.setIcon(new ImageIcon(getClass().getResource("/assets/write.png")));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Home - " + currentUser);
        getContentPane().setBackground(new Color(255, 255, 255));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        titlePanel.setBackground(new Color(255, 255, 255));

        jTitleLabel = new JLabel("Welcome, " + currentUser);
        jTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton settingsButton = new JButton(new ImageIcon(getClass().getResource("/assets/profile-circle.png")));
        styleButton(settingsButton, e -> openSettings());

        buttonPanel.add(settingsButton);

        titlePanel.add(jTitleLabel, BorderLayout.WEST);
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        // Search Field
        jSearchField = new RoundedTextField(20);
        jSearchField.setForeground(new Color(150, 150, 150));
        jSearchField.setFont(new Font("Arial", Font.PLAIN, 14));
        jSearchField.setBackground(new Color(211, 211, 211));
        jSearchField.setText(" Search");
        jSearchField.setForeground(new Color(150, 150, 150));

        jSearchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jSearchField.getText().equals(" Search")) {
                    jSearchField.setText("");
                    jSearchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jSearchField.getText().isEmpty()) {
                    jSearchField.setForeground(new Color(150, 150, 150));
                    jSearchField.setText(" Search");
                }
            }
        });
        jSearchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterNotes();
            }
        });

        jCategoryFilter = new JComboBox<>();
        jCategoryFilter.addItem("⇵");
        jCategoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        jCategoryFilter.addActionListener(e -> filterNotes());

        JPanel filterPanel = new JPanel(new BorderLayout(10, 0));
        filterPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        filterPanel.add(jSearchField, BorderLayout.CENTER);
        filterPanel.add(jCategoryFilter, BorderLayout.EAST);

        // Notes List
        listModel = new DefaultListModel<>();
        jNotesList = new JList<>(listModel);
        jNotesList.setBackground(Color.WHITE);
        jNotesList.setFont(new Font("Arial", Font.PLAIN, 16));
        jNotesList.setFixedCellHeight(60);
        jNotesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jNotesList.setCellRenderer(new NoteListCellRenderer());

        jNotesList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                swipeStartX = e.getX();
            }

            public void mouseReleased(MouseEvent e) {
                int index = jNotesList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Rectangle cellBounds = jNotesList.getCellBounds(index, index);
                    if (cellBounds.contains(e.getPoint())) {
                        if (swipeStartX - e.getX() > 50) {
                            Note selectedNote = jNotesList.getModel().getElementAt(index);
                            confirmAndDeleteNote(selectedNote);
                        } else {
                            Note selectedNote = jNotesList.getModel().getElementAt(index);
                            openNoteForEditing(selectedNote);
                        }
                    }
                }
            }
        });

        jScrollPane = new JScrollPane(jNotesList);
        jScrollPane.setBorder(null);

        // Main Layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10)
                        .addComponent(jSearchField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10))
                    .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jSearchField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap()
        );

        pack();
    }

    private void styleButton(JButton button, ActionListener action) {
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener(action);
    }

    private void loadNotes() {
        SwingWorker<List<Note>, Void> worker = new SwingWorker<>() {
            protected List<Note> doInBackground() throws Exception {
                return DatabaseUtil.getUserNotes(currentUser);
            }
            protected void done() {
                try {
                    listModel.clear();
                    List<Note> notes = get();
                    if (notes != null) {
                        notes.forEach(listModel::addElement);
                    }
                    updateCategoryFilter();
                } catch (Exception e) {
                    showError("Error loading notes: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateCategoryFilter() {
        String selected = (String) jCategoryFilter.getSelectedItem();
        jCategoryFilter.removeAllItems();
        jCategoryFilter.addItem("⇵");

        List<String> categories = DatabaseUtil.getCategories(currentUser);
        if (categories != null) {
            for (String category : categories) {
                if (category != null && !category.isEmpty()) {
                    jCategoryFilter.addItem(category);
                }
            }
        }

        if (selected != null && jCategoryFilter.getItemCount() > 1) {
            jCategoryFilter.setSelectedItem(selected);
        }
    }

    private void filterNotes() {
        String searchText = jSearchField.getText().toLowerCase();
        if (searchText.equals(" search")) searchText = "";

        List<Note> allNotes = DatabaseUtil.getUserNotes(currentUser);
        listModel.clear();

        if (allNotes != null) {
            for (Note note : allNotes) {
                if (searchText.isEmpty() || 
                    note.getContent().toLowerCase().contains(searchText) ||
                    (note.getCategory() != null && note.getCategory().toLowerCase().contains(searchText))) {
                    listModel.addElement(note);
                }
            }
        }
    }

    private void confirmAndDeleteNote(Note note) {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Delete this note?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            deleteNoteFromDatabase(note);
        }
    }

    private void deleteNoteFromDatabase(Note note) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            protected Boolean doInBackground() throws Exception {
                return DatabaseUtil.deleteNote(currentUser, note.getId());
            }
            protected void done() {
                try {
                    if (get()) {
                        listModel.removeElement(note);
                        updateCategoryFilter();
                    } else {
                        showError("Failed to delete note");
                    }
                } catch (Exception e) {
                    showError("Error deleting note: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void addNewNote() {
        AddNote addNote = new AddNote(currentUser);
        addNote.setSaveListener(note -> {
            if (note != null) {
                listModel.add(0, note);
                updateCategoryFilter();
            }
        });
        addNote.setVisible(true);
    }

    private void openNoteForEditing(Note note) {
        AddNote addNote = new AddNote(note, currentUser);
        addNote.setSaveListener(updatedNote -> {
            if (updatedNote != null) {
                for (int i = 0; i < listModel.size(); i++) {
                    if (listModel.getElementAt(i).getId() == updatedNote.getId()) {
                        listModel.set(i, updatedNote);
                        break;
                    }
                }
                updateCategoryFilter();
            }
        });
        addNote.setVisible(true);
    }

    private void openSettings() {
        new Settings(currentUser).setVisible(true);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class NoteListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            Note note = (Note) value;
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);

            JLabel contentLabel = new JLabel("<html>" + note.getContent().replaceAll("\n", "<br>") + "</html>");
            contentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JLabel categoryLabel = new JLabel(note.getCategory());
            categoryLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            categoryLabel.setForeground(Color.GRAY);
            categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel contentPanel = new JPanel(new BorderLayout()) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    g2.dispose();
                }
            };
            
            contentPanel.setBackground(isSelected ? new Color(230, 230, 230) : Color.WHITE);
            contentPanel.setBorder(new EmptyBorder(5, 15, 5, 15));
            contentPanel.add(contentLabel, BorderLayout.CENTER);
            contentPanel.add(categoryLabel, BorderLayout.SOUTH);

            panel.add(contentPanel, BorderLayout.CENTER);
            panel.setBorder(new EmptyBorder(5, 10, 5, 10));

            return panel;
        }
    }
}