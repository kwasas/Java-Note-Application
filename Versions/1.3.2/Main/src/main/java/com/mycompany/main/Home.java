package com.mycompany.main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Home extends javax.swing.JFrame {
    private JButton jAddNoteButton;
    private JTextField jSearchField;
    private JLabel jTitleLabel;
    private JList<Note> jNotesList;
    private JScrollPane jScrollPane;
    private DefaultListModel<Note> listModel;
    private JComboBox<String> jCategoryFilter;
    private String currentUser;

    public Home(String username) {
        this.currentUser = username;
        initComponents();
        loadNotes();
        Main.setWindowSize(this, 400, 600);
        Main.centerWindow(this);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Home - " + currentUser);

        jTitleLabel = new JLabel("Welcome, " + currentUser);
        jTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        jTitleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        jSearchField = new JTextField();
        jSearchField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        jSearchField.setBackground(Color.WHITE);
        jSearchField.setFont(new Font("Arial", Font.PLAIN, 14));
        jSearchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterNotes();
            }
        });

        jCategoryFilter = new JComboBox<>();
        jCategoryFilter.addItem("All Categories");
        jCategoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        jCategoryFilter.addActionListener(e -> filterNotes());

        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(jSearchField, BorderLayout.CENTER);
        filterPanel.add(jCategoryFilter, BorderLayout.EAST);

        listModel = new DefaultListModel<>();
        jNotesList = new JList<>(listModel);
        jNotesList.setBackground(Color.WHITE);
        jNotesList.setFont(new Font("Arial", Font.PLAIN, 16));
        jNotesList.setFixedCellHeight(60);
        jNotesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jNotesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
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
                    @Override
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
        });

        jNotesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Note selectedNote = jNotesList.getSelectedValue();
                    if (selectedNote != null) {
                        openNoteForEditing(selectedNote);
                    }
                }
            }
        });

        jScrollPane = new JScrollPane(jNotesList);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

        jAddNoteButton = new JButton("+ Add Note");
        jAddNoteButton.setFont(new Font("Arial", Font.BOLD, 14));
        jAddNoteButton.setBackground(new Color(255, 204, 0));
        jAddNoteButton.setForeground(Color.BLACK);
        jAddNoteButton.setFocusPainted(false);
        jAddNoteButton.setBorderPainted(false);
        jAddNoteButton.setOpaque(true);
        jAddNoteButton.addActionListener(e -> {
            AddNote addNote = new AddNote(currentUser);
            addNote.setSaveListener(note -> {
                listModel.addElement(note);
                updateCategoryFilter();
                filterNotes();
            });
            addNote.setVisible(true);
            dispose();
        });

        JButton jSettingsButton = new JButton("Settings");
        jSettingsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        jSettingsButton.setBackground(Color.WHITE);
        jSettingsButton.addActionListener(e -> {
            new Settings(currentUser).setVisible(true);
            dispose();
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jTitleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(filterPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jSettingsButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTitleLabel)
                .addGap(15)
                .addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(15)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSettingsButton))
                .addContainerGap()
        );

        pack();
    }

    private void loadNotes() {
        listModel.clear();
        List<Note> notes = DatabaseUtil.getUserNotes(currentUser);
        if (notes != null) {
            for (Note note : notes) {
                listModel.addElement(note);
            }
        }
        updateCategoryFilter();
    }

    private void updateCategoryFilter() {
        String selected = (String) jCategoryFilter.getSelectedItem();
        jCategoryFilter.removeAllItems();
        jCategoryFilter.addItem("All Categories");

        List<String> categories = DatabaseUtil.getCategories(currentUser);
        if (categories != null) {
            for (String category : categories) {
                if (category != null) {
                    jCategoryFilter.addItem(category);
                }
            }
        }

        if (selected != null && categories != null && categories.contains(selected)) {
            jCategoryFilter.setSelectedItem(selected);
        }
    }

    private void filterNotes() {
        String searchText = jSearchField.getText().toLowerCase();
        String selectedCategory = (String) jCategoryFilter.getSelectedItem();

        List<Note> allNotes = DatabaseUtil.getUserNotes(currentUser);
        listModel.clear();

        for (Note note : allNotes) {
            boolean matchesSearch = note.getContent().toLowerCase().contains(searchText);

            // Handle null category comparison safely
            boolean matchesCategory;
            if ("All Categories".equals(selectedCategory)) {
                matchesCategory = true;
            } else {
                String noteCategory = note.getCategory();
                matchesCategory = (noteCategory != null && noteCategory.equals(selectedCategory)) ||
                                 (noteCategory == null && selectedCategory == null);
            }

            if (matchesSearch && matchesCategory) {
                listModel.addElement(note);
            }
        }
    }

    private void openNoteForEditing(Note note) {
        AddNote addNote = new AddNote(note, currentUser);
        addNote.setSaveListener(updatedNote -> {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.getElementAt(i).getId() == updatedNote.getId()) {
                    listModel.set(i, updatedNote);
                    break;
                }
            }
            updateCategoryFilter();
            filterNotes();
        });
        addNote.setVisible(true);
        dispose();
    }
}