package com.mycompany.main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class Home extends javax.swing.JFrame {
    // UI Components
    private JButton jAddNoteButton;
    private JTextField jSearchField;
    private JLabel jTitleLabel;
    private JList<String> jNotesList;
    private JScrollPane jScrollPane;
    private DefaultListModel<String> listModel;
    private String currentUser;

    // Constructor - initializes the window with a username
    public Home(String username) {
        this.currentUser = username;
        initComponents(); // build the UI
        loadNotes(); // load user's notes from DB
        Main.setWindowSize(this, 380, 596); // fixed window size
        Main.centerWindow(this); // center the window on screen
    }

    // Method to set up UI components and layout
    private void initComponents() {
        // Close app when this window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Home");

        // ----- Title Label -----
        jTitleLabel = new JLabel("Home");
        jTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        jTitleLabel.setBorder(new EmptyBorder(20, 20, 20, 20)); // padding

        // ----- Search Field -----
        jSearchField = new JTextField();
        // Compound border: outer rounded line + inner padding
        jSearchField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true), // light gray, rounded
            new EmptyBorder(10, 15, 10, 15) // top, left, bottom, right padding
        ));
        jSearchField.setBackground(Color.WHITE);
        jSearchField.setFont(new Font("Arial", Font.PLAIN, 14));

        // ----- Notes List -----
        listModel = new DefaultListModel<>(); // manages list data
        jNotesList = new JList<>(listModel);
        jNotesList.setBackground(Color.WHITE);
        jNotesList.setFont(new Font("Arial", Font.PLAIN, 16));
        jNotesList.setFixedCellHeight(60); // height for each note item
        jNotesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ----- Custom Renderer to style each list item -----
        jNotesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);

                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                label.setBorder(new EmptyBorder(10, 15, 10, 15));

                // Inner panel with rounded corners for each list item
                JPanel contentPanel = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // rounded fill
                        g2.setColor(new Color(200, 200, 200));
                        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15); // border
                        g2.dispose();
                    }
                };
                contentPanel.setBackground(isSelected ? new Color(230, 230, 230) : Color.WHITE); // highlight selected
                contentPanel.add(label, BorderLayout.CENTER);
                contentPanel.setBorder(new EmptyBorder(5, 15, 5, 15));

                panel.add(contentPanel, BorderLayout.CENTER);
                panel.setBorder(new EmptyBorder(5, 10, 5, 10));

                return panel;
            }
        });

        // ----- Make Notes Clickable -----
        jNotesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedNote = jNotesList.getSelectedValue();
                if (selectedNote != null) {
                    this.dispose(); // close current window
                    new AddNote(selectedNote, jNotesList.getSelectedIndex(), currentUser).setVisible(true); // open note
                }
            }
        });

        // ----- Scroll Pane for Notes -----
        jScrollPane = new JScrollPane(jNotesList);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        jScrollPane.getViewport().setOpaque(false); // transparent background

        // ----- Add Note Button -----
        jAddNoteButton = new JButton("+ Add Note");
        jAddNoteButton.setFont(new Font("Arial", Font.BOLD, 14));
        jAddNoteButton.setBackground(new Color(255, 204, 0)); // yellow
        jAddNoteButton.setForeground(Color.BLACK);
        jAddNoteButton.setFocusPainted(false); // removes focus outline
        jAddNoteButton.setBorderPainted(false); // no border line
        jAddNoteButton.setOpaque(true);
        // Action when clicked: open AddNote window
        jAddNoteButton.addActionListener(e -> {
            this.dispose();
            new AddNote(currentUser).setVisible(true);
        });

        // ----- Layout Setup -----
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        getContentPane().setBackground(Color.WHITE); // main background color

        int componentWidth = 360; // shared width for list & layout

        // Horizontal layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jTitleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSearchField, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, componentWidth, GroupLayout.PREFERRED_SIZE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        // Vertical layout
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTitleLabel)
                .addGap(15)
                .addComponent(jSearchField, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(15)
                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(15)
                .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
        );

        pack(); // adjust frame to fit contents
    }

    // Method to load notes from database for the current user
    private void loadNotes() {
        listModel.clear(); // clear old list data
        try {
            List<String> notes = DatabaseUtil.getUserNotes(currentUser);
            if (notes != null) {
                for (String note : notes) {
                    if (note != null) {
                        listModel.addElement(note); // add each note to the list
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