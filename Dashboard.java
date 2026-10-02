package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dashboard extends JFrame {

    private final Color SIDEBAR_COLOR = new Color(10, 22, 35);
    private final Color GOLD = new Color(220, 170, 95);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_TEXT = new Color(220, 220, 220);

    public Dashboard() {
        setTitle("Hotel Management System");
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

        JLabel hotelName = new JLabel("ROYAL STAY");
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

        homeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "You are already on Dashboard.", "Home", JOptionPane.INFORMATION_MESSAGE));
        roomButton.addActionListener(e -> { new ManageRoom(); dispose(); });
        guestButton.addActionListener(e -> { new CheckIn(); dispose(); });
        bookingButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reservation page is not connected yet.", "Reservation", JOptionPane.INFORMATION_MESSAGE));
        billingButton.addActionListener(e -> { new Billing(); dispose(); });
        reportButton.addActionListener(e -> { new Report().setVisible(true); dispose(); });

        // Connected Settings Button
        settingsButton.addActionListener(e -> {
            new Settings();
            dispose();
        });

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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(40, 60, 0, 40));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome to");
        welcome.setForeground(GOLD);
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 25));

        JLabel hotelTitle = new JLabel("ROYAL STAY HOTEL");
        hotelTitle.setForeground(WHITE);
        hotelTitle.setFont(new Font("Serif", Font.BOLD, 45));

        JLabel slogan = new JLabel("Comfort   |   Luxury   |   Experience");
        slogan.setForeground(LIGHT_TEXT);
        slogan.setFont(new Font("SansSerif", Font.PLAIN, 18));

        titlePanel.add(welcome);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(hotelTitle);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(slogan);

        topPanel.add(titlePanel, BorderLayout.WEST);

        JPanel datePanel = new JPanel();
        datePanel.setBackground(new Color(15, 25, 35, 220));
        datePanel.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        datePanel.setPreferredSize(new Dimension(210, 85));
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));

        JLabel date = new JLabel();
        date.setForeground(LIGHT_TEXT);
        date.setFont(new Font("SansSerif", Font.PLAIN, 13));
        date.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel time = new JLabel();
        time.setForeground(WHITE);
        time.setFont(new Font("SansSerif", Font.BOLD, 17));
        time.setAlignmentX(Component.CENTER_ALIGNMENT);

        datePanel.add(Box.createVerticalStrut(15));
        datePanel.add(date);
        datePanel.add(Box.createVerticalStrut(8));
        datePanel.add(time);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        Timer timer = new Timer(1000, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            date.setText(currentTime.format(dateFormatter));
            time.setText(currentTime.format(timeFormatter));
        });

        LocalDateTime currentTime = LocalDateTime.now();
        date.setText(currentTime.format(dateFormatter));
        time.setText(currentTime.format(timeFormatter));
        timer.start();

        topPanel.add(datePanel, BorderLayout.EAST);
        content.add(topPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 18, 20));

        JButton checkIn = createCard("⇥", "CHECK IN", "Register New Guest");
        JButton checkOut = createCard("⇥", "CHECK OUT", "Complete Guest Stay");
        JButton rooms = createCard("▣", "MANAGE ROOMS", "View & Update Rooms");
        JButton bookings = createCard("▤", "VIEW DETAILS", "View Guest Details");

        checkIn.addActionListener(e -> { new CheckIn(); dispose(); });
        checkOut.addActionListener(e -> { new CheckOut(); dispose(); });
        rooms.addActionListener(e -> { new ManageRoom(); dispose(); });
        bookings.addActionListener(e -> { new ViewGuestDetails(); dispose(); });

        cardsPanel.add(checkIn);
        cardsPanel.add(checkOut);
        cardsPanel.add(rooms);
        cardsPanel.add(bookings);

        JLabel footer = new JLabel("Thank you for choosing Royal Stay");
        footer.setForeground(new Color(230, 210, 180));
        footer.setFont(new Font("Serif", Font.ITALIC, 17));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JPanel bottomGroup = new JPanel();
        bottomGroup.setOpaque(false);
        bottomGroup.setLayout(new BoxLayout(bottomGroup, BoxLayout.Y_AXIS));

        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomGroup.add(cardsPanel);
        bottomGroup.add(Box.createVerticalStrut(10));
        bottomGroup.add(footer);
        bottomGroup.add(Box.createVerticalStrut(10));

        centerPanel.add(bottomGroup, BorderLayout.SOUTH);
        content.add(centerPanel, BorderLayout.CENTER);

        return content;
    }

    private JButton createCard(String icon, String title, String subtitle) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(205, 115));
        button.setBackground(new Color(10, 20, 30, 230));
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 110, 120), 1));
        button.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(GOLD);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(new Color(190, 190, 190));
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(iconLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitleLabel);

        button.add(textPanel, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(65, 55, 45));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(10, 20, 30, 230));
            }
        });

        return button;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}