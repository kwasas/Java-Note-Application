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
    
    // Swipe animation variables
    private static final int SWIPE_THRESHOLD = 100;
    private int swipeStartX = -1;
    private Note swipedNote = null;
    private int swipeOffset = 0;
    private Timer swipeTimer;
    private boolean isSwipingBack = false;

    public Home(String username) {
        this.currentUser = username;
        initComponents();
        loadNotes();
        Main.setWindowSize(this, 400, 600);
        Main.centerWindow(this);
        
        // Initialize swipe animation timer
        swipeTimer = new Timer(10, e -> {
            if (isSwipingBack) {
                swipeOffset += 10;
                if (swipeOffset >= 0) {
                    swipeOffset = 0;
                    swipeTimer.stop();
                    isSwipingBack = false;
                }
            } else if (swipeOffset < -SWIPE_THRESHOLD) {
                swipeOffset -= 5;
                if (swipeOffset <= -SWIPE_THRESHOLD * 2) {
                    confirmAndDeleteNote(swipedNote);
                    swipeTimer.stop();
                }
            }
            jNotesList.repaint();
        });
    }
    
    class RoundedTextField extends JTextField {
    private int cornerRadius = 20;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
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
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        g2.dispose();
    }
}


    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Home - " + currentUser);

        // Title Label
        jTitleLabel = new JLabel("Welcome, " + currentUser);
        jTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        jTitleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Search Field with placeholder
        jSearchField = new RoundedTextField(20);
        jSearchField.setForeground(new Color(150, 150, 150));
        jSearchField.setFont(new Font("Arial", Font.PLAIN, 14));
        jSearchField.setBackground(Color.WHITE);
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
                    jSearchField.setText(" Search");
                    jSearchField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        jSearchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterNotes();
            }
});

        // Category Filter
        jCategoryFilter = new JComboBox<>();
        jCategoryFilter.addItem("All Categories");
        jCategoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        jCategoryFilter.addActionListener(e -> filterNotes());

        // Filter Panel (matches note width)
        JPanel filterPanel = new JPanel(new BorderLayout(10, 0));
        filterPanel.setBorder(new EmptyBorder(0, 15, 0, 15)); // Matches note margins
        filterPanel.add(jSearchField, BorderLayout.CENTER);
        filterPanel.add(jCategoryFilter, BorderLayout.EAST);

        // Notes List with swipe animation
        listModel = new DefaultListModel<>();
        jNotesList = new JList<>(listModel) {
            @Override
            protected void paintComponent(Graphics g) {
                // Paint the background first
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Paint each cell with swipe animation
                for (int i = 0; i < getModel().getSize(); i++) {
                    Rectangle cellBounds = getCellBounds(i, i);
                    if (cellBounds != null) {
                        Graphics cellGraphics = g.create(0, cellBounds.y, getWidth(), cellBounds.height);
                        Note note = getModel().getElementAt(i);
                        
                        // Apply swipe offset
                        int offset = (note == swipedNote) ? swipeOffset : 0;
                        
                        // Paint delete area (red background)
                        if (offset < 0) {
                            int deleteWidth = Math.min(-offset, SWIPE_THRESHOLD);
                            Graphics2D g2d = (Graphics2D)cellGraphics.create();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setColor(new Color(255, 100, 100));
                            g2d.fillRoundRect(
                                getWidth() - deleteWidth, 
                                5, 
                                deleteWidth, 
                                cellBounds.height - 10, 
                                15, 
                                15
                            );
                            
                            // Paint trash icon
                            ImageIcon trashIcon = new ImageIcon(getClass().getResource("/assets/trash.png"));
                            int iconX = getWidth() - deleteWidth/2 - trashIcon.getIconWidth()/2;
                            int iconY = cellBounds.height/2 - trashIcon.getIconHeight()/2;
                            trashIcon.paintIcon(this, g2d, iconX, iconY);
                            g2d.dispose();
                        }
                        
                        // Paint the cell content
                        cellGraphics.translate(offset, 0);
                        super.paintComponent(cellGraphics);
                        cellGraphics.dispose();
                    }
                }
            }
        };
        jNotesList.setBackground(Color.WHITE);
        jNotesList.setFont(new Font("Arial", Font.PLAIN, 16));
        jNotesList.setFixedCellHeight(60);
        jNotesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jNotesList.setCellRenderer(new NoteListCellRenderer());
        
        // Mouse listeners for swipe
        jNotesList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (swipeStartX != -1 && swipedNote != null) {
                    swipeOffset = e.getX() - swipeStartX;
                    if (swipeOffset > 0) swipeOffset = 0;
                    jNotesList.repaint();
                }
            }
        });
        
        jNotesList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int index = jNotesList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    swipedNote = listModel.getElementAt(index);
                    swipeStartX = e.getX();
                    swipeOffset = 0;
                }
            }
            
            public void mouseReleased(MouseEvent e) {
                if (swipeStartX != -1 && swipedNote != null) {
                    if (swipeOffset < -SWIPE_THRESHOLD) {
                        swipeTimer.start();
                    } else {
                        isSwipingBack = true;
                        swipeTimer.start();
                    }
                }
                swipeStartX = -1;
            }
            
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
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

        // Bottom navigation buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton jDashboardButton = new JButton(new ImageIcon(getClass().getResource("/assets/dashboard.png")));
        styleNavigationButton(jDashboardButton);
        jDashboardButton.addActionListener(e -> showDashboard());

        JButton jAddNoteButton = new JButton(new ImageIcon(getClass().getResource("/assets/message-add.png")));
        styleNavigationButton(jAddNoteButton);
        jAddNoteButton.addActionListener(e -> addNewNote());

        JButton jSettingsButton = new JButton(new ImageIcon(getClass().getResource("/assets/profile-circle.png")));
        styleNavigationButton(jSettingsButton);
        jSettingsButton.addActionListener(e -> openSettings());

        buttonPanel.add(jDashboardButton);
        buttonPanel.add(jAddNoteButton);
        buttonPanel.add(jSettingsButton);

        // Main layout
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
                    .addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
        );

        pack();
    }

    private void styleNavigationButton(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
        if (searchText.equals(" search...")) searchText = "";
        
        String selectedCategory = (String) jCategoryFilter.getSelectedItem();

        List<Note> allNotes = DatabaseUtil.getUserNotes(currentUser);
        listModel.clear();

        for (Note note : allNotes) {
            boolean matchesSearch = searchText.isEmpty() || 
                                  note.getContent().toLowerCase().contains(searchText);

            boolean matchesCategory = "All Categories".equals(selectedCategory) ||
                                    (note.getCategory() != null && note.getCategory().equals(selectedCategory)) ||
                                    (note.getCategory() == null && selectedCategory == null);

            if (matchesSearch && matchesCategory) {
                listModel.addElement(note);
            }
        }
    }

    private void confirmAndDeleteNote(Note note) {
        boolean success = DatabaseUtil.deleteNote(currentUser, note.getId());
        if (success) {
            listModel.removeElement(note);
            updateCategoryFilter();
            swipeOffset = 0;
            swipedNote = null;
            jNotesList.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete note", "Error", JOptionPane.ERROR_MESSAGE);
            isSwipingBack = true;
            swipeTimer.start();
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

    private void showDashboard() {
        JOptionPane.showMessageDialog(this, "Dashboard will be implemented");
    }

    private void addNewNote() {
        AddNote addNote = new AddNote(currentUser);
        addNote.setSaveListener(note -> {
            listModel.addElement(note);
            updateCategoryFilter();
            filterNotes();
        });
        addNote.setVisible(true);
        dispose();
    }

    private void openSettings() {
        new Settings(currentUser).setVisible(true);
        dispose();
    }

    // Custom cell renderer for notes
    private class NoteListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                    boolean isSelected, boolean cellHasFocus) {
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
    }
}