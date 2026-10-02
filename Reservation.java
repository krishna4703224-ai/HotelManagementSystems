package hotel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * ============================================================
 * ROYAL STAY HOTEL MANAGEMENT SYSTEM
 * RESERVATION / PRE-BOOKING PAGE
 * ============================================================
 */
public class Reservation extends JFrame {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/hotel_management";

    private static final String DB_USER = "root";

    private static final String DB_PASSWORD = "YOUR_PASSWORD";

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color NAVY =
            new Color(4, 18, 38);

    private static final Color DARK_NAVY =
            new Color(5, 25, 50);

    private static final Color CARD =
            new Color(7, 31, 61);

    private static final Color CARD_2 =
            new Color(9, 39, 73);

    private static final Color GOLD =
            new Color(220, 170, 70);

    private static final Color LIGHT_GOLD =
            new Color(245, 205, 115);

    private static final Color WHITE =
            new Color(245, 247, 250);

    private static final Color MUTED =
            new Color(165, 181, 201);

    private static final Color BORDER =
            new Color(38, 70, 105);

    private static final Color GREEN =
            new Color(24, 180, 120);

    private static final Color RED =
            new Color(235, 75, 80);

    private static final Color BLUE =
            new Color(45, 145, 225);

    // =========================================================
    // FORM FIELDS
    // =========================================================

    private JTextField guestNameField;
    private JTextField phoneField;
    private JTextField idNumberField;

    private JComboBox<String> idTypeCombo;
    private JComboBox<String> roomTypeCombo;
    private JComboBox<Integer> guestsCombo;

    private JSpinner checkInSpinner;
    private JSpinner checkOutSpinner;

    private JTextArea specialRequestArea;

    // =========================================================
    // ROOM SELECTION
    // =========================================================

    private String selectedRoomType = "";
    private int selectedRoomNumber = -1;
    private double selectedRoomPrice = 0;

    private JLabel selectedRoomLabel;
    private JLabel selectedPriceLabel;
    private JLabel selectedDateLabel;
    private JLabel selectedGuestLabel;

    private JPanel selectedRoomDetailsPanel;

    // =========================================================
    // TABLE
    // =========================================================

    private JTable reservationTable;
    private DefaultTableModel reservationModel;

    // =========================================================
    // MAIN CONSTRUCTOR
    // =========================================================

    public Reservation() {

        setTitle("Royal Stay - Reservation");
        setSize(1500, 900);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createReservationTable();

        setContentPane(new BackgroundPanel());

        buildUI();

        loadRecentReservations();

        updateSelectedRoomDetails();
    }

    // =========================================================
    // DATABASE CONNECTION
    // =========================================================

    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );
    }

    // =========================================================
    // CREATE RESERVATION TABLE
    // =========================================================

    private void createReservationTable() {

        String sql =
                "CREATE TABLE IF NOT EXISTS reservations (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "booking_id VARCHAR(30) UNIQUE NOT NULL," +
                        "guest_name VARCHAR(100) NOT NULL," +
                        "phone VARCHAR(20) NOT NULL," +
                        "id_type VARCHAR(50)," +
                        "id_number VARCHAR(100)," +
                        "room_number INT NOT NULL," +
                        "room_type VARCHAR(50) NOT NULL," +
                        "check_in_date DATE NOT NULL," +
                        "check_out_date DATE NOT NULL," +
                        "number_of_guests INT DEFAULT 1," +
                        "special_request VARCHAR(500)," +
                        "total_amount DECIMAL(10,2) DEFAULT 0," +
                        "status VARCHAR(30) DEFAULT 'Confirmed'," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(sql);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to create reservation table.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // BUILD MAIN UI
    // =========================================================

    private void buildUI() {

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);

        // -----------------------------------------------------
        // TOP HEADER
        // -----------------------------------------------------

        main.add(createHeader(), BorderLayout.NORTH);

        // -----------------------------------------------------
        // SIDEBAR
        // -----------------------------------------------------

        main.add(createSidebar(), BorderLayout.WEST);

        // -----------------------------------------------------
        // CONTENT
        // -----------------------------------------------------

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        22,
                        20,
                        20,
                        25
                )
        );

        content.add(createTitlePanel(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);

        center.add(
                createReservationArea(),
                BorderLayout.CENTER
        );

        center.add(
                createRecentReservationsPanel(),
                BorderLayout.SOUTH
        );

        content.add(center, BorderLayout.CENTER);

        main.add(content, BorderLayout.CENTER);

        add(main);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(
                new Dimension(1500, 82)
        );

        header.setBackground(
                new Color(3, 16, 34)
        );

        header.setBorder(
                new MatteBorder(
                        0,
                        0,
                        1,
                        0,
                        GOLD
                )
        );

        // -----------------------------------------------------
        // LOGO
        // -----------------------------------------------------

        JPanel logoPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        18,
                        12
                )
        );

        logoPanel.setOpaque(false);

        JLabel logo = new JLabel("♛");

        logo.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        48
                )
        );

        logo.setForeground(GOLD);

        logoPanel.add(logo);

        JPanel hotelText = new JPanel();

        hotelText.setOpaque(false);

        hotelText.setLayout(
                new BoxLayout(
                        hotelText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel hotelName =
                new JLabel("ROYAL STAY");

        hotelName.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        27
                )
        );

        hotelName.setForeground(
                LIGHT_GOLD
        );

        JLabel subtitle =
                new JLabel(
                        "HOTEL MANAGEMENT SYSTEM"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                WHITE
        );

        hotelText.add(hotelName);
        hotelText.add(subtitle);

        logoPanel.add(hotelText);

        header.add(
                logoPanel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // RIGHT HEADER
        // -----------------------------------------------------

        JPanel right = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        20,
                        20
                )
        );

        right.setOpaque(false);

        JLabel date =
                new JLabel(
                        "▣  "
                                + new SimpleDateFormat(
                                "dd MMM yyyy, hh:mm a"
                        ).format(new Date())
                );

        date.setForeground(WHITE);

        date.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        JLabel manager =
                new JLabel(
                        "●  Admin  ▾"
                );

        manager.setForeground(WHITE);

        manager.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        right.add(date);
        right.add(manager);

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(250, 0)
        );

        sidebar.setBackground(
                new Color(3, 20, 42)
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setBorder(
                new MatteBorder(
                        0,
                        0,
                        0,
                        1,
                        BORDER
                )
        );

        sidebar.add(
                createMenuButton(
                        "⌂",
                        "Dashboard",
                        false
                )
        );

        sidebar.add(
                createMenuButton(
                        "▣",
                        "Room Management",
                        false
                )
        );

        sidebar.add(
                createMenuButton(
                        "♟",
                        "Guest Management",
                        false
                )
        );

        sidebar.add(
                createMenuButton(
                        "▣",
                        "Reservation",
                        true
                )
        );

        sidebar.add(
                createMenuButton(
                        "₹",
                        "Billing",
                        false
                )
        );

        sidebar.add(
                createMenuButton(
                        "▥",
                        "Reports",
                        false
                )
        );

        sidebar.add(
                createMenuButton(
                        "⚙",
                        "Settings",
                        false
                )
        );

        sidebar.add(Box.createVerticalGlue());

        JLabel quote =
                new JLabel(
                        "<html><div style='text-align:center;'>"
                                + "Comfort<br>"
                                + "Luxury<br>"
                                + "Experience"
                                + "</div></html>"
                );

        quote.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        quote.setFont(
                new Font(
                        "Serif",
                        Font.ITALIC,
                        19
                )
        );

        quote.setForeground(
                LIGHT_GOLD
        );

        quote.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        35,
                        10
                )
        );

        sidebar.add(quote);

        return sidebar;
    }

    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private JPanel createMenuButton(
            String icon,
            String text,
            boolean selected
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        58
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        0,
                        22,
                        0,
                        10
                )
        );

        if (selected) {

            panel.setBackground(GOLD);

        } else {

            panel.setBackground(
                    new Color(3, 20, 42)
            );
        }

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        iconLabel.setForeground(
                selected
                        ? NAVY
                        : LIGHT_GOLD
        );

        JLabel textLabel =
                new JLabel(text);

        textLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        textLabel.setForeground(
                selected
                        ? NAVY
                        : WHITE
        );

        JPanel inner =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                14,
                                0
                        )
                );

        inner.setOpaque(false);

        inner.add(iconLabel);
        inner.add(textLabel);

        panel.add(
                inner,
                BorderLayout.CENTER
        );

        if (!selected) {

            panel.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            panel.setBackground(
                                    new Color(
                                            10,
                                            40,
                                            70
                                    )
                            );
                        }

                        @Override
                        public void mouseExited(
                                MouseEvent e
                        ) {

                            panel.setBackground(
                                    new Color(
                                            3,
                                            20,
                                            42
                                    )
                            );
                        }
                    }
            );
        }

        return panel;
    }

    // =========================================================
    // TITLE
    // =========================================================

    private JPanel createTitlePanel() {

        JPanel title =
                new JPanel(
                        new BorderLayout()
                );

        title.setOpaque(false);

        JLabel heading =
                new JLabel(
                        "▣  RESERVATION"
                );

        heading.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        32
                )
        );

        heading.setForeground(
                LIGHT_GOLD
        );

        JLabel description =
                new JLabel(
                        "Book your stay in advance and enjoy a hassle-free experience."
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        description.setForeground(
                MUTED
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.add(heading);
        text.add(
                Box.createVerticalStrut(4)
        );
        text.add(description);

        title.add(
                text,
                BorderLayout.WEST
        );

        title.setBorder(
                new EmptyBorder(
                        0,
                        5,
                        18,
                        5
                )
        );

        return title;
    }

    // =========================================================
    // RESERVATION AREA
    // =========================================================

    private JPanel createReservationArea() {

        JPanel container =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                14,
                                0
                        )
                );

        container.setOpaque(false);

        container.add(
                createReservationForm()
        );

        container.add(
                createRoomArea()
        );

        return container;
    }

    // =========================================================
    // RESERVATION FORM
    // =========================================================

    private JPanel createReservationForm() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout()
        );

        JPanel header =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                0
                        )
                );

        header.setOpaque(false);

        JLabel icon =
                new JLabel("▤");

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25
                )
        );

        icon.setForeground(GOLD);

        JLabel title =
                new JLabel(
                        "Make a Reservation"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(WHITE);

        header.add(icon);
        header.add(title);

        card.add(
                header,
                BorderLayout.NORTH
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        7,
                        6,
                        7
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // -----------------------------------------------------
        // GUEST NAME
        // -----------------------------------------------------

        guestNameField =
                new JTextField();

        addField(
                form,
                gbc,
                "Guest Name *",
                guestNameField,
                0,
                0
        );

        // -----------------------------------------------------
        // PHONE
        // -----------------------------------------------------

        phoneField =
                new JTextField();

        addField(
                form,
                gbc,
                "Phone Number *",
                phoneField,
                1,
                0
        );

        // -----------------------------------------------------
        // ID TYPE
        // -----------------------------------------------------

        idTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "Aadhar Card",
                                "PAN Card",
                                "Passport",
                                "Driving Licence",
                                "Voter ID"
                        }
                );

        styleCombo(
                idTypeCombo
        );

        addField(
                form,
                gbc,
                "ID Type *",
                idTypeCombo,
                0,
                1
        );

        // -----------------------------------------------------
        // ID NUMBER
        // -----------------------------------------------------

        idNumberField =
                new JTextField();

        addField(
                form,
                gbc,
                "ID Number *",
                idNumberField,
                1,
                1
        );

        // -----------------------------------------------------
        // CHECK-IN
        // -----------------------------------------------------

        checkInSpinner =
                createDateSpinner();

        addField(
                form,
                gbc,
                "Check-in Date *",
                checkInSpinner,
                0,
                2
        );

        // -----------------------------------------------------
        // CHECK-OUT
        // -----------------------------------------------------

        checkOutSpinner =
                createDateSpinner();

        addField(
                form,
                gbc,
                "Check-out Date *",
                checkOutSpinner,
                1,
                2
        );

        // -----------------------------------------------------
        // NUMBER OF GUESTS
        // -----------------------------------------------------

        guestsCombo =
                new JComboBox<>(
                        new Integer[]{
                                1,
                                2,
                                3,
                                4,
                                5,
                                6,
                                7,
                                8
                        }
                );

        styleCombo(
                guestsCombo
        );

        addField(
                form,
                gbc,
                "Number of Guests *",
                guestsCombo,
                0,
                3
        );

        // -----------------------------------------------------
        // ROOM TYPE
        // -----------------------------------------------------

        roomTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "Select Room Type",
                                "Single",
                                "Double",
                                "Deluxe",
                                "Suite"
                        }
                );

        styleCombo(
                roomTypeCombo
        );

        roomTypeCombo.addActionListener(
                e -> {
                    selectedRoomType =
                            roomTypeCombo
                                    .getSelectedItem()
                                    .toString();

                    loadAvailableRooms();

                    updateSelectedRoomDetails();
                }
        );

        addField(
                form,
                gbc,
                "Room Type *",
                roomTypeCombo,
                1,
                3
        );

        // -----------------------------------------------------
        // SPECIAL REQUEST
        // -----------------------------------------------------

        specialRequestArea =
                new JTextArea(3, 20);

        specialRequestArea.setLineWrap(true);
        specialRequestArea.setWrapStyleWord(true);

        specialRequestArea.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        specialRequestArea.setForeground(WHITE);

        specialRequestArea.setBackground(
                new Color(
                        6,
                        33,
                        62
                )
        );

        specialRequestArea.setCaretColor(
                WHITE
        );

        specialRequestArea.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                7,
                                8,
                                7,
                                8
                        )
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0;

        JLabel requestLabel =
                createLabel(
                        "Special Request (Optional)"
                );

        form.add(
                requestLabel,
                gbc
        );

        gbc.gridy = 5;

        form.add(
                new JScrollPane(
                        specialRequestArea
                ),
                gbc
        );

        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        JPanel buttons =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                12,
                                0
                        )
                );

        buttons.setOpaque(false);

        JButton checkButton =
                createGoldButton(
                        "⌕   Check Availability"
                );

        checkButton.addActionListener(
                e -> checkAvailability()
        );

        JButton resetButton =
                createOutlineButton(
                        "↻   Reset"
                );

        resetButton.addActionListener(
                e -> resetForm()
        );

        buttons.add(checkButton);
        buttons.add(resetButton);

        gbc.gridy = 6;

        form.add(
                buttons,
                gbc
        );

        card.add(
                form,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // ROOM AREA
    // =========================================================

    private JPanel createRoomArea() {

        JPanel outer =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        outer.setOpaque(false);

        outer.add(
                createAvailableRoomsPanel(),
                BorderLayout.CENTER
        );

        selectedRoomDetailsPanel =
                createSelectedRoomPanel();

        outer.add(
                selectedRoomDetailsPanel,
                BorderLayout.SOUTH
        );

        return outer;
    }

    // =========================================================
    // AVAILABLE ROOMS
    // =========================================================

    private JPanel createAvailableRoomsPanel() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout()
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JLabel title =
                new JLabel(
                        "▣  Available Rooms"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(WHITE);

        top.add(
                title,
                BorderLayout.WEST
        );

        JComboBox<String> filter =
                new JComboBox<>(
                        new String[]{
                                "All Room Types",
                                "Single",
                                "Double",
                                "Deluxe",
                                "Suite"
                        }
                );

        styleCombo(filter);

        filter.addActionListener(
                e -> {

                    String selected =
                            filter
                                    .getSelectedItem()
                                    .toString();

                    if (selected.equals(
                            "All Room Types"
                    )) {

                        selectedRoomType = "";

                    } else {

                        selectedRoomType =
                                selected;
                    }

                    loadAvailableRooms();
                }
        );

        top.add(
                filter,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        JPanel rooms =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                0
                        )
                );

        rooms.setOpaque(false);

        rooms.add(
                createRoomCard(
                        "Single",
                        2500,
                        "/single.jpg"
                )
        );

        rooms.add(
                createRoomCard(
                        "Double",
                        3500,
                        "/double.jpg"
                )
        );

        rooms.add(
                createRoomCard(
                        "Deluxe",
                        5000,
                        "/deluxe.jpg"
                )
        );

        rooms.add(
                createRoomCard(
                        "Suite",
                        7000,
                        "/suite.jpg"
                )
        );

        card.add(
                rooms,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // ROOM CARD
    // =========================================================

    private JPanel createRoomCard(
            String type,
            double price,
            String imagePath
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                5
                        )
                );

        card.setBackground(
                CARD_2
        );

        card.setBorder(
                new LineBorder(
                        GOLD,
                        1,
                        true
                )
        );

        // -----------------------------------------------------
        // IMAGE
        // -----------------------------------------------------

        JLabel image =
                new JLabel();

        image.setPreferredSize(
                new Dimension(
                        150,
                        105
                )
        );

        image.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        ImageIcon icon =
                loadImage(
                        imagePath,
                        150,
                        105
                );

        if (icon != null) {

            image.setIcon(icon);

        } else {

            image.setText(
                    "ROOM"
            );

            image.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            18
                    )
            );

            image.setForeground(
                    LIGHT_GOLD
            );

            image.setBackground(
                    new Color(
                            15,
                            48,
                            75
                    )
            );

            image.setOpaque(true);
        }

        card.add(
                image,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // CONTENT
        // -----------------------------------------------------

        JPanel body =
                new JPanel();

        body.setOpaque(false);

        body.setLayout(
                new BoxLayout(
                        body,
                        BoxLayout.Y_AXIS
                )
        );

        body.setBorder(
                new EmptyBorder(
                        7,
                        8,
                        8,
                        8
                )
        );

        JLabel roomName =
                new JLabel(
                        type + " Room"
                );

        roomName.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        roomName.setForeground(
                WHITE
        );

        JLabel rate =
                new JLabel(
                        "₹ "
                                + String.format(
                                "%.0f",
                                price
                        )
                                + " / night"
                );

        rate.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        rate.setForeground(
                LIGHT_GOLD
        );

        JLabel available =
                new JLabel(
                        "  Available  "
                );

        available.setOpaque(true);

        available.setBackground(
                new Color(
                        18,
                        120,
                        90
                )
        );

        available.setForeground(
                WHITE
        );

        available.setBorder(
                new EmptyBorder(
                        5,
                        7,
                        5,
                        7
                )
        );

        available.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JButton select =
                createSmallButton(
                        "Select Room"
                );

        select.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        select.addActionListener(
                e -> selectRoom(
                        type,
                        price
                )
        );

        body.add(roomName);

        body.add(
                Box.createVerticalStrut(4)
        );

        body.add(rate);

        body.add(
                Box.createVerticalStrut(6)
        );

        body.add(available);

        body.add(
                Box.createVerticalStrut(8)
        );

        body.add(select);

        card.add(
                body,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // SELECT ROOM
    // =========================================================

    private void selectRoom(
            String type,
            double price
    ) {

        selectedRoomType = type;
        selectedRoomPrice = price;

        roomTypeCombo.setSelectedItem(
                type
        );

        int room =
                findAvailableRoom(type);

        selectedRoomNumber = room;

        updateSelectedRoomDetails();
    }

    // =========================================================
    // FIND AVAILABLE ROOM
    // =========================================================

    private int findAvailableRoom(
            String roomType
    ) {

        String sql =
                "SELECT room_number FROM rooms "
                        + "WHERE room_type = ? "
                        + "AND status = 'Available' "
                        + "ORDER BY room_number "
                        + "LIMIT 1";

        try (Connection con =
                     getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(
                    1,
                    roomType
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(
                        "room_number"
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return -1;
    }

    // =========================================================
    // SELECTED ROOM PANEL
    // =========================================================

    private JPanel createSelectedRoomPanel() {

        JPanel panel =
                createCard();

        panel.setPreferredSize(
                new Dimension(
                        0,
                        130
                )
        );

        panel.setLayout(
                new BorderLayout()
        );

        JLabel title =
                new JLabel(
                        "●  Selected Room Details"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        title.setForeground(
                WHITE
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        JPanel details =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        details.setOpaque(false);

        selectedRoomLabel =
                createDetailLabel(
                        "Room",
                        "-"
                );

        selectedPriceLabel =
                createDetailLabel(
                        "Price",
                        "-"
                );

        selectedDateLabel =
                createDetailLabel(
                        "Stay",
                        "-"
                );

        selectedGuestLabel =
                createDetailLabel(
                        "Guests",
                        "-"
                );

        details.add(
                selectedRoomLabel
        );

        details.add(
                selectedPriceLabel
        );

        details.add(
                selectedDateLabel
        );

        details.add(
                selectedGuestLabel
        );

        panel.add(
                details,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // DETAIL LABEL
    // =========================================================

    private JLabel createDetailLabel(
            String title,
            String value
    ) {

        JLabel label =
                new JLabel(
                        "<html><b>"
                                + title
                                + "</b><br>"
                                + value
                                + "</html>"
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                WHITE
        );

        return label;
    }

    // =========================================================
    // UPDATE SELECTED ROOM
    // =========================================================

    private void updateSelectedRoomDetails() {

        if (selectedRoomType == null
                || selectedRoomType.isEmpty()
                || selectedRoomType.equals(
                "Select Room Type"
        )) {

            selectedRoomLabel.setText(
                    "<html><b>Room</b><br>-</html>"
            );

            selectedPriceLabel.setText(
                    "<html><b>Price</b><br>-</html>"
            );

        } else {

            selectedRoomLabel.setText(
                    "<html><b>Room</b><br>"
                            + (
                            selectedRoomNumber > 0
                                    ? selectedRoomNumber
                                    : "Available"
                    )
                            + "</html>"
            );

            selectedPriceLabel.setText(
                    "<html><b>Price</b><br>₹ "
                            + String.format(
                            "%.0f",
                            selectedRoomPrice
                    )
                            + " / night</html>"
            );
        }

        selectedDateLabel.setText(
                "<html><b>Stay</b><br>"
                        + getDate(
                        checkInSpinner
                )
                        + " → "
                        + getDate(
                        checkOutSpinner
                )
                        + "</html>"
        );

        selectedGuestLabel.setText(
                "<html><b>Guests</b><br>"
                        + guestsCombo.getSelectedItem()
                        + "</html>"
        );
    }

    // =========================================================
    // RECENT RESERVATIONS
    // =========================================================

    private JPanel createRecentReservationsPanel() {

        JPanel card =
                createCard();

        card.setPreferredSize(
                new Dimension(
                        0,
                        260
                )
        );

        card.setLayout(
                new BorderLayout(
                        0,
                        10
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JLabel title =
                new JLabel(
                        "▣  Recent Reservations"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(
                WHITE
        );

        top.add(
                title,
                BorderLayout.WEST
        );

        JLabel viewAll =
                new JLabel(
                        "View All →"
                );

        viewAll.setForeground(
                LIGHT_GOLD
        );

        viewAll.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        top.add(
                viewAll,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        String[] columns = {
                "#",
                "Booking ID",
                "Guest Name",
                "Room Number",
                "Room Type",
                "Check-in Date",
                "Check-out Date",
                "Status",
                "Action"
        };

        reservationModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        reservationTable =
                new JTable(
                        reservationModel
                );

        reservationTable.setRowHeight(38);

        reservationTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        reservationTable.setForeground(
                WHITE
        );

        reservationTable.setBackground(
                new Color(
                        6,
                        29,
                        55
                )
        );

        reservationTable.setGridColor(
                BORDER
        );

        reservationTable.setSelectionBackground(
                new Color(
                        22,
                        65,
                        100
                )
        );

        reservationTable.setSelectionForeground(
                WHITE
        );

        reservationTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        reservationTable.getTableHeader()
                .setBackground(
                        new Color(
                                12,
                                43,
                                76
                        )
                );

        reservationTable.getTableHeader()
                .setForeground(
                        WHITE
                );

        reservationTable.getTableHeader()
                .setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                12
                        )
                );

        reservationTable.getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        new StatusRenderer()
                );

        reservationTable.getColumnModel()
                .getColumn(8)
                .setCellRenderer(
                        new ActionRenderer()
                );

        JScrollPane scroll =
                new JScrollPane(
                        reservationTable
                );

        scroll.setBorder(
                new LineBorder(
                        BORDER
                )
        );

        scroll.getViewport()
                .setBackground(
                        new Color(
                                6,
                                29,
                                55
                        )
                );

        card.add(
                scroll,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // LOAD RECENT RESERVATIONS
    // =========================================================

    private void loadRecentReservations() {

        if (reservationModel == null) {
            return;
        }

        reservationModel.setRowCount(0);

        String sql =
                "SELECT booking_id, guest_name, "
                        + "room_number, room_type, "
                        + "check_in_date, check_out_date, "
                        + "status "
                        + "FROM reservations "
                        + "ORDER BY id DESC LIMIT 5";

        try (Connection con =
                     getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            int number = 1;

            while (rs.next()) {

                reservationModel.addRow(
                        new Object[]{
                                number++,
                                rs.getString(
                                        "booking_id"
                                ),
                                rs.getString(
                                        "guest_name"
                                ),
                                rs.getInt(
                                        "room_number"
                                ),
                                rs.getString(
                                        "room_type"
                                ),
                                rs.getDate(
                                        "check_in_date"
                                ),
                                rs.getDate(
                                        "check_out_date"
                                ),
                                rs.getString(
                                        "status"
                                ),
                                "View   Cancel"
                        }
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // CHECK AVAILABILITY
    // =========================================================

    private void checkAvailability() {

        if (!validateForm()) {
            return;
        }

        String type =
                roomTypeCombo
                        .getSelectedItem()
                        .toString();

        int room =
                findAvailableRoom(type);

        if (room == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "No room of type "
                            + type
                            + " is currently available.",
                    "Room Not Available",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        selectedRoomType = type;

        selectedRoomNumber = room;

        selectedRoomPrice =
                getRoomPrice(type);

        updateSelectedRoomDetails();

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Room "
                                + room
                                + " is available.\n\n"
                                + "Do you want to confirm this reservation?",
                        "Room Available",
                        JOptionPane.YES_NO_OPTION
                );

        if (result ==
                JOptionPane.YES_OPTION) {

            saveReservation();
        }
    }

    // =========================================================
    // VALIDATE FORM
    // =========================================================

    private boolean validateForm() {

        if (guestNameField
                .getText()
                .trim()
                .isEmpty()) {

            showError(
                    "Please enter guest name."
            );

            return false;
        }

        if (phoneField
                .getText()
                .trim()
                .isEmpty()) {

            showError(
                    "Please enter phone number."
            );

            return false;
        }

        if (idNumberField
                .getText()
                .trim()
                .isEmpty()) {

            showError(
                    "Please enter ID number."
            );

            return false;
        }

        String type =
                roomTypeCombo
                        .getSelectedItem()
                        .toString();

        if (type.equals(
                "Select Room Type"
        )) {

            showError(
                    "Please select room type."
            );

            return false;
        }

        Date checkIn =
                (Date) checkInSpinner
                        .getValue();

        Date checkOut =
                (Date) checkOutSpinner
                        .getValue();

        if (!checkOut.after(checkIn)) {

            showError(
                    "Check-out date must be after check-in date."
            );

            return false;
        }

        return true;
    }

    // =========================================================
    // SAVE RESERVATION
    // =========================================================

    private void saveReservation() {

        if (selectedRoomNumber == -1) {

            selectedRoomNumber =
                    findAvailableRoom(
                            selectedRoomType
                    );
        }

        if (selectedRoomNumber == -1) {

            showError(
                    "Please select an available room."
            );

            return;
        }

        String bookingId =
                generateBookingId();

        Date checkIn =
                (Date) checkInSpinner
                        .getValue();

        Date checkOut =
                (Date) checkOutSpinner
                        .getValue();

        long nights =
                ChronoUnit.DAYS.between(
                        checkIn.toInstant()
                                .atZone(
                                        java.time.ZoneId
                                                .systemDefault()
                                )
                                .toLocalDate(),

                        checkOut.toInstant()
                                .atZone(
                                        java.time.ZoneId
                                                .systemDefault()
                                )
                                .toLocalDate()
                );

        if (nights <= 0) {
            nights = 1;
        }

        double total =
                selectedRoomPrice
                        * nights;

        String sql =
                "INSERT INTO reservations "
                        + "(booking_id, guest_name, phone, "
                        + "id_type, id_number, room_number, "
                        + "room_type, check_in_date, "
                        + "check_out_date, number_of_guests, "
                        + "special_request, total_amount, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con =
                     getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(
                    1,
                    bookingId
            );

            ps.setString(
                    2,
                    guestNameField
                            .getText()
                            .trim()
            );

            ps.setString(
                    3,
                    phoneField
                            .getText()
                            .trim()
            );

            ps.setString(
                    4,
                    idTypeCombo
                            .getSelectedItem()
                            .toString()
            );

            ps.setString(
                    5,
                    idNumberField
                            .getText()
                            .trim()
            );

            ps.setInt(
                    6,
                    selectedRoomNumber
            );

            ps.setString(
                    7,
                    selectedRoomType
            );

            ps.setDate(
                    8,
                    new java.sql.Date(
                            checkIn.getTime()
                    )
            );

            ps.setDate(
                    9,
                    new java.sql.Date(
                            checkOut.getTime()
                    )
            );

            ps.setInt(
                    10,
                    Integer.parseInt(
                            guestsCombo
                                    .getSelectedItem()
                                    .toString()
                    )
            );

            ps.setString(
                    11,
                    specialRequestArea
                            .getText()
                            .trim()
            );

            ps.setDouble(
                    12,
                    total
            );

            ps.setString(
                    13,
                    "Confirmed"
            );

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Reservation Confirmed!\n\n"
                            + "Booking ID: "
                            + bookingId
                            + "\nRoom Number: "
                            + selectedRoomNumber
                            + "\nRoom Type: "
                            + selectedRoomType
                            + "\nNights: "
                            + nights
                            + "\nTotal Amount: ₹"
                            + String.format(
                            "%.2f",
                            total
                    ),
                    "Reservation Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadRecentReservations();

            resetForm();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Reservation failed.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // GENERATE BOOKING ID
    // =========================================================

    private String generateBookingId() {

        return "RS"
                + new SimpleDateFormat(
                "yyyyMMddHHmmss"
        ).format(
                new Date()
        );
    }

    // =========================================================
    // ROOM PRICE
    // =========================================================

    private double getRoomPrice(
            String type
    ) {

        switch (type) {

            case "Single":
                return 2500;

            case "Double":
                return 3500;

            case "Deluxe":
                return 5000;

            case "Suite":
                return 7000;

            default:
                return 0;
        }
    }

    // =========================================================
    // RESET
    // =========================================================

    private void resetForm() {

        guestNameField.setText("");

        phoneField.setText("");

        idNumberField.setText("");

        idTypeCombo.setSelectedIndex(0);

        roomTypeCombo.setSelectedIndex(0);

        guestsCombo.setSelectedIndex(0);

        specialRequestArea.setText("");

        checkInSpinner.setValue(
                new Date()
        );

        Date tomorrow =
                new Date(
                        System.currentTimeMillis()
                                + 24L
                                * 60
                                * 60
                                * 1000
                );

        checkOutSpinner.setValue(
                tomorrow
        );

        selectedRoomType = "";

        selectedRoomNumber = -1;

        selectedRoomPrice = 0;

        updateSelectedRoomDetails();
    }

    // =========================================================
    // LOAD AVAILABLE ROOMS
    // =========================================================

    private void loadAvailableRooms() {

        if (selectedRoomType == null
                || selectedRoomType.isEmpty()) {

            return;
        }

        selectedRoomNumber =
                findAvailableRoom(
                        selectedRoomType
                );

        selectedRoomPrice =
                getRoomPrice(
                        selectedRoomType
                );

        updateSelectedRoomDetails();
    }

    // =========================================================
    // DATE SPINNER
    // =========================================================

    private JSpinner createDateSpinner() {

        JSpinner spinner =
                new JSpinner(
                        new SpinnerDateModel(
                                new Date(),
                                null,
                                null,
                                java.util.Calendar.DAY_OF_MONTH
                        )
                );

        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(
                        spinner,
                        "dd-MM-yyyy"
                );

        spinner.setEditor(editor);

        spinner.setPreferredSize(
                new Dimension(
                        250,
                        38
                )
        );

        editor.getTextField()
                .setForeground(
                        WHITE
                );

        editor.getTextField()
                .setBackground(
                        new Color(
                                6,
                                33,
                                62
                        )
                );

        editor.getTextField()
                .setCaretColor(
                        WHITE
                );

        editor.getTextField()
                .setBorder(
                        new CompoundBorder(
                                new LineBorder(
                                        BORDER
                                ),
                                new EmptyBorder(
                                        5,
                                        8,
                                        5,
                                        8
                                )
                        )
                );

        spinner.addChangeListener(
                e -> updateSelectedRoomDetails()
        );

        return spinner;
    }

    // =========================================================
    // ADD FIELD
    // =========================================================

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            String labelText,
            JComponent component,
            int x,
            int y
    ) {

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 1;

        JPanel field =
                new JPanel();

        field.setOpaque(false);

        field.setLayout(
                new BoxLayout(
                        field,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel label =
                createLabel(
                        labelText
                );

        field.add(label);

        field.add(
                Box.createVerticalStrut(5)
        );

        if (component instanceof JTextField) {

            styleTextField(
                    (JTextField) component
            );

        }

        field.add(component);

        panel.add(
                field,
                gbc
        );
    }

    // =========================================================
    // LABEL
    // =========================================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                WHITE
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private void styleTextField(
            JTextField field
    ) {

        field.setPreferredSize(
                new Dimension(
                        250,
                        38
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        38
                )
        );

        field.setBackground(
                new Color(
                        6,
                        33,
                        62
                )
        );

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                WHITE
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        field.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                5,
                                9,
                                5,
                                9
                        )
                )
        );
    }

    // =========================================================
    // COMBO STYLE
    // =========================================================

    private void styleCombo(
            JComboBox<?> combo
    ) {

        combo.setPreferredSize(
                new Dimension(
                        250,
                        38
                )
        );

        combo.setBackground(
                new Color(
                        6,
                        33,
                        62
                )
        );

        combo.setForeground(
                WHITE
        );

        combo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        combo.setBorder(
                new LineBorder(
                        BORDER
                )
        );
    }

    // =========================================================
    // GOLD BUTTON
    // =========================================================

    private JButton createGoldButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(GOLD);

        button.setForeground(
                NAVY
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        return button;
    }

    // =========================================================
    // OUTLINE BUTTON
    // =========================================================

    private JButton createOutlineButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(
                CARD
        );

        button.setForeground(
                WHITE
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(
                        GOLD,
                        1,
                        true
                )
        );

        return button;
    }

    // =========================================================
    // SMALL BUTTON
    // =========================================================

    private JButton createSmallButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(
                new Color(
                        8,
                        31,
                        55
                )
        );

        button.setForeground(
                LIGHT_GOLD
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(
                        GOLD,
                        1,
                        true
                )
        );

        return button;
    }

    // =========================================================
    // CARD
    // =========================================================

    private JPanel createCard() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                CARD
        );

        panel.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        return panel;
    }

    // =========================================================
    // IMAGE LOADER
    // =========================================================

    private ImageIcon loadImage(
            String path,
            int width,
            int height
    ) {

        java.net.URL url =
                getClass()
                        .getResource(path);

        if (url == null) {
            return null;
        }

        Image image =
                new ImageIcon(
                        url
                ).getImage();

        Image resized =
                image.getScaledInstance(
                        width,
                        height,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(
                resized
        );
    }

    // =========================================================
    // DATE FORMAT
    // =========================================================

    private String getDate(
            JSpinner spinner
    ) {

        Date date =
                (Date) spinner.getValue();

        return new SimpleDateFormat(
                "dd-MM-yyyy"
        ).format(date);
    }

    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Required Information",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =========================================================
    // STATUS RENDERER
    // =========================================================

    private static class StatusRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setOpaque(true);

            String status =
                    value == null
                            ? ""
                            : value.toString();

            if (status.equals(
                    "Confirmed"
            )) {

                label.setBackground(
                        new Color(
                                15,
                                120,
                                90
                        )
                );

                label.setForeground(WHITE);

            } else if (status.equals(
                    "Pending"
            )) {

                label.setBackground(
                        new Color(
                                210,
                                150,
                                30
                        )
                );

                label.setForeground(
                        NAVY
                );

            } else {

                label.setBackground(
                        new Color(
                                80,
                                90,
                                105
                        )
                );

                label.setForeground(WHITE);
            }

            return label;
        }
    }

    // =========================================================
    // ACTION RENDERER
    // =========================================================

    private static class ActionRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setForeground(
                    LIGHT_GOLD
            );

            label.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            11
                    )
            );

            return label;
        }
    }

    // =========================================================
    // BACKGROUND PANEL
    // =========================================================

    private static class BackgroundPanel
            extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {

            setLayout(
                    new BorderLayout()
            );

            java.net.URL url =
                    getClass()
                            .getResource(
                                    "/reservation_bg.png"
                            );

            if (url != null) {

                backgroundImage =
                        new ImageIcon(
                                url
                        ).getImage();
            }
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            if (backgroundImage != null) {

                g2.drawImage(
                        backgroundImage,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );

            } else {

                g2.setColor(
                        new Color(
                                3,
                                17,
                                35
                        )
                );

                g2.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );
            }

            // Dark overlay
            g2.setColor(
                    new Color(
                            0,
                            10,
                            25,
                            150
                    )
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            g2.dispose();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (Exception ignored) {
                    }

                    new Reservation()
                            .setVisible(true);
                }
        );
    }
}