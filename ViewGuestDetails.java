package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ViewGuestDetails extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private final Color SIDEBAR_COLOR = new Color(10, 22, 35);
    private final Color GOLD = new Color(220, 170, 95);
    private final Color WHITE = Color.WHITE;

    public ViewGuestDetails() {
        setTitle("Hotel Management System - Guest Details");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SIDEBAR_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 40, 20, 40));

        JLabel title = new JLabel("GUEST DETAILS");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(GOLD);
        headerPanel.add(title, BorderLayout.WEST);

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(WHITE);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(Color.BLACK);
        searchField.setForeground(WHITE);
        searchField.setCaretColor(WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(GOLD, 1));

        JButton searchButton = new JButton("Search");
        styleButton(searchButton);
        searchButton.setPreferredSize(new Dimension(90, 32));

        JButton resetButton = new JButton("Reset");
        styleButton(resetButton);
        resetButton.setPreferredSize(new Dimension(90, 32));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        // --- Action Buttons Panel ---
        JButton deleteButton = new JButton("Delete Selected");
        styleButton(deleteButton);

        JButton backButton = new JButton("← Dashboard");
        styleButton(backButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(deleteButton);
        rightPanel.add(backButton);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.CENTER);
        topContainer.add(rightPanel, BorderLayout.SOUTH);
        topContainer.setBorder(new EmptyBorder(0, 40, 10, 40));

        mainPanel.add(topContainer, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {
                "Booking ID", "Guest Name", "Phone", "ID Type",
                "Room Type", "Room No.", "Check-In Date", "Guests", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(GOLD);
        table.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));
        scrollPane.getViewport().setBackground(new Color(15, 25, 35));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Load Initial Data
        loadGuestData("");

        // Search Action
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            loadGuestData(keyword);
        });

        // Reset Action
        resetButton.addActionListener(e -> {
            searchField.setText("");
            loadGuestData("");
        });

        // Delete Action
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a guest row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Booking ID: " + bookingId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("DELETE FROM checkins WHERE booking_id = ?")) {

                    pstmt.setString(1, bookingId);
                    int rows = pstmt.executeUpdate();

                    if (rows > 0) {
                        String roomNo = (String) tableModel.getValueAt(selectedRow, 5);
                        try (PreparedStatement roomPstmt = conn.prepareStatement("UPDATE rooms SET status = 'Available' WHERE room_number = ?")) {
                            roomPstmt.setString(1, roomNo);
                            roomPstmt.executeUpdate();
                        }
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, "Deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        backButton.addActionListener(e -> {
            new Dashboard();
            dispose();
        });

        setVisible(true);
    }

    private void loadGuestData(String keyword) {
        tableModel.setRowCount(0); // Clear existing rows

        String query;
        boolean isSearch = !keyword.isEmpty();

        if (isSearch) {
            query = "SELECT booking_id, guest_name, phone, id_type, room_type, room_number, check_in_date, number_of_guests, status FROM checkins WHERE booking_id LIKE ? OR guest_name LIKE ?";
        } else {
            query = "SELECT booking_id, guest_name, phone, id_type, room_type, room_number, check_in_date, number_of_guests, status FROM checkins";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (isSearch) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("booking_id"),
                            rs.getString("guest_name"),
                            rs.getString("phone"),
                            rs.getString("id_type"),
                            rs.getString("room_type"),
                            rs.getString("room_number"),
                            rs.getDate("check_in_date"),
                            rs.getInt("number_of_guests"),
                            rs.getString("status")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(GOLD, 1));
        button.setPreferredSize(new Dimension(140, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(GOLD);
                button.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(SIDEBAR_COLOR);
                button.setForeground(Color.WHITE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewGuestDetails());
    }
}