package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignIn extends javax.swing.JFrame {
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JLabel jLabel0;
    private javax.swing.JLabel jForgotPassword;

    public SignIn() {
        initComponents();
        Main.setWindowSize(this, 380, 596);
        Main.centerWindow(this);
    }

    private void initComponents() {
        jLabel0 = new JLabel("Sign In");
        jLabel0.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabel0.setHorizontalAlignment(SwingConstants.CENTER);

        // Use rounded text field
        jTextField1 = new RoundedTextField(20);
        jTextField1.setBackground(Color.WHITE);
        setPlaceholder(jTextField1, "Enter username");

        // Use rounded password field
        jPasswordField1 = new RoundedPasswordField(20);
        jPasswordField1.setBackground(Color.WHITE);
        setPasswordPlaceholder(jPasswordField1, "Enter password");

        jForgotPassword = new JLabel("Forgot password?");
        jForgotPassword.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jForgotPassword.setForeground(new Color(255, 204, 0));
        jForgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SignIn.this.dispose();
                new ForgotPassword().setVisible(true);
            }
        });

        // Use rounded button for sign in
        jButton1 = new RoundedButton("Sign In", 35);
        jButton1.setBackground(new Color(255, 204, 0));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        // Use rounded button for create account (transparent)
        jButton2 = new RoundedButton("<html>Don't have an account? <font color='#FFCC00'>Create account</font><html>", 15);
        jButton2.setBackground(new Color(0, 0, 0, 0)); // Transparent
        jButton2.setForeground(Color.BLACK);
        jButton2.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jButton2.addActionListener(this::jButton2ActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign In");

        // Set frame background
        getContentPane().setBackground(Color.WHITE);

        // Set fixed heights
        Dimension fieldSize = new Dimension(260, 40);
        jTextField1.setPreferredSize(fieldSize);
        jPasswordField1.setPreferredSize(fieldSize);
        jButton1.setPreferredSize(new Dimension(200, 40));

        // Layout setup
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(jLabel0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                .addComponent(jForgotPassword)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addGap(100)
                .addComponent(jLabel0)
                .addGap(20)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGap(15)
                .addComponent(jForgotPassword)
                .addGap(20)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void handleForgotPassword() {
        int choice = JOptionPane.showOptionDialog(this, 
            "Forgot your password?", 
            "Password Recovery",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{"Get Reset Token", "Reset Password", "Cancel"},
            "Get Reset Token");
        
        if (choice == 0) {
            this.dispose();
            new ForgotPassword().setVisible(true);
        } else if (choice == 1) {
            this.dispose();
            new ResetPassword().setVisible(true);
        }
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void setPasswordPlaceholder(JPasswordField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setEchoChar((char)0);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char)0);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String username = jTextField1.getText().trim();
        if (username.equals("Enter username")) username = "";
        
        String password = new String(jPasswordField1.getPassword());
        if (password.equals("Enter password")) password = "";

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseUtil.validateUser(username, password)) {
            this.dispose();
            new Home(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        new SignUp().setVisible(true);
    }
}

class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g2);
        g2.dispose();
    }
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

class RoundedPasswordField extends JPasswordField {
    private int cornerRadius = 20;

    public RoundedPasswordField(int columns) {
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