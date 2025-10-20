package ui;

import db.DBConnection;
import com.mongodb.client.*;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import static com.mongodb.client.model.Filters.eq;

public class FacultyUI extends JPanel {

    private final MongoCollection<Document> collection;
    private DefaultTableModel model;
    private JTextField idField, nameField, subjectField;
    private JComboBox<String> deptCombo;
    private JTable table;

    public FacultyUI() {
        MongoDatabase db = DBConnection.getDatabase();
        collection = db.getCollection("faculty");

        deptCombo = new JComboBox<>();
        loadDepartments();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 245));

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

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

        loadFaculty();
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(156, 39, 176));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Faculty Management System");
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
        JLabel formTitle = new JLabel("Faculty Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(formTitle, BorderLayout.NORTH);

        // Input fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        idField = createStyledTextField();
        nameField = createStyledTextField();
        subjectField = createStyledTextField();

        addFormField(fieldsPanel, "Faculty ID:", idField, gbc, 0);
        addFormField(fieldsPanel, "Faculty Name:", nameField, gbc, 1);
        addFormField(fieldsPanel, "Department:", deptCombo, gbc, 2);
        addFormField(fieldsPanel, "Subject:", subjectField, gbc, 3);

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

        JButton addBtn = createStyledButton("Add Faculty", new Color(156, 39, 176));
        JButton updBtn = createStyledButton("Update", new Color(103, 58, 183));
        JButton delBtn = createStyledButton("Delete", new Color(244, 67, 54));
        JButton clearBtn = createStyledButton("Clear", new Color(158, 158, 158));
        JButton refBtn = createStyledButton("Refresh", new Color(255, 152, 0));

        addBtn.addActionListener(e -> addFaculty());
        updBtn.addActionListener(e -> updateFaculty());
        delBtn.addActionListener(e -> deleteFaculty());
        clearBtn.addActionListener(e -> clearFields());
        refBtn.addActionListener(e -> {
            loadFaculty();
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

        JLabel tableTitle = new JLabel("Faculty Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Faculty ID", "Name", "Department", "Subject"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(156, 39, 176, 50));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(156, 39, 176));
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
        idField.setText(model.getValueAt(row, 0).toString());
        nameField.setText(model.getValueAt(row, 1).toString());
        deptCombo.setSelectedItem(model.getValueAt(row, 2).toString());
        subjectField.setText(model.getValueAt(row, 3).toString());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        deptCombo.setSelectedIndex(-1);
        subjectField.setText("");
        table.clearSelection();
    }

    private void addFaculty() {
        if (validateFields()) {
            Document doc = new Document("id", idField.getText().trim())
                    .append("name", nameField.getText().trim())
                    .append("dept", (String) deptCombo.getSelectedItem())
                    .append("subject", subjectField.getText().trim());
            collection.insertOne(doc);
            JOptionPane.showMessageDialog(this, "Faculty added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadFaculty();
        }
    }

    private void updateFaculty() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a faculty member to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (validateFields()) {
            collection.updateOne(eq("id", idField.getText().trim()),
                    new Document("$set", new Document()
                            .append("name", nameField.getText().trim())
                            .append("dept", (String) deptCombo.getSelectedItem())
                            .append("subject", subjectField.getText().trim())));
            JOptionPane.showMessageDialog(this, "Faculty updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadFaculty();
        }
    }

    private void deleteFaculty() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a faculty member to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this faculty member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            collection.deleteOne(eq("id", idField.getText().trim()));
            JOptionPane.showMessageDialog(this, "Faculty deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadFaculty();
        }
    }

    private boolean validateFields() {
        if (idField.getText().trim().isEmpty()
                || nameField.getText().trim().isEmpty()
                || deptCombo.getSelectedItem() == null
                || subjectField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadFaculty() {
        model.setRowCount(0);
        for (Document d : collection.find()) {
            model.addRow(new Object[]{
                d.getString("id"),
                d.getString("name"),
                d.getString("dept"),
                d.getString("subject")
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
        deptCombo.setSelectedIndex(-1);
    }
}
