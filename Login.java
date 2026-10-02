package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class Login extends JFrame {

    private static final Color GOLD = new Color(220, 170, 70);
    private static final Color LIGHT_GOLD = new Color(245, 210, 135);
    private static final Color NAVY = new Color(5, 15, 30);
    private static final Color WHITE = Color.WHITE;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JCheckBox rememberMe;

    public Login() {
        setTitle("ROYAL STAY HOTEL - Login");
        setSize(1400, 800);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        BackgroundPanel backgroundPanel = new BackgroundPanel("/resource/loginPage.png");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel overlay = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 55));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        overlay.setOpaque(false);
        GridBagConstraints overlayGbc = new GridBagConstraints();
        overlayGbc.fill = GridBagConstraints.BOTH;
        overlayGbc.weightx = 1.0;
        overlayGbc.weighty = 1.0;
        backgroundPanel.add(overlay, overlayGbc);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.weighty = 1.0;
        mainGbc.insets = new Insets(30, 50, 30, 50);

        JPanel loginCard = new RoundedPanel(30, new Color(5, 15, 30, 238));
        loginCard.setLayout(new GridBagLayout());
        loginCard.setBorder(new EmptyBorder(35, 55, 30, 55));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx = 0;
        cardGbc.weightx = 1.0;
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.anchor = GridBagConstraints.CENTER;

        JLabel cardCrown = new JLabel("♛");
        cardCrown.setFont(new Font("Serif", Font.PLAIN, 60));
        cardCrown.setForeground(GOLD);
        cardCrown.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 0;
        cardGbc.insets = new Insets(0, 0, 5, 0);
        loginCard.add(cardCrown, cardGbc);

        JLabel cardHotelName = new JLabel("ROYAL STAY");
        cardHotelName.setFont(new Font("Serif", Font.BOLD, 34));
        cardHotelName.setForeground(LIGHT_GOLD);
        cardHotelName.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 1;
        cardGbc.insets = new Insets(0, 0, 4, 0);
        loginCard.add(cardHotelName, cardGbc);

        JLabel cardHotel = new JLabel("━━  HOTEL  ━━");
        cardHotel.setFont(new Font("Serif", Font.PLAIN, 17));
        cardHotel.setForeground(GOLD);
        cardHotel.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(0, 0, 30, 0);
        loginCard.add(cardHotel, cardGbc);

        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(new Font("Serif", Font.BOLD, 34));
        welcome.setForeground(WHITE);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 3;
        cardGbc.insets = new Insets(0, 0, 8, 0);
        loginCard.add(welcome, cardGbc);

        JLabel subtitle = new JLabel("Login to your account");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 17));
        subtitle.setForeground(new Color(190, 195, 205));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 4;
        cardGbc.insets = new Insets(0, 0, 28, 0);
        loginCard.add(subtitle, cardGbc);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridx = 0;
        fieldGbc.weightx = 1.0;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        fieldGbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = createLabel("Username");
        fieldGbc.gridy = 0;
        fieldGbc.insets = new Insets(0, 0, 8, 0);
        fieldsPanel.add(usernameLabel, fieldGbc);

        usernameField = createTextField();
        fieldGbc.gridy = 1;
        fieldGbc.insets = new Insets(0, 0, 20, 0);
        fieldsPanel.add(usernameField, fieldGbc);

        JLabel passwordLabel = createLabel("Password");
        fieldGbc.gridy = 2;
        fieldGbc.insets = new Insets(0, 0, 8, 0);
        fieldsPanel.add(passwordLabel, fieldGbc);

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        fieldGbc.gridy = 3;
        fieldGbc.insets = new Insets(0, 0, 0, 0);
        fieldsPanel.add(passwordField, fieldGbc);

        cardGbc.gridy = 5;
        cardGbc.insets = new Insets(0, 0, 0, 0);
        loginCard.add(fieldsPanel, cardGbc);

        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);

        rememberMe = new JCheckBox("Remember Me");
        rememberMe.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rememberMe.setForeground(WHITE);
        rememberMe.setOpaque(false);
        rememberMe.setFocusPainted(false);

        JButton forgotPassword = new JButton("Forgot Password?");
        forgotPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        forgotPassword.setForeground(GOLD);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setBorderPainted(false);
        forgotPassword.setFocusPainted(false);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

        forgotPassword.addActionListener(e -> JOptionPane.showMessageDialog(this, "Please contact the hotel administrator.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE));

        optionsPanel.add(rememberMe, BorderLayout.WEST);
        optionsPanel.add(forgotPassword, BorderLayout.EAST);

        cardGbc.gridy = 6;
        cardGbc.insets = new Insets(15, 0, 22, 0);
        loginCard.add(optionsPanel, cardGbc);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 19));
        loginButton.setForeground(NAVY);
        loginButton.setBackground(new Color(235, 185, 75));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(400, 55));

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(255, 210, 110));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(235, 185, 75));
            }
        });

        loginButton.addActionListener(e -> loginUser());

        cardGbc.gridy = 7;
        cardGbc.insets = new Insets(0, 0, 18, 0);
        loginCard.add(loginButton, cardGbc);

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 8;
        cardGbc.insets = new Insets(0, 0, 10, 0);
        loginCard.add(messageLabel, cardGbc);

        JLabel bottomLine = new JLabel("━━━   Your Stay, Our Priority   ━━━");
        bottomLine.setFont(new Font("Serif", Font.ITALIC, 16));
        bottomLine.setForeground(new Color(220, 220, 220));
        bottomLine.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 9;
        cardGbc.insets = new Insets(0, 0, 0, 0);
        loginCard.add(bottomLine, cardGbc);

        mainGbc.gridx = 1;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1.05;
        mainGbc.fill = GridBagConstraints.VERTICAL;
        mainGbc.anchor = GridBagConstraints.CENTER;

        overlay.add(loginCard, mainGbc);

        setContentPane(backgroundPanel);
        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("1234")) {
            messageLabel.setForeground(new Color(120, 230, 150));
            messageLabel.setText("Login Successful!");

            JOptionPane.showMessageDialog(this, "Welcome to ROYAL STAY HOTEL!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                Dashboard dashboard = new Dashboard();
                dashboard.setLocationRelativeTo(null);
                dashboard.setVisible(true);
                dashboard.revalidate();
                dashboard.repaint();
            });

            dispose();

        } else {
            messageLabel.setForeground(new Color(255, 120, 120));
            messageLabel.setText("Invalid username or password!");
            passwordField.setText("");
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(WHITE);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(WHITE);
        field.setCaretColor(GOLD);
        field.setBackground(new Color(8, 23, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 170, 185), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        field.setPreferredSize(new Dimension(400, 55));
        field.setMinimumSize(new Dimension(400, 55));
        field.setMaximumSize(new Dimension(400, 55));
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color panelColor;

        public RoundedPanel(int radius, Color panelColor) {
            this.radius = radius;
            this.panelColor = panelColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(panelColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(new Color(220, 170, 70));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            URL imageURL = BackgroundPanel.class.getResource(imagePath);
            if (imageURL != null) {
                backgroundImage = new ImageIcon(imageURL).getImage();
            } else {
                System.out.println("Background image not found: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2.setColor(NAVY);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}