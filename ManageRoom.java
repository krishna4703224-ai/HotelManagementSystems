package hotel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageRoom extends JFrame {

    private final Color GOLD = new Color(212, 175, 55);
    private final Color GOLD_LIGHT = new Color(245, 202, 90);
    private final Color DARK = new Color(5, 18, 39);
    private final Color CARD = new Color(10, 28, 53);
    private final Color BORDER = new Color(35, 60, 90);
    private final Color TEXT = new Color(225, 232, 245);
    private final Color MUTED = new Color(165, 180, 205);
    private final Color GREEN = new Color(15, 170, 115);
    private final Color RED = new Color(215, 45, 70);
    private final Color BLUE = new Color(35, 95, 210);
    private final Color ORANGE = new Color(205, 135, 20);

    private JPanel roomGrid;
    private JTextField searchField;
    private JComboBox<String> filterBox;
    private JLabel totalLabel, availableLabel, checkedInLabel, checkedOutLabel, maintenanceLabel;
    private JLabel dateTimeLabel;
    private Timer clockTimer;
    private final List<Room> rooms = new ArrayList<>();

    public ManageRoom() {
        setTitle("ROYAL STAY - Manage Rooms");
        setSize(1500, 900);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK);

        createUI();
        startClock();
        refreshRooms();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK);
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createMainContent(), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(DARK);
        header.setBorder(new MatteBorder(0, 0, 1, 0, GOLD));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        logoPanel.setBackground(DARK);
        JLabel logo = new JLabel("▰  ROYAL STAY");
        logo.setForeground(GOLD_LIGHT);
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoPanel.add(logo);
        header.add(logoPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setBackground(DARK);

        JButton backButton = createActionButton("← Back to Dashboard", GOLD);
        backButton.setPreferredSize(new Dimension(160, 38));
        backButton.addActionListener(e -> {
            new Dashboard();
            dispose();
        });

        dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(GOLD_LIGHT);
        dateTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel separator = new JLabel("|");
        separator.setForeground(BORDER);
        JLabel adminIcon = new JLabel("●");
        adminIcon.setForeground(GOLD_LIGHT);
        JLabel adminLabel = new JLabel("Admin");
        adminLabel.setForeground(TEXT);
        adminLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        rightPanel.add(backButton);
        rightPanel.add(dateTimeLabel);
        rightPanel.add(separator);
        rightPanel.add(adminIcon);
        rightPanel.add(adminLabel);

        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(DARK);
        content.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel topArea = new JPanel();
        topArea.setBackground(DARK);
        topArea.setLayout(new BoxLayout(topArea, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(DARK);
        JLabel bedIcon = new JLabel("▰");
        bedIcon.setForeground(GOLD);
        bedIcon.setFont(new Font("SansSerif", Font.BOLD, 34));
        JLabel title = new JLabel("Manage Rooms");
        title.setForeground(GOLD_LIGHT);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        titlePanel.add(bedIcon);
        titlePanel.add(Box.createHorizontalStrut(15));
        titlePanel.add(title);

        JLabel subtitle = new JLabel("View and manage all rooms with their current status.");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setBorder(new EmptyBorder(4, 55, 20, 0));

        topArea.add(titlePanel);
        topArea.add(subtitle);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 5, 16, 0));
        summaryPanel.setBackground(DARK);
        summaryPanel.setPreferredSize(new Dimension(0, 100));

        summaryPanel.add(createSummaryCard("▰", "Total Rooms", "0", new Color(70, 100, 145)));
        summaryPanel.add(createSummaryCard("●", "Available", "0", GREEN));
        summaryPanel.add(createSummaryCard("●", "Checked In", "0", RED));
        summaryPanel.add(createSummaryCard("↗", "Checked Out", "0", BLUE));
        summaryPanel.add(createSummaryCard("⚒", "Maintenance", "0", ORANGE));

        topArea.add(summaryPanel);
        topArea.add(Box.createVerticalStrut(22));

        JPanel searchPanel = new JPanel(new BorderLayout(15, 0));
        searchPanel.setBackground(CARD);
        searchPanel.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(10, 15, 10, 10)));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 40));
        searchField.setBackground(new Color(8, 24, 47));
        searchField.setForeground(TEXT);
        searchField.setCaretColor(GOLD);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        searchField.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(0, 12, 0, 12)));
        searchField.setToolTipText("Search by Room Number");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { displayRooms(); }
            public void removeUpdate(DocumentEvent e) { displayRooms(); }
            public void changedUpdate(DocumentEvent e) { displayRooms(); }
        });

        filterBox = new JComboBox<>(new String[]{
                "All Rooms",
                "Single Room",
                "Double Room",
                "Deluxe Room",
                "Suite Room"
        });
        filterBox.setPreferredSize(new Dimension(190, 40));
        filterBox.setBackground(new Color(8, 24, 47));
        filterBox.setForeground(TEXT);
        filterBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        filterBox.addActionListener(e -> displayRooms());

        JButton refreshButton = createActionButton("⟳  Refresh", GOLD);
        refreshButton.addActionListener(e -> refreshRooms());

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(filterBox, BorderLayout.EAST);
        searchPanel.add(refreshButton, BorderLayout.WEST);

        topArea.add(searchPanel);
        topArea.add(Box.createVerticalStrut(22));
        content.add(topArea, BorderLayout.NORTH);

        roomGrid = new JPanel();
        roomGrid.setBackground(DARK);
        roomGrid.setLayout(new GridLayout(0, 4, 18, 18));

        JScrollPane scrollPane = new JScrollPane(roomGrid);
        scrollPane.setBorder(null);
        scrollPane.setBackground(DARK);
        scrollPane.getViewport().setBackground(DARK);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scrollPane, BorderLayout.CENTER);
        return content;
    }

    private JPanel createSummaryCard(String icon, String name, String value, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(12, 12, 12, 12)));

        JPanel iconCircle = new JPanel(new GridBagLayout());
        iconCircle.setPreferredSize(new Dimension(58, 58));
        iconCircle.setBackground(new Color(Math.min(255, iconColor.getRed() / 3 + 10), Math.min(255, iconColor.getGreen() / 3 + 10), Math.min(255, iconColor.getBlue() / 3 + 10)));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(iconColor);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        iconCircle.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(CARD);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(TEXT);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 26));

        if (name.equals("Total Rooms")) totalLabel = valueLabel;
        else if (name.equals("Available")) availableLabel = valueLabel;
        else if (name.equals("Checked In")) checkedInLabel = valueLabel;
        else if (name.equals("Checked Out")) checkedOutLabel = valueLabel;
        else if (name.equals("Maintenance")) maintenanceLabel = valueLabel;

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(valueLabel);

        card.add(iconCircle, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private void initializeRooms() {
        rooms.clear();
        String sql = "SELECT room_number, room_type, price, status FROM rooms ORDER BY room_number";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String roomNumber = resultSet.getString("room_number");
                String rawType = resultSet.getString("room_type");
                int price = resultSet.getBigDecimal("price").intValue();
                String status = resultSet.getString("status");

                // Normalize database variants into standard 4 room types
                String roomType = "Single Room";
                if (rawType != null) {
                    String lower = rawType.trim().toLowerCase();
                    if (lower.contains("double")) {
                        roomType = "Double Room";
                    } else if (lower.contains("delux") || lower.contains("deluxe")) {
                        roomType = "Deluxe Room";
                    } else if (lower.contains("suite")) {
                        roomType = "Suite Room";
                    } else {
                        roomType = "Single Room";
                    }
                }

                rooms.add(new Room(roomNumber, roomType, price, status));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to load rooms from database.\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void refreshRooms() {
        initializeRooms();
        displayRooms();
    }

    private void displayRooms() {
        if (roomGrid == null) return;
        roomGrid.removeAll();

        String search = searchField != null ? searchField.getText().trim().toLowerCase() : "";
        String filter = filterBox != null && filterBox.getSelectedItem() != null ? filterBox.getSelectedItem().toString() : "All Rooms";

        int visibleRooms = 0;
        for (Room room : rooms) {
            boolean matchesSearch = room.number.toLowerCase().contains(search);
            boolean matchesFilter = filter.equals("All Rooms") || room.status.equalsIgnoreCase(filter) || room.type.equalsIgnoreCase(filter);

            if (matchesSearch && matchesFilter) {
                roomGrid.add(createRoomCard(room));
                visibleRooms++;
            }
        }

        if (visibleRooms == 0) {
            JLabel noRooms = new JLabel("No rooms found");
            noRooms.setForeground(MUTED);
            noRooms.setFont(new Font("SansSerif", Font.BOLD, 18));
            roomGrid.add(noRooms);
        }

        updateSummary();
        roomGrid.revalidate();
        roomGrid.repaint();
    }

    private JPanel createRoomCard(Room room) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(14, 14, 14, 14)));

        JPanel imagePanel = new RoomImagePanel();
        imagePanel.setPreferredSize(new Dimension(90, 92));

        JPanel details = new JPanel();
        details.setBackground(CARD);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel roomNumber = new JLabel("Room " + room.number);
        roomNumber.setForeground(GOLD_LIGHT);
        roomNumber.setFont(new Font("SansSerif", Font.BOLD, 17));

        JLabel roomType = new JLabel(room.type);
        roomType.setForeground(TEXT);
        roomType.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel price = new JLabel("₹ " + room.price + " / night");
        price.setForeground(TEXT);
        price.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel status = new JLabel("●  " + room.status);
        status.setOpaque(true);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SansSerif", Font.BOLD, 11));
        status.setBorder(new EmptyBorder(5, 8, 5, 8));
        status.setBackground(getStatusColor(room.status));

        details.add(roomNumber);
        details.add(Box.createVerticalStrut(6));
        details.add(roomType);
        details.add(Box.createVerticalStrut(6));
        details.add(price);
        details.add(Box.createVerticalStrut(10));
        details.add(status);

        JPanel infoPanel = new JPanel(new BorderLayout(12, 0));
        infoPanel.setBackground(CARD);
        infoPanel.add(imagePanel, BorderLayout.WEST);
        infoPanel.add(details, BorderLayout.CENTER);

        JButton actionButton = createActionButton(getActionText(room.status), GOLD);
        actionButton.setPreferredSize(new Dimension(0, 38));
        actionButton.addActionListener(e -> performRoomAction(room));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(CARD);
        buttonPanel.setBorder(new EmptyBorder(14, 0, 0, 0));
        buttonPanel.add(actionButton, BorderLayout.CENTER);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        return card;
    }

    private class RoomImagePanel extends JPanel {
        public RoomImagePanel() {
            setBackground(new Color(95, 78, 60));
            setBorder(new LineBorder(new Color(150, 125, 90), 1, true));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            g2.setColor(new Color(200, 178, 145));
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(90, 110, 125));
            g2.fillRect(8, 8, 25, 30);
            g2.setColor(new Color(235, 220, 190));
            g2.fillRect(11, 11, 19, 24);
            g2.setColor(new Color(105, 72, 48));
            g2.fillRect(10, 52, w - 20, 28);
            g2.setColor(new Color(245, 238, 220));
            g2.fillRoundRect(14, 54, 24, 12, 4, 4);
            g2.setColor(new Color(175, 150, 115));
            g2.fillRect(38, 55, w - 52, 23);
            g2.dispose();
        }
    }

    private Color getStatusColor(String status) {
        switch (status) {
            case "Available": return GREEN;
            case "Checked In": return RED;
            case "Checked Out": return BLUE;
            case "Maintenance": return ORANGE;
            default: return MUTED;
        }
    }

    private String getActionText(String status) {
        switch (status) {
            case "Available": return "Make Maintenance";
            case "Checked In": return "Check Out";
            case "Checked Out":
            case "Maintenance": return "Mark Available";
            default: return "Update";
        }
    }

    private void performRoomAction(Room room) {
        String newStatus = null;
        String message = "";

        if (room.status.equals("Available")) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to mark Room " + room.number + " for Maintenance?", "Maintenance", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                newStatus = "Maintenance";
                message = "Room " + room.number + " marked as Maintenance.";
            }
        } else if (room.status.equals("Checked In")) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to checkout Room " + room.number + "?", "Check Out", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                newStatus = "Checked Out";
                message = "Room " + room.number + " marked as Checked Out.";
            }
        } else if (room.status.equals("Checked Out") || room.status.equals("Maintenance")) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to mark Room " + room.number + " as Available?", "Room Status", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                newStatus = "Available";
                message = "Room " + room.number + " marked as Available.";
            }
        }

        if (newStatus != null) {
            boolean updated = updateRoomStatusInDatabase(room.number, newStatus);
            if (updated) {
                JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshRooms();
            }
        }
    }

    private boolean updateRoomStatusInDatabase(String roomNumber, String newStatus) {
        String sql = "UPDATE rooms SET status = ? WHERE room_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setString(2, roomNumber);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Unable to update room status.\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    private void updateSummary() {
        int total = rooms.size();
        int available = 0, checkedIn = 0, checkedOut = 0, maintenance = 0;

        for (Room room : rooms) {
            switch (room.status) {
                case "Available": available++; break;
                case "Checked In": checkedIn++; break;
                case "Checked Out": checkedOut++; break;
                case "Maintenance": maintenance++; break;
            }
        }

        if (totalLabel != null) totalLabel.setText(String.valueOf(total));
        if (availableLabel != null) availableLabel.setText(String.valueOf(available));
        if (checkedInLabel != null) checkedInLabel.setText(String.valueOf(checkedIn));
        if (checkedOutLabel != null) checkedOutLabel.setText(String.valueOf(checkedOut));
        if (maintenanceLabel != null) maintenanceLabel.setText(String.valueOf(maintenance));
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setForeground(GOLD_LIGHT);
        button.setBackground(DARK);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(new LineBorder(color, 1, true), new EmptyBorder(7, 15, 7, 15)));
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(35, 48, 70)); }
            public void mouseExited(MouseEvent e) { button.setBackground(DARK); }
        });
        return button;
    }

    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            if (dateTimeLabel != null) {
                String date = new SimpleDateFormat("dd MMM yyyy").format(new Date());
                String time = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                dateTimeLabel.setText("▣  " + date + "    " + time);
            }
        });
        clockTimer.start();
    }

    private static class Room {
        String number, type, status;
        int price;

        Room(String number, String type, int price, String status) {
            this.number = number;
            this.type = type;
            this.price = price;
            this.status = status;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ManageRoom();
        });
    }
}