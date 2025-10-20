package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("College Management Dashboard");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Optional header label
        JLabel headerLabel = new JLabel("College Management System");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headerLabel, BorderLayout.NORTH);

        // Tabbed pane with padding inside container panel
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        tabs.addTab("Students", new StudentUI());
        tabs.addTab("Faculty", new FacultyUI());
        tabs.addTab("Courses", new CourseUI());
        tabs.addTab("Departments", new DepartmentUI());

        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabPanel.add(tabs, BorderLayout.CENTER);

        add(tabPanel, BorderLayout.CENTER);
    }
}
