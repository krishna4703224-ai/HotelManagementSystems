package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * =========================================================
 * ROYAL STAY - HOTEL MANAGEMENT SYSTEM
 * REPORT PAGE (DATABASE CONNECTED)
 * =========================================================
 */

public class Report extends JFrame {

    // =========================================================
    // DATABASE CONFIGURATION
    // =========================================================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your_password"; // Change to your MySQL password

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color DARK_NAVY = new Color(3, 18, 36);
    private static final Color HEADER_NAVY = new Color(2, 18, 39);
    private static final Color CARD = new Color(5, 29, 54);
    private static final Color FIELD = new Color(8, 36, 64);
    private static final Color GOLD = new Color(232, 181, 73);
    private static final Color LIGHT_GOLD = new Color(250, 211, 120);
    private static final Color WHITE = new Color(245, 247, 250);
    private static final Color LIGHT_TEXT = new Color(205, 214, 226);
    private static final Color BORDER = new Color(55, 88, 120);
    private static final Color GREEN = new Color(20, 198, 132);
    private static final Color RED = new Color(255, 75, 88);
    private static final Color BLUE = new Color(30, 154, 238);
    private static final Color ORANGE = new Color(244, 170, 27);

    // =========================================================
    // LIVE CLOCK LABEL
    // =========================================================

    private JLabel clockLabel;

    // =========================================================
    // SUMMARY METRICS VARIABLES (Loaded from DB)
    // =========================================================
    private String totalBookingsVal = "0";
    private String totalGuestsVal = "0";
    private String roomsOccupiedVal = "0";
    private String roomsAvailableVal = "0";
    private String totalRevenueVal = "₹ 0";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Report() {

        setTitle("Royal Stay - Reports");

        // Fetch metrics from Database before building UI components
        fetchSummaryDataFromDatabase();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =====================================================
        // BACKGROUND
        // =====================================================

        BackgroundPanel background = new BackgroundPanel("/reportBackground.png");
        background.setLayout(new BorderLayout());

        // =====================================================
        // HEADER
        // =====================================================

        background.add(createHeader(), BorderLayout.NORTH);

        // =====================================================
        // SCROLLABLE MAIN CONTENT
        // =====================================================

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(25, 30, 30, 30));

        // =====================================================
        // TITLE
        // =====================================================

        content.add(createTitlePanel());
        content.add(Box.createVerticalStrut(15));

        // =====================================================
        // FILTER
        // =====================================================

        content.add(createFilterPanel());
        content.add(Box.createVerticalStrut(18));

        // =====================================================
        // SUMMARY CARDS
        // =====================================================

        content.add(createSummaryCards());
        content.add(Box.createVerticalStrut(18));

        // =====================================================
        // CHARTS
        // =====================================================

        content.add(createChartsPanel());
        content.add(Box.createVerticalStrut(18));

        // =====================================================
        // RECENT BOOKINGS
        // =====================================================

        content.add(createRecentBookingsPanel());

        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        background.add(scrollPane, BorderLayout.CENTER);
        setContentPane(background);

        // =====================================================
        // START LIVE CLOCK
        // =====================================================

        startLiveClock();
    }

    // =========================================================
    // FETCH SUMMARY METRICS FROM DATABASE
    // =========================================================
    private void fetchSummaryDataFromDatabase() {
        String query = "SELECT " +
                "(SELECT COUNT(*) FROM bookings) AS total_bookings, " +
                "(SELECT COUNT(*) FROM guests) AS total_guests, " +
                "(SELECT COUNT(*) FROM rooms WHERE status='Occupied') AS rooms_occupied, " +
                "(SELECT COUNT(*) FROM rooms WHERE status='Available') AS rooms_available, " +
                "(SELECT SUM(amount) FROM payments) AS total_revenue";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                totalBookingsVal = String.valueOf(rs.getInt("total_bookings"));
                totalGuestsVal = String.valueOf(rs.getInt("total_guests"));
                roomsOccupiedVal = String.valueOf(rs.getInt("rooms_occupied"));
                roomsAvailableVal = String.valueOf(rs.getInt("rooms_available"));
                double rev = rs.getDouble("total_revenue");
                totalRevenueVal = "₹ " + String.format("%,.0f", rev);
            }
        } catch (SQLException e) {
            System.out.println("Database connection error for summary cards: " + e.getMessage());
        }
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(1000, 108));
        header.setBackground(HEADER_NAVY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GOLD));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 12));
        logoPanel.setOpaque(false);

        JLabel crown = new JLabel("♛");
        crown.setFont(new Font("Serif", Font.BOLD, 55));
        crown.setForeground(GOLD);
        logoPanel.add(crown);

        JPanel hotelText = new JPanel();
        hotelText.setOpaque(false);
        hotelText.setLayout(new BoxLayout(hotelText, BoxLayout.Y_AXIS));

        JLabel hotelName = new JLabel("ROYAL STAY");
        hotelName.setFont(new Font("Serif", Font.BOLD, 34));
        hotelName.setForeground(LIGHT_GOLD);

        JLabel hotelSub = new JLabel("HOTEL MANAGEMENT SYSTEM");
        hotelSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        hotelSub.setForeground(WHITE);

        hotelText.add(hotelName);
        hotelText.add(hotelSub);
        logoPanel.add(hotelText);
        header.add(logoPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 32));
        rightPanel.setOpaque(false);

        JButton backButton = createOutlineButton("← Back to Dashboard");

        // Connected Back to Dashboard Button Action
        backButton.addActionListener(e -> {
            new Dashboard(); // Opens Dashboard
            dispose();       // Closes Report window
        });

        rightPanel.add(backButton);

        JLabel calendar = new JLabel("▣");
        calendar.setFont(new Font("SansSerif", Font.BOLD, 23));
        calendar.setForeground(GOLD);
        rightPanel.add(calendar);

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        clockLabel.setForeground(WHITE);
        updateClock();
        rightPanel.add(clockLabel);

        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 32));
        separator.setForeground(new Color(130, 145, 165));
        rightPanel.add(separator);

        JLabel userIcon = new JLabel("●");
        userIcon.setFont(new Font("SansSerif", Font.BOLD, 37));
        userIcon.setForeground(GOLD);
        rightPanel.add(userIcon);

        JLabel admin = new JLabel("Admin ⌄");
        admin.setFont(new Font("SansSerif", Font.PLAIN, 16));
        admin.setForeground(WHITE);
        rightPanel.add(admin);

        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private void startLiveClock() {
        Timer timer = new Timer(1000, e -> updateClock());
        timer.setInitialDelay(0);
        timer.start();
    }

    private void updateClock() {
        if (clockLabel != null) {
            String currentDateTime = new SimpleDateFormat("dd MMM yyyy, hh:mm:ss a").format(new Date());
            clockLabel.setText(currentDateTime);
        }
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel heading = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        heading.setOpaque(false);

        JLabel icon = new JLabel("▥");
        icon.setFont(new Font("SansSerif", Font.BOLD, 40));
        icon.setForeground(GOLD);

        JLabel title = new JLabel("REPORT");
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(LIGHT_GOLD);

        heading.add(icon);
        heading.add(Box.createHorizontalStrut(18));
        heading.add(title);
        left.add(heading);

        JLabel subtitle = new JLabel("View detailed reports of your hotel operations from database");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(LIGHT_TEXT);
        subtitle.setBorder(new EmptyBorder(0, 58, 0, 0));
        left.add(subtitle);

        panel.add(left, BorderLayout.WEST);
        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new RoundedPanel(16, new Color(4, 28, 52, 245));
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 0, 8);

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        panel.add(createDateField("From Date", "01-04-2025"), gbc);

        gbc.gridx = 1;
        panel.add(createDateField("To Date", "15-04-2025"), gbc);

        gbc.gridx = 2;
        panel.add(createReportTypeField(), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(23, 15, 0, 5);

        JButton generate = createGoldButton("⌕  Generate Report");
        generate.addActionListener(e -> {
            fetchSummaryDataFromDatabase();
            JOptionPane.showMessageDialog(this, "Report refreshed from database!", "Royal Stay", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(generate, gbc);

        return panel;
    }

    private JPanel createDateField(String labelText, String value) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(WHITE);

        JTextField field = new JTextField(value);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(WHITE);
        field.setBackground(FIELD);
        field.setCaretColor(WHITE);
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
        field.setPreferredSize(new Dimension(200, 42));

        panel.add(label);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);
        return panel;
    }

    private JPanel createReportTypeField() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Report Type");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(WHITE);

        String[] options = {"All Reports", "Booking Report", "Guest Report", "Room Report", "Revenue Report"};
        JComboBox<String> combo = new JComboBox<>(options);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setForeground(WHITE);
        combo.setBackground(FIELD);
        combo.setOpaque(true);

        panel.add(label);
        panel.add(Box.createVerticalStrut(7));
        panel.add(combo);
        return panel;
    }

    private JPanel createSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 15, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(1000, 130));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        panel.add(createSummaryCard("▰", "Total Bookings", totalBookingsVal, "Live from DB", BLUE));
        panel.add(createSummaryCard("●", "Total Guests", totalGuestsVal, "Live from DB", GREEN));
        panel.add(createSummaryCard("▣", "Rooms Occupied", roomsOccupiedVal, "Live from DB", RED));
        panel.add(createSummaryCard("⇥", "Rooms Available", roomsAvailableVal, "Live from DB", BLUE));
        panel.add(createSummaryCard("₹", "Total Revenue", totalRevenueVal, "Live from DB", ORANGE));

        return panel;
    }

    private JPanel createSummaryCard(String icon, String title, String value, String percentage, Color accent) {
        JPanel card = new RoundedPanel(12, CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 1),
                new EmptyBorder(12, 12, 10, 12)
        ));
        card.setLayout(new BorderLayout(12, 0));

        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(60, 65));
        iconPanel.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 45));
        iconPanel.setBorder(BorderFactory.createLineBorder(accent));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        iconLabel.setForeground(accent);
        iconPanel.add(iconLabel);
        card.add(iconPanel, BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        titleLabel.setForeground(WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setForeground(LIGHT_GOLD);

        JLabel percentageLabel = new JLabel(percentage);
        percentageLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        percentageLabel.setForeground(GREEN);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(5));
        text.add(valueLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(percentageLabel);

        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 18, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(1000, 270));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));

        panel.add(createRoomStatusPanel());
        panel.add(createRevenuePanel());
        return panel;
    }

    private JPanel createRoomStatusPanel() {
        JPanel panel = new RoundedPanel(15, new Color(4, 28, 52, 245));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("▥  Room Status Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(WHITE);
        panel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(new DonutChart(), BorderLayout.WEST);

        JPanel legend = new JPanel();
        legend.setOpaque(false);
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));

        legend.add(createLegendRow(GREEN, "Available", roomsAvailableVal));
        legend.add(Box.createVerticalStrut(13));
        legend.add(createLegendRow(RED, "Occupied", roomsOccupiedVal));

        center.add(legend, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLegendRow(Color color, String name, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);

        JLabel dot = new JLabel("●");
        dot.setFont(new Font("SansSerif", Font.BOLD, 20));
        dot.setForeground(color);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setForeground(WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        valueLabel.setForeground(LIGHT_TEXT);

        row.add(dot, BorderLayout.WEST);
        row.add(nameLabel, BorderLayout.CENTER);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private JPanel createRevenuePanel() {
        JPanel panel = new RoundedPanel(15, new Color(4, 28, 52, 245));
        panel.setBorder(new EmptyBorder(15, 20, 10, 20));
        panel.setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("↗  Revenue Trend");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(WHITE);

        top.add(title, BorderLayout.WEST);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new RevenueChart(), BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // RECENT BOOKINGS (LOADED DYNAMICALLY FROM SQL)
    // =========================================================

    private JPanel createRecentBookingsPanel() {
        JPanel panel = new RoundedPanel(15, new Color(4, 28, 52, 245));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setLayout(new BorderLayout(0, 10));

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JLabel title = new JLabel("▤  Recent Bookings (From Database)");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(WHITE);
        heading.add(title, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);

        JButton print = createOutlineButton("▣  Print Report");
        JButton download = createOutlineButton("↓  Download PDF");
        buttons.add(print);
        buttons.add(download);
        heading.add(buttons, BorderLayout.EAST);

        panel.add(heading, BorderLayout.NORTH);

        String[] columns = {
                "#", "Booking ID", "Guest Name", "Room Number",
                "Room Type", "Check-In Date", "Check-Out Date", "Total Amount", "Status"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Query fetching live data from MySQL Workbench tables
        String query = "SELECT b.booking_id, g.guest_name, b.room_number, r.room_type, b.check_in, b.check_out, b.amount, b.status " +
                "FROM bookings b " +
                "JOIN guests g ON b.guest_id = g.guest_id " +
                "JOIN rooms r ON b.room_number = r.room_number";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int index = 1;
            while (rs.next()) {
                Object[] row = {
                        String.valueOf(index++),
                        rs.getString("booking_id"),
                        rs.getString("guest_name"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getString("check_in"),
                        rs.getString("check_out"),
                        "₹ " + rs.getDouble("amount"),
                        rs.getString("status")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println("Could not load bookings table data: " + e.getMessage());
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setForeground(WHITE);
        table.setBackground(new Color(5, 30, 56));
        table.setGridColor(new Color(25, 59, 89));
        table.setSelectionBackground(new Color(25, 60, 90));
        table.setSelectionForeground(WHITE);

        table.getTableHeader().setBackground(new Color(10, 39, 68));
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 8; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table.getColumnModel().getColumn(8).setCellRenderer(new StatusRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(25, 59, 89)));
        scroll.getViewport().setBackground(new Color(5, 30, 56));

        panel.add(scroll, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(1000, 275));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        return panel;
    }

    private JButton createGoldButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(GOLD);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(210, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createOutlineButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(LIGHT_GOLD);
        button.setBackground(new Color(5, 30, 55));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(145, 105, 35)),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        button.setPreferredSize(new Dimension(145, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 11));
            label.setOpaque(true);

            if (value != null && value.toString().equalsIgnoreCase("Checked In")) {
                label.setBackground(RED);
            } else {
                label.setBackground(BLUE);
            }
            label.setForeground(Color.WHITE);
            return label;
        }
    }

    static class DonutChart extends JPanel {
        DonutChart() {
            setOpaque(false);
            setPreferredSize(new Dimension(300, 215));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 35, y = 18, size = 165;
            int start = 0;
            int[] values = {60, 30, 5, 5};
            Color[] colors = {GREEN, RED, BLUE, ORANGE};

            for (int i = 0; i < values.length; i++) {
                int angle = values[i] * 360 / 100;
                g2.setColor(colors[i]);
                g2.fill(new Arc2D.Double(x, y, size, size, start, angle, Arc2D.PIE));
                start += angle;
            }

            g2.setColor(new Color(4, 28, 52));
            g2.fill(new Ellipse2D.Double(x + 44, y + 44, 77, 77));

            g2.setColor(WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String text = "Total Rooms";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, x + size / 2 - fm.stringWidth(text) / 2, y + 78);

            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            text = "20";
            fm = g2.getFontMetrics();
            g2.drawString(text, x + size / 2 - fm.stringWidth(text) / 2, y + 103);
            g2.dispose();
        }
    }

    static class RevenueChart extends JPanel {
        RevenueChart() {
            setOpaque(false);
            setPreferredSize(new Dimension(550, 210));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int left = 60, right = 20, top = 25, bottom = 40;
            int width = getWidth() - left - right;
            int height = getHeight() - top - bottom;

            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(35, 70, 100));
            for (int i = 0; i <= 5; i++) {
                int y = top + i * height / 5;
                g2.drawLine(left, y, left + width, y);
            }

            String[] labels = {"50,000", "40,000", "30,000", "20,000", "10,000", "0"};
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.setColor(WHITE);
            for (int i = 0; i < labels.length; i++) {
                int y = top + i * height / 5 + 4;
                g2.drawString(labels[i], 5, y);
            }

            int[] values = {14000, 24000, 32000, 27000, 34000, 43000, 31000};
            String[] dates = {"09 Apr", "10 Apr", "11 Apr", "12 Apr", "13 Apr", "14 Apr", "15 Apr"};
            int[] px = new int[values.length];
            int[] py = new int[values.length];

            for (int i = 0; i < values.length; i++) {
                px[i] = left + i * width / (values.length - 1);
                py[i] = top + height - (values[i] * height / 50000);
            }

            Polygon area = new Polygon();
            area.addPoint(px[0], top + height);
            for (int i = 0; i < px.length; i++) {
                area.addPoint(px[i], py[i]);
            }
            area.addPoint(px[px.length - 1], top + height);

            g2.setColor(new Color(232, 181, 73, 45));
            g2.fillPolygon(area);

            g2.setColor(GOLD);
            g2.setStroke(new BasicStroke(2.5f));
            for (int i = 0; i < px.length - 1; i++) {
                g2.drawLine(px[i], py[i], px[i + 1], py[i + 1]);
            }

            for (int i = 0; i < px.length; i++) {
                g2.setColor(GOLD);
                g2.fillOval(px[i] - 5, py[i] - 5, 10, 10);
                g2.setColor(new Color(4, 28, 52));
                g2.fillOval(px[i] - 2, py[i] - 2, 4, 4);
                g2.setColor(WHITE);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(dates[i], px[i] - fm.stringWidth(dates[i]) / 2, top + height + 22);
            }
            g2.dispose();
        }
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color background;

        RoundedPanel(int radius, Color background) {
            this.radius = radius;
            this.background = background;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        BackgroundPanel(String imagePath) {
            java.net.URL imageURL = BackgroundPanel.class.getResource(imagePath);
            if (imageURL != null) {
                backgroundImage = new ImageIcon(imageURL).getImage();
            }
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (backgroundImage != null) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                g2.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, this);
                g2.setColor(new Color(0, 12, 30, 145));
                g2.fillRect(0, 0, panelWidth, panelHeight);
            } else {
                g2.setColor(DARK_NAVY);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Report().setVisible(true));
    }
}