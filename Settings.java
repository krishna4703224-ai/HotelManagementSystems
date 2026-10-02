package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Settings extends JFrame {

    private final Color SIDEBAR_COLOR = new Color(10, 22, 35);
    private final Color GOLD = new Color(220, 170, 95);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_TEXT = new Color(220, 220, 220);

    public Settings() {
        setTitle(HotelConfig.getHotelName() + " - Settings");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        backgroundPanel.add(sidebar, BorderLayout.WEST);

        JPanel contentPanel = createMainContent();
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(270, 700));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BorderLayout());

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_COLOR);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(35, 20, 20, 20));

        JLabel hotelIcon = new JLabel("★");
        hotelIcon.setForeground(GOLD);
        hotelIcon.setFont(new Font("Serif", Font.BOLD, 42));
        hotelIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fetching hotel name dynamically from HotelConfig
        JLabel hotelName = new JLabel(HotelConfig.getHotelName());
        hotelName.setForeground(GOLD);
        hotelName.setFont(new Font("Serif", Font.BOLD, 28));
        hotelName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hotelSubtitle = new JLabel("HOTEL MANAGEMENT SYSTEM");
        hotelSubtitle.setForeground(LIGHT_TEXT);
        hotelSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        hotelSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(hotelIcon);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(hotelName);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(hotelSubtitle);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(SIDEBAR_COLOR);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JButton homeButton = createMenuButton("⌂   Home");
        JButton roomButton = createMenuButton("▣   Room Management");
        JButton guestButton = createMenuButton("♙   Guest Management");
        JButton bookingButton = createMenuButton("▣   Reservation");
        JButton billingButton = createMenuButton("▤   Billing");
        JButton reportButton = createMenuButton("▥   Reports");
        JButton settingsButton = createMenuButton("⚙   Settings");

        menuPanel.add(homeButton);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(roomButton);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(guestButton);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(bookingButton);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(billingButton);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(reportButton);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(settingsButton);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // Navigation Actions
        homeButton.addActionListener(e -> { new Dashboard(); dispose(); });
        roomButton.addActionListener(e -> { new ManageRoom(); dispose(); });
        guestButton.addActionListener(e -> { new CheckIn(); dispose(); });
        billingButton.addActionListener(e -> { new Billing(); dispose(); });
        reportButton.addActionListener(e -> { new Report().setVisible(true); dispose(); });
        settingsButton.addActionListener(e -> { /* Already on settings */ });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(SIDEBAR_COLOR);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JLabel line = new JLabel("────────────");
        line.setForeground(GOLD);
        line.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel better = new JLabel("Better Stays");
        better.setForeground(LIGHT_TEXT);
        better.setFont(new Font("SansSerif", Font.PLAIN, 14));
        better.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brighter = new JLabel("Brighter Days");
        brighter.setForeground(WHITE);
        brighter.setFont(new Font("Serif", Font.ITALIC, 15));
        brighter.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(line);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(better);
        bottomPanel.add(brighter);
        bottomPanel.add(Box.createVerticalStrut(30));

        sidebar.add(bottomPanel, BorderLayout.SOUTH);
        return sidebar;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setForeground(WHITE);
        button.setBackground(SIDEBAR_COLOR);
        button.setBorder(new EmptyBorder(10, 20, 10, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 82, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIDEBAR_COLOR);
            }
        });
        return button;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("SYSTEM SETTINGS & CONFIGURATION");
        titleLabel.setForeground(GOLD);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        titleLabel.setBorder(new EmptyBorder(40, 50, 20, 40));
        content.add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2, 2, 25, 25));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 50, 40, 50));

        // 1. General Settings Card (Interactive)
        JPanel generalCard = createSettingCard("⚙ General Settings", "Update hotel name, address, and contact info.");
        generalCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generalCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openGeneralSettingsDialog();
            }
        });
        panel.add(generalCard);

        // 2. Room Configuration Card
        JPanel roomCard = createSettingCard("🛏 Room Configuration", "Manage room types, base prices, and rates.");
        roomCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        roomCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRoomPriceDialog();
            }
        });
        panel.add(roomCard);

        // 3. User Management Card
        panel.add(createSettingCard("👥 User & Role Management", "Add staff accounts and handle permissions."));

        // 4. Security Card
        panel.add(createSettingCard("🛡 Database Backup & Security", "Backup records and update system security."));

        content.add(panel, BorderLayout.CENTER);
        return content;
    }

    private JPanel createSettingCard(String title, String desc) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(15, 25, 35, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 110, 120), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel tLabel = new JLabel(title);
        tLabel.setForeground(GOLD);
        tLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel dLabel = new JLabel(desc);
        dLabel.setForeground(LIGHT_TEXT);
        dLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        card.add(tLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(dLabel);
        return card;
    }

    private void openGeneralSettingsDialog() {
        JTextField nameField = new JTextField(HotelConfig.getHotelName(), 20);
        JTextField addressField = new JTextField(HotelConfig.getHotelAddress(), 20);
        JTextField phoneField = new JTextField(HotelConfig.getHotelPhone(), 20);

        JPanel myPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        myPanel.add(new JLabel("Hotel Name:"));
        myPanel.add(nameField);
        myPanel.add(new JLabel("Hotel Address:"));
        myPanel.add(addressField);
        myPanel.add(new JLabel("Mobile Number:"));
        myPanel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, myPanel,
                "Edit General Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Updating globally in HotelConfig
            HotelConfig.setHotelName(nameField.getText().trim());
            HotelConfig.setHotelAddress(addressField.getText().trim());
            HotelConfig.setHotelPhone(phoneField.getText().trim());

            JOptionPane.showMessageDialog(this, "General Settings Updated Successfully!");

            // Refresh current settings window to display the new name immediately
            dispose();
            new Settings();
        }
    }

    private void openRoomPriceDialog() {
        String[] roomTypes = {"Single Room", "Double Room", "Deluxe Suite", "Executive Suite"};
        String selectedType = (String) JOptionPane.showInputDialog(this,
                "Select Room Type to Update Price:",
                "Room Configuration",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roomTypes,
                roomTypes[0]);

        if (selectedType != null) {
            String newPriceStr = JOptionPane.showInputDialog(this, "Enter new base price for " + selectedType + ":");
            if (newPriceStr != null && !newPriceStr.trim().isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(newPriceStr.trim());
                    JOptionPane.showMessageDialog(this, "Base price for " + selectedType + " updated to $" + newPrice + " successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price entered. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            java.net.URL imageURL = Dashboard.class.getResource("/resource/background.png");
            if (imageURL != null) {
                backgroundImage = new ImageIcon(imageURL).getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2.setColor(new Color(0, 0, 0, 85));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            } else {
                g.setColor(SIDEBAR_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}