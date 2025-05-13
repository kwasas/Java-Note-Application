package com.mycompany.main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class Home extends javax.swing.JFrame {
    private JButton jAddNoteButton;
    private JTextField jSearchField;
    private JLabel jLabel1;
    private JButton jSettingsButton;
    private JList<String> jListNotes;
    private JScrollPane jScrollPaneNotes;
    private DefaultListModel<String> listModel;
    private String currentUser;

    public Home(String username) {
        this.currentUser = username;
        initComponents();
        loadNotes();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        // Main label
        jLabel1 = new JLabel("Home");
        jLabel1.setFont(new Font("Arial", Font.BOLD, 18));

        // Search field with border
        jSearchField = new JTextField();
        jSearchField.setBorder(new CompoundBorder(
            new TitledBorder("Search"),
            new EmptyBorder(5, 5, 5, 5)
        ));

        // Settings button
        jSettingsButton = new JButton("⚙");
        jSettingsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        jSettingsButton.setBorderPainted(false);
        jSettingsButton.setContentAreaFilled(false);
        jSettingsButton.addActionListener(e -> {
            this.dispose();
            new Settings().setVisible(true);
        });

        // Add note button
        jAddNoteButton = new JButton("+ Add Note");
        jAddNoteButton.setFont(new Font("Arial", Font.BOLD, 14));
        jAddNoteButton.setBackground(new Color(255, 204, 0));
        jAddNoteButton.setOpaque(true);
        jAddNoteButton.setBorderPainted(false);
        jAddNoteButton.addActionListener(e -> {
            this.dispose();
            new AddNote(currentUser).setVisible(true);
        });

        // Notes list with proper selection handling
        listModel = new DefaultListModel<>();
        jListNotes = new JList<>(listModel);
        jListNotes.setFixedCellHeight(50);
        jListNotes.setBackground(Color.WHITE);
        jListNotes.setSelectionBackground(new Color(240, 240, 240));
        jListNotes.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Enable note selection and editing
        jListNotes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedNote = jListNotes.getSelectedValue();
                if (selectedNote != null) {
                    this.dispose();
                    new AddNote(selectedNote, jListNotes.getSelectedIndex(), currentUser).setVisible(true);
                }
            }
        });

        jListNotes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    new EmptyBorder(10, 15, 10, 15)
                ));
                return label;
            }
        });

        // Scroll pane with border
        jScrollPaneNotes = new JScrollPane(jListNotes);
        jScrollPaneNotes.setBorder(new CompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            new LineBorder(new Color(220, 220, 220), 1)
        ));

        // Main layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Home");
        getContentPane().setBackground(Color.WHITE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSettingsButton))
                    .addComponent(jSearchField)
                    .addComponent(jScrollPaneNotes)
                    .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSettingsButton))
                .addGap(18)
                .addComponent(jSearchField, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(18)
                .addComponent(jScrollPaneNotes, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(18)
                .addComponent(jAddNoteButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
        );
   

        pack();
    }

    private void loadNotes() {
        listModel.clear();
        try {
            List<String> notes = DatabaseUtil.getUserNotes(currentUser);
            if (notes != null) {
                for (String note : notes) {
                    if (note != null) {
                        listModel.addElement(note);
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