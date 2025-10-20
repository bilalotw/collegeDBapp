package ui;

import db.DBConnection;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import static com.mongodb.client.model.Filters.eq;

public class StudentUI extends JPanel {

    private final MongoCollection<Document> collection;
    private DefaultTableModel model;
    private JTextField rollField, nameField, deptField, semField, emailField;
    private JTable table;

    private JTextField searchField;
    private JComboBox<String> searchByCombo;
    private JComboBox<String> deptSortCombo;
    private JComboBox<String> deptCombo;

    public StudentUI() {
        MongoDatabase db = DBConnection.getDatabase();
        collection = db.getCollection("students");

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 245));

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Search and Sort Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.setBackground(Color.WHITE);

        searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        filterPanel.add(searchField);

        searchByCombo = new JComboBox<>(new String[]{"Name", "Roll"});
        filterPanel.add(searchByCombo);

        deptSortCombo = new JComboBox<>(new String[]{"All Departments", "Sort by Dept ASC", "Sort by Dept DESC"});
        filterPanel.add(deptSortCombo);

        JButton searchBtn = createStyledButton("Search", new Color(33, 150, 243));
        searchBtn.addActionListener(e -> searchAndSortStudents());
        filterPanel.add(searchBtn);

        add(filterPanel, BorderLayout.PAGE_START);

        // Main Content Panel (Form + Table)
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        // Form Panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        loadStudents();
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(63, 81, 181));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panel.add(title);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Form title
        JLabel formTitle = new JLabel("Student Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(formTitle, BorderLayout.NORTH);

        // Input fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        rollField = createStyledTextField();
        nameField = createStyledTextField();
        deptCombo = new JComboBox<>();
        loadDepartments();  // populate combo box
        semField = createStyledTextField();
        emailField = createStyledTextField();

        addFormField(fieldsPanel, "Roll Number:", rollField, gbc, 0);
        addFormField(fieldsPanel, "Student Name:", nameField, gbc, 1);
        addFormField(fieldsPanel, "Department:", deptCombo, gbc, 2);
        addFormField(fieldsPanel, "Semester:", semField, gbc, 3);
        addFormField(fieldsPanel, "Email:", emailField, gbc, 4);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void addFormField(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panel.setBackground(Color.WHITE);

        JButton addBtn = createStyledButton("Add Student", new Color(76, 175, 80));
        JButton updBtn = createStyledButton("Update", new Color(33, 150, 243));
        JButton delBtn = createStyledButton("Delete", new Color(244, 67, 54));
        JButton clearBtn = createStyledButton("Clear", new Color(158, 158, 158));
        JButton refBtn = createStyledButton("Refresh", new Color(255, 152, 0));

        addBtn.addActionListener(e -> addStudent());
        updBtn.addActionListener(e -> updateStudent());
        delBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        refBtn.addActionListener(e -> {
            loadStudents();
            loadDepartments();
        });

        panel.add(addBtn);
        panel.add(updBtn);
        panel.add(delBtn);
        panel.add(clearBtn);
        panel.add(refBtn);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("Student Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Roll Number", "Name", "Department", "Semester", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(63, 81, 181, 50));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(63, 81, 181));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFields(table.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void populateFields(int row) {
        rollField.setText(model.getValueAt(row, 0).toString());
        nameField.setText(model.getValueAt(row, 1).toString());
        deptCombo.setSelectedItem(model.getValueAt(row, 2).toString());
        semField.setText(model.getValueAt(row, 3).toString());
        emailField.setText(model.getValueAt(row, 4).toString());
    }

    private void clearFields() {
        rollField.setText("");
        nameField.setText("");
        deptCombo.setSelectedIndex(-1);
        semField.setText("");
        emailField.setText("");
        table.clearSelection();
    }

    private void addStudent() {
        if (validateFields()) {
            Document doc = new Document("roll", rollField.getText().trim())
                    .append("name", nameField.getText().trim())
                    .append("dept", (String) deptCombo.getSelectedItem())
                    .append("semester", semField.getText().trim())
                    .append("email", emailField.getText().trim());

            collection.insertOne(doc);
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadStudents();
        }
    }

    private void updateStudent() {
        if (rollField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (validateFields()) {
            collection.updateOne(eq("roll", rollField.getText().trim()),
                    new Document("$set", new Document("name", nameField.getText().trim())
                            .append("dept", (String) deptCombo.getSelectedItem())
                            .append("semester", semField.getText().trim())
                            .append("email", emailField.getText().trim())));
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadStudents();
        }
    }

    private void deleteStudent() {
        if (rollField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            collection.deleteOne(eq("roll", rollField.getText().trim()));
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadStudents();
        }
    }

    private boolean validateFields() {
        if (rollField.getText().trim().isEmpty()
                || nameField.getText().trim().isEmpty()
                || deptField.getText().trim().isEmpty()
                || semField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadStudents() {
        model.setRowCount(0);
        for (Document d : collection.find()) {
            Object semester = d.get("semester");
            String semesterStr = semester == null ? "" : semester.toString();
            model.addRow(new Object[]{
                d.getString("roll"),
                d.getString("name"),
                d.getString("dept"),
                semesterStr,
                d.getString("email")
            });
        }
    }

    private void loadDepartments() {
        deptCombo.removeAllItems();
        MongoCollection<Document> deptCollection = DBConnection.getDatabase().getCollection("departments");
        for (Document doc : deptCollection.find()) {
            String deptCode = doc.getString("name");
            deptCombo.addItem(deptCode);
        }
    }

    private void searchAndSortStudents() {
        String keyword = searchField.getText().trim();
        String searchBy = (String) searchByCombo.getSelectedItem();
        String deptSortOrder = (String) deptSortCombo.getSelectedItem();

        Bson filter = new Document();
        if (!keyword.isEmpty()) {
            if ("Name".equals(searchBy)) {
                filter = eq("name", keyword);
            } else if ("Roll".equals(searchBy)) {
                filter = eq("roll", keyword);
            }
        }

        Bson sort = null;
        if ("Sort by Dept ASC".equals(deptSortOrder)) {
            sort = new Document("dept", 1);
        } else if ("Sort by Dept DESC".equals(deptSortOrder)) {
            sort = new Document("dept", -1);
        }

        model.setRowCount(0);
        FindIterable<Document> results;
        if (sort != null) {
            results = collection.find(filter).sort(sort);
        } else {
            results = collection.find(filter);
        }

        for (Document d : results) {
            Object semester = d.get("semester");
            String semesterStr = semester == null ? "" : semester.toString();
            model.addRow(new Object[]{
                d.getString("roll"),
                d.getString("name"),
                d.getString("dept"),
                semesterStr,
                d.getString("email")
            });
        }
    }
}
