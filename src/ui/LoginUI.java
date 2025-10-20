package ui;

import db.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.mongodb.client.model.Filters.eq;

public class LoginUI extends JFrame {

    private final JTextField userField;
    private final JPasswordField passField;
    private final MongoCollection<Document> adminCollection;

    public LoginUI() {
        MongoDatabase db = DBConnection.getDatabase();
        adminCollection = db.getCollection("admins");

        setTitle("Admin Login - College DB");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel("Welcome to College DB Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel);

        // Username panel
        JPanel userPanel = new JPanel(new BorderLayout(10, 5));
        JLabel userLabel = new JLabel("Username:");
        userLabel.setPreferredSize(new Dimension(80, 25));
        userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 30));
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(userField, BorderLayout.CENTER);
        mainPanel.add(userPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Password panel
        JPanel passPanel = new JPanel(new BorderLayout(10, 5));
        JLabel passLabel = new JLabel("Password:");
        passLabel.setPreferredSize(new Dimension(80, 25));
        passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 30));
        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passField, BorderLayout.CENTER);
        mainPanel.add(passPanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Login button panel for centering button
        JPanel btnPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 35));
        btnPanel.add(loginBtn);
        mainPanel.add(btnPanel);

        // Button action
        loginBtn.addActionListener(e -> verifyLogin());

        setResizable(false);
        setVisible(true);
    }

    private void verifyLogin() {
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        Document admin = adminCollection.find(eq("username", user)).first();

        if (admin != null && admin.getString("password").equals(pass)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            new MainDashboard().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }
}
