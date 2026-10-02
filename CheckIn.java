package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckIn extends JFrame {

    // ================= COLORS =================

    Color gold = new Color(212, 175, 55);
    Color dark = new Color(15, 23, 42);
    Color white = Color.WHITE;

    // ================= FORM FIELDS =================

    JTextField guestNameField;
    JTextField phoneField;
    JComboBox<String> roomNumberCombo;
    JSpinner dateSpinner;
    JTextField guestsField;

    JComboBox<String> idTypeCombo;
    JComboBox<String> roomTypeCombo;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CheckIn() {

        // =====================================================
        // FRAME
        // =====================================================

        setTitle("Hotel Management System - Check In");

        setSize(1100, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =====================================================
        // WINDOW LISTENER
        // =====================================================

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                new Dashboard().setVisible(true);
            }
        });

        // =====================================================
        // BACKGROUND PANEL
        // =====================================================

        BackgroundPanel backgroundPanel =
                new BackgroundPanel();

        backgroundPanel.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // OVERLAY
        // =====================================================

        JPanel overlay =
                new JPanel(new BorderLayout());

        overlay.setOpaque(false);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        40,
                        20,
                        40
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel("GUEST CHECK-IN");

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        32
                )
        );

        title.setForeground(gold);

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitle =
                new JLabel(
                        "Welcome to our luxury hotel"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(Color.WHITE);

        // =====================================================
        // TITLE PANEL
        // =====================================================

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // =====================================================
        // BACK TO DASHBOARD BUTTON
        // =====================================================

        JButton backButton =
                new JButton("← Dashboard");

        styleButton(backButton);

        backButton.setPreferredSize(
                new Dimension(
                        140,
                        38
                )
        );

        backButton.addActionListener(e -> {

            dispose();

            new Dashboard().setVisible(true);
        });

        JPanel backPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        backPanel.setOpaque(false);

        backPanel.add(backButton);

        header.add(
                backPanel,
                BorderLayout.EAST
        );

        overlay.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // FORM PANEL
        // =====================================================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        15,
                        10,
                        15
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // =====================================================
        // GUEST NAME
        // =====================================================

        addLabel(
                formPanel,
                "Guest Name",
                gbc,
                0,
                0
        );

        guestNameField =
                createTextField();

        addComponent(
                formPanel,
                guestNameField,
                gbc,
                1,
                0
        );

        // =====================================================
        // PHONE NUMBER
        // =====================================================

        addLabel(
                formPanel,
                "Phone Number",
                gbc,
                0,
                1
        );

        phoneField =
                createTextField();

        addComponent(
                formPanel,
                phoneField,
                gbc,
                1,
                1
        );

        // =====================================================
        // ID TYPE
        // =====================================================

        addLabel(
                formPanel,
                "ID Type",
                gbc,
                0,
                2
        );

        idTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "Passport",
                                "Driving License",
                                "Voter ID",
                                "Other ID"
                        }
                );

        styleComboBox(idTypeCombo);

        addComponent(
                formPanel,
                idTypeCombo,
                gbc,
                1,
                2
        );

        // =====================================================
        // ROOM TYPE
        // =====================================================

        addLabel(
                formPanel,
                "Room Type",
                gbc,
                0,
                3
        );

        roomTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "Single Room",
                                "Double Room",
                                "Deluxe Room",
                                "Suite Room"
                        }
                );

        styleComboBox(roomTypeCombo);

        addComponent(
                formPanel,
                roomTypeCombo,
                gbc,
                1,
                3
        );

        // =====================================================
        // ROOM NUMBER (COMBOBOX)
        // =====================================================

        addLabel(
                formPanel,
                "Room Number",
                gbc,
                0,
                4
        );

        roomNumberCombo =
                new JComboBox<>();

        styleComboBox(roomNumberCombo);

        addComponent(
                formPanel,
                roomNumberCombo,
                gbc,
                1,
                4
        );

        // Populate initial rooms based on default selection and listen for changes
        updateAvailableRooms();

        roomTypeCombo.addActionListener(e -> updateAvailableRooms());

        // =====================================================
        // CHECK-IN DATE
        // =====================================================

        addLabel(
                formPanel,
                "Check-In Date",
                gbc,
                0,
                5
        );

        dateSpinner =
                createDateSpinner();

        addComponent(
                formPanel,
                dateSpinner,
                gbc,
                1,
                5
        );

        // =====================================================
        // NUMBER OF GUESTS
        // =====================================================

        addLabel(
                formPanel,
                "Number of Guests",
                gbc,
                0,
                6
        );

        guestsField =
                createTextField();

        addComponent(
                formPanel,
                guestsField,
                gbc,
                1,
                6
        );

        // =====================================================
        // FORM CARD
        // =====================================================

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                new Color(
                        10,
                        20,
                        30
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                gold,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                35,
                                25,
                                35
                        )
                )
        );

        card.setPreferredSize(
                new Dimension(
                        600,
                        520
                )
        );

        card.add(
                formPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                20,
                                10
                        )
                );

        buttonPanel.setOpaque(false);

        JButton checkInButton =
                new JButton(
                        "CONFIRM CHECK-IN"
                );

        JButton clearButton =
                new JButton(
                        "CLEAR"
                );

        styleButton(checkInButton);

        styleButton(clearButton);

        buttonPanel.add(checkInButton);

        buttonPanel.add(clearButton);

        card.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // CENTER CARD
        // =====================================================

        JPanel centerPanel =
                new JPanel(
                        new GridBagLayout()
                );

        centerPanel.setOpaque(false);

        centerPanel.add(card);

        overlay.add(
                centerPanel,
                BorderLayout.CENTER
        );

        backgroundPanel.add(
                overlay,
                BorderLayout.CENTER
        );

        setContentPane(
                backgroundPanel
        );

        // =====================================================
        // CONFIRM CHECK-IN BUTTON
        // =====================================================

        checkInButton.addActionListener(
                e -> saveCheckIn()
        );

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        clearButton.addActionListener(
                e -> clearForm()
        );

        setVisible(true);
    }

    // =========================================================
    // FETCH AVAILABLE ROOMS BY TYPE (ROBUST & FLEXIBLE)
    // =========================================================

    private void updateAvailableRooms() {

        roomNumberCombo.removeAllItems();

        String selectedType =
                (String) roomTypeCombo.getSelectedItem();

        if (selectedType == null) {
            return;
        }

        // Map selections to handle variations stored in database
        String type1 = selectedType;                   // e.g., "Single Room"
        String type2 = selectedType.replace(" Room", ""); // e.g., "Single"

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT room_number FROM rooms WHERE (room_type = ? OR room_type = ?) AND LOWER(status) = 'available'"
             )) {

            pstmt.setString(1, type1);
            pstmt.setString(2, type2);

            try (ResultSet rs = pstmt.executeQuery()) {

                boolean found = false;

                while (rs.next()) {

                    roomNumberCombo.addItem(
                            rs.getString("room_number")
                    );

                    found = true;
                }

                if (!found) {

                    roomNumberCombo.addItem("No rooms available");
                }
            }

        } catch (SQLException ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error fetching available rooms: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SAVE CHECK-IN INTO MYSQL AND UPDATE ROOM STATUS
    // =========================================================

    private void saveCheckIn() {

        // -----------------------------------------------------
        // VALIDATE REQUIRED FIELDS
        // -----------------------------------------------------

        String selectedRoom =
                (String) roomNumberCombo.getSelectedItem();

        if (guestNameField.getText().trim().isEmpty()
                || phoneField.getText().trim().isEmpty()
                || selectedRoom == null
                || selectedRoom.equals("No rooms available")
                || guestsField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields and select a valid room.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // VALIDATE PHONE NUMBER
        // -----------------------------------------------------

        String phone =
                phoneField.getText().trim();

        if (!phone.matches("\\d{10}")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid 10-digit phone number.",
                    "Invalid Phone Number",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // GET NUMBER OF GUESTS
        // -----------------------------------------------------

        int numberOfGuests;

        try {

            numberOfGuests =
                    Integer.parseInt(
                            guestsField
                                    .getText()
                                    .trim()
                    );

            if (numberOfGuests <= 0) {

                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Number of Guests must be a valid number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -----------------------------------------------------
        // GET FORM DATA
        // -----------------------------------------------------

        String guestName =
                guestNameField
                        .getText()
                        .trim();

        String roomNumber =
                selectedRoom.trim();

        String roomType =
                roomTypeCombo
                        .getSelectedItem()
                        .toString();

        String idType =
                idTypeCombo
                        .getSelectedItem()
                        .toString();

        // -----------------------------------------------------
        // GET CHECK-IN DATE
        // -----------------------------------------------------

        Date selectedDate =
                (Date) dateSpinner.getValue();

        String formattedDate =
                new SimpleDateFormat(
                        "dd-MM-yyyy"
                ).format(selectedDate);

        java.sql.Date sqlDate =
                new java.sql.Date(
                        selectedDate.getTime()
                );

        // -----------------------------------------------------
        // GENERATE BOOKING ID
        // -----------------------------------------------------

        String bookingId =
                "BK" + System.currentTimeMillis();

        Connection connection = null;

        PreparedStatement roomStatement = null;

        PreparedStatement insertStatement = null;

        PreparedStatement updateRoomStatement = null;

        ResultSet roomResult = null;

        try {

            // -------------------------------------------------
            // DATABASE CONNECTION
            // -------------------------------------------------

            connection =
                    DBConnection.getConnection();

            // Start transaction

            connection.setAutoCommit(false);

            // -------------------------------------------------
            // CHECK ROOM AVAILABILITY
            // -------------------------------------------------

            String roomCheckSQL =
                    "SELECT status, room_type "
                            + "FROM rooms "
                            + "WHERE room_number = ?";

            roomStatement =
                    connection.prepareStatement(
                            roomCheckSQL
                    );

            roomStatement.setString(
                    1,
                    roomNumber
            );

            roomResult =
                    roomStatement.executeQuery();

            // -------------------------------------------------
            // ROOM NOT FOUND
            // -------------------------------------------------

            if (!roomResult.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Room number "
                                + roomNumber
                                + " does not exist.",
                        "Room Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                connection.rollback();

                return;
            }

            String roomStatus =
                    roomResult.getString(
                            "status"
                    );

            // -------------------------------------------------
            // CHECK ROOM STATUS
            // -------------------------------------------------

            if (!"Available".equalsIgnoreCase(roomStatus)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Room "
                                + roomNumber
                                + " is currently "
                                + roomStatus
                                + ".\nPlease select another room.",
                        "Room Not Available",
                        JOptionPane.WARNING_MESSAGE
                );

                connection.rollback();

                return;
            }

            // -------------------------------------------------
            // INSERT GUEST INTO CHECKINS TABLE
            // -------------------------------------------------

            String insertSQL =
                    "INSERT INTO checkins "
                            + "(booking_id, "
                            + "guest_name, "
                            + "room_number, "
                            + "room_type, "
                            + "check_in_date, "
                            + "phone, "
                            + "id_type, "
                            + "number_of_guests, "
                            + "status) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            insertStatement =
                    connection.prepareStatement(
                            insertSQL
                    );

            insertStatement.setString(
                    1,
                    bookingId
            );

            insertStatement.setString(
                    2,
                    guestName
            );

            insertStatement.setString(
                    3,
                    roomNumber
            );

            insertStatement.setString(
                    4,
                    roomType
            );

            insertStatement.setDate(
                    5,
                    sqlDate
            );

            insertStatement.setString(
                    6,
                    phone
            );

            insertStatement.setString(
                    7,
                    idType
            );

            insertStatement.setInt(
                    8,
                    numberOfGuests
            );

            insertStatement.setString(
                    9,
                    "Checked In"
            );

            int rowsInserted =
                    insertStatement.executeUpdate();

            // -------------------------------------------------
            // UPDATE ROOM STATUS
            // -------------------------------------------------

            String updateRoomSQL =
                    "UPDATE rooms "
                            + "SET status = 'Checked In' "
                            + "WHERE room_number = ? "
                            + "AND LOWER(status) = 'available'";

            updateRoomStatement =
                    connection.prepareStatement(
                            updateRoomSQL
                    );

            updateRoomStatement.setString(
                    1,
                    roomNumber
            );

            int roomUpdated =
                    updateRoomStatement.executeUpdate();

            // -------------------------------------------------
            // CHECK BOTH OPERATIONS
            // -------------------------------------------------

            if (rowsInserted > 0 && roomUpdated > 0) {

                // Save both operations permanently

                connection.commit();

                JOptionPane.showMessageDialog(
                        this,
                        "Guest Checked-In Successfully!\n\n"
                                + "Booking ID: "
                                + bookingId
                                + "\n"
                                + "Guest Name: "
                                + guestName
                                + "\n"
                                + "Phone: "
                                + phone
                                + "\n"
                                + "Room Number: "
                                + roomNumber
                                + "\n"
                                + "Room Type: "
                                + roomType
                                + "\n"
                                + "Check-In Date: "
                                + formattedDate
                                + "\n"
                                + "Number of Guests: "
                                + numberOfGuests,
                        "Check-In Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Clear form after successful save

                clearForm();

            } else {

                connection.rollback();

                JOptionPane.showMessageDialog(
                        this,
                        "Check-in failed. Please try again.",
                        "Check-In Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException ex) {

            // -------------------------------------------------
            // ROLLBACK IF ERROR OCCURS
            // -------------------------------------------------

            try {

                if (connection != null) {

                    connection.rollback();
                }

            } catch (SQLException rollbackException) {

                rollbackException.printStackTrace();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();

        } finally {

            // -------------------------------------------------
            // CLOSE RESULT SET
            // -------------------------------------------------

            try {

                if (roomResult != null) {

                    roomResult.close();
                }

            } catch (SQLException ex) {

                ex.printStackTrace();
            }

            // -------------------------------------------------
            // CLOSE STATEMENTS
            // -------------------------------------------------

            try {

                if (roomStatement != null) {

                    roomStatement.close();
                }

                if (insertStatement != null) {

                    insertStatement.close();
                }

                if (updateRoomStatement != null) {

                    updateRoomStatement.close();
                }

            } catch (SQLException ex) {

                ex.printStackTrace();
            }

            // -------------------------------------------------
            // CLOSE CONNECTION
            // -------------------------------------------------

            try {

                if (connection != null) {

                    connection.setAutoCommit(true);

                    connection.close();
                }

            } catch (SQLException ex) {

                ex.printStackTrace();
            }
        }
    }

    // =========================================================
    // CREATE TEXT FIELD
    // =========================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        field.setForeground(
                Color.WHITE
        );

        field.setBackground(
                new Color(
                        0,
                        0,
                        0
                )
        );

        field.setCaretColor(
                Color.WHITE
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                gold,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        return field;
    }

    // =========================================================
    // CREATE DATE SPINNER
    // =========================================================

    private JSpinner createDateSpinner() {

        JSpinner spinner =
                new JSpinner(
                        new SpinnerDateModel()
                );

        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(
                        spinner,
                        "dd-MM-yyyy"
                );

        spinner.setEditor(editor);

        spinner.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        JFormattedTextField textField =
                editor.getTextField();

        textField.setForeground(
                Color.WHITE
        );

        textField.setBackground(
                new Color(
                        0,
                        0,
                        0
                )
        );

        textField.setCaretColor(
                Color.WHITE
        );

        textField.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        10,
                        8,
                        10
                )
        );

        spinner.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                gold,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                0,
                                0,
                                0
                        )
                )
        );

        return spinner;
    }

    // =========================================================
    // ADD LABEL
    // =========================================================

    private void addLabel(
            JPanel panel,
            String text,
            GridBagConstraints gbc,
            int x,
            int y
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                Color.WHITE
        );

        gbc.gridx = x;

        gbc.gridy = y;

        panel.add(
                label,
                gbc
        );
    }

    // =========================================================
    // ADD COMPONENT
    // =========================================================

    private void addComponent(
            JPanel panel,
            JComponent component,
            GridBagConstraints gbc,
            int x,
            int y
    ) {

        gbc.gridx = x;

        gbc.gridy = y;

        panel.add(
                component,
                gbc
        );
    }

    // =========================================================
    // STYLE COMBO BOX
    // =========================================================

    private void styleComboBox(
            JComboBox<String> comboBox
    ) {

        comboBox.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        comboBox.setForeground(
                Color.WHITE
        );

        comboBox.setBackground(
                dark
        );

        comboBox.setBorder(
                BorderFactory.createLineBorder(
                        gold
                )
        );
    }

    // =========================================================
    // STYLE BUTTON
    // =========================================================

    private void styleButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                dark
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        gold,
                        1
                )
        );

        button.setPreferredSize(
                new Dimension(
                        180,
                        45
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(gold);

                        button.setForeground(Color.BLACK);
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(dark);

                        button.setForeground(Color.WHITE);
                    }
                }
        );
    }

    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {

        guestNameField.setText("");

        phoneField.setText("");

        dateSpinner.setValue(
                new Date()
        );

        guestsField.setText("");

        idTypeCombo.setSelectedIndex(0);

        roomTypeCombo.setSelectedIndex(0);

        updateAvailableRooms();
    }

    // =========================================================
    // BACKGROUND PANEL
    // =========================================================

    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {

            try {

                ImageIcon icon =
                        new ImageIcon(
                                getClass().getResource(
                                        "/resource/checkIn.png"
                                )
                        );

                backgroundImage =
                        icon.getImage();

            } catch (Exception e) {

                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            if (backgroundImage == null) {

                return;
            }

            int width =
                    getWidth();

            int height =
                    getHeight();

            double imageRatio =
                    (double)
                            backgroundImage.getWidth(null)
                            /
                            backgroundImage.getHeight(null);

            double panelRatio =
                    (double) width / height;

            int drawWidth;

            int drawHeight;

            if (panelRatio > imageRatio) {

                drawWidth =
                        width;

                drawHeight =
                        (int)
                                (width / imageRatio);

            } else {

                drawHeight =
                        height;

                drawWidth =
                        (int)
                                (height * imageRatio);
            }

            int x =
                    (width - drawWidth) / 2;

            int y =
                    (height - drawHeight) / 2;

            g.drawImage(
                    backgroundImage,
                    x,
                    y,
                    drawWidth,
                    drawHeight,
                    this
            );
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

                    new CheckIn();
                }
        );
    }
}