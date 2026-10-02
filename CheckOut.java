package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckOut extends JFrame {

    // ================= COLORS =================

    private final Color gold = new Color(212, 175, 55);
    private final Color dark = new Color(15, 23, 42);

    // ================= TEXT FIELDS =================

    private JTextField guestNameField;
    private JTextField roomNumberField;
    private JTextField totalAmountField;
    private JTextField searchRoomField; // Top search bar field

    // ================= DATE SPINNERS =================

    private JSpinner checkInDateSpinner;
    private JSpinner checkOutDateSpinner;

    // ================= BILL AMOUNT =================

    private double calculatedTotalAmount = 0;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CheckOut() {

        setTitle("Hotel Management System - Check Out");

        setSize(1100, 720);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                new Dashboard().setVisible(true);
            }
        });

        // =================================================
        // BACKGROUND IMAGE
        // =================================================

        BackgroundPanel backgroundPanel =
                new BackgroundPanel();

        backgroundPanel.setLayout(
                new BorderLayout()
        );

        // =================================================
        // HEADER
        // =================================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        40,
                        15,
                        40
                )
        );

        JLabel title =
                new JLabel("GUEST CHECK-OUT");

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        32
                )
        );

        title.setForeground(gold);

        JLabel subtitle =
                new JLabel(
                        "Thank you for staying with us"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(Color.WHITE);

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
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // Right side: Search Panel & Back Button
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeaderPanel.setOpaque(false);

        // Top Search Section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Room No:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchLabel.setForeground(Color.WHITE);

        searchRoomField = new JTextField(6);
        styleTextField(searchRoomField);
        searchRoomField.setPreferredSize(new Dimension(80, 35));

        JButton searchButton = new JButton("SEARCH");
        styleButton(searchButton);
        searchButton.setPreferredSize(new Dimension(95, 35));

        searchPanel.add(searchLabel);
        searchPanel.add(searchRoomField);
        searchPanel.add(searchButton);

        JButton backButton =
                new JButton("← Dashboard");

        styleButton(backButton);

        backButton.setPreferredSize(
                new Dimension(
                        130,
                        35
                )
        );

        backButton.addActionListener(e -> {
            dispose();
            new Dashboard().setVisible(true);
        });

        rightHeaderPanel.add(searchPanel);
        rightHeaderPanel.add(backButton);

        header.add(
                rightHeaderPanel,
                BorderLayout.EAST
        );

        backgroundPanel.add(
                header,
                BorderLayout.NORTH
        );

        // =================================================
        // FORM PANEL
        // =================================================

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

        // Guest Name

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

        // Room Number

        addLabel(
                formPanel,
                "Room Number",
                gbc,
                0,
                1
        );

        roomNumberField =
                createTextField();

        addComponent(
                formPanel,
                roomNumberField,
                gbc,
                1,
                1
        );

        // Check-In Date

        addLabel(
                formPanel,
                "Check-In Date",
                gbc,
                0,
                2
        );

        checkInDateSpinner =
                createDateSpinner();

        addComponent(
                formPanel,
                checkInDateSpinner,
                gbc,
                1,
                2
        );

        // Check-Out Date

        addLabel(
                formPanel,
                "Check-Out Date",
                gbc,
                0,
                3
        );

        checkOutDateSpinner =
                createDateSpinner();

        addComponent(
                formPanel,
                checkOutDateSpinner,
                gbc,
                1,
                3
        );

        // Total Amount

        addLabel(
                formPanel,
                "Total Amount",
                gbc,
                0,
                4
        );

        totalAmountField =
                createTextField();

        totalAmountField.setEditable(false);

        addComponent(
                formPanel,
                totalAmountField,
                gbc,
                1,
                4
        );

        // =================================================
        // CARD
        // =================================================

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                new Color(
                        15,
                        23,
                        42,
                        220
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
                                40,
                                25,
                                40
                        )
                )
        );

        card.setPreferredSize(
                new Dimension(
                        650,
                        430
                )
        );

        card.add(
                formPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        buttonPanel.setOpaque(false);

        JButton calculateButton =
                new JButton(
                        "CALCULATE BILL"
                );

        JButton checkoutButton =
                new JButton(
                        "CONFIRM CHECK-OUT"
                );

        JButton printButton =
                new JButton(
                        "PRINT BILL"
                );

        JButton clearButton =
                new JButton(
                        "CLEAR"
                );

        styleButton(calculateButton);
        styleButton(checkoutButton);
        styleButton(printButton);
        styleButton(clearButton);

        buttonPanel.add(calculateButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(printButton);
        buttonPanel.add(clearButton);

        card.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =================================================
        // CENTER PANEL
        // =================================================

        JPanel centerPanel =
                new JPanel(
                        new GridBagLayout()
                );

        centerPanel.setOpaque(false);

        centerPanel.add(card);

        backgroundPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        setContentPane(
                backgroundPanel
        );

        // =================================================
        // SEARCH ACTION (Top Bar)
        // =================================================

        searchButton.addActionListener(e -> performSearch(searchRoomField.getText().trim()));
        searchRoomField.addActionListener(e -> performSearch(searchRoomField.getText().trim()));

        // =================================================
        // CALCULATE BILL
        // =================================================

        calculateButton.addActionListener(e -> calculateBillAction());

        // =================================================
        // CONFIRM CHECK-OUT
        // =================================================

        checkoutButton.addActionListener(e -> {

            String guestName =
                    guestNameField
                            .getText()
                            .trim();

            String roomNumber =
                    roomNumberField
                            .getText()
                            .trim();

            if (
                    guestName.isEmpty()
                            || roomNumber.isEmpty()
                            || calculatedTotalAmount <= 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please search, enter details and calculate the bill first.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String checkInDateStr =
                    getDateFromSpinner(
                            checkInDateSpinner
                    );

            String checkOutDateStr =
                    getDateFromSpinner(
                            checkOutDateSpinner
                    );

            try (
                    Connection conn =
                            DBConnection.getConnection()
            ) {

                conn.setAutoCommit(false);

                try {

                    // 1. Update room status

                    String updateRoomSQL =
                            "UPDATE rooms "
                                    + "SET status = 'Available' "
                                    + "WHERE room_number = ?";

                    try (
                            PreparedStatement pstRoom =
                                    conn.prepareStatement(
                                            updateRoomSQL
                                    )
                    ) {

                        pstRoom.setString(
                                1,
                                roomNumber
                        );

                        pstRoom.executeUpdate();
                    }

                    // 2. Update check-in status and save amount

                    String updateCheckinSQL =
                            "UPDATE checkins "
                                    + "SET status = 'Checked Out', "
                                    + "total_amount = ? "
                                    + "WHERE room_number = ? "
                                    + "AND status = 'Checked In'";

                    int updatedRows;

                    try (
                            PreparedStatement pstCheckin =
                                    conn.prepareStatement(
                                            updateCheckinSQL
                                    )
                    ) {

                        pstCheckin.setDouble(
                                1,
                                calculatedTotalAmount
                        );

                        pstCheckin.setString(
                                2,
                                roomNumber
                        );

                        updatedRows =
                                pstCheckin.executeUpdate();
                    }

                    if (updatedRows == 0) {

                        throw new SQLException(
                                "No active check-in found for this room."
                        );
                    }

                    conn.commit();

                    JOptionPane.showMessageDialog(
                            this,
                            "Guest Checked-Out Successfully!\n\n"
                                    + "Guest: "
                                    + guestName
                                    + "\nRoom No: "
                                    + roomNumber
                                    + "\nCheck-In: "
                                    + checkInDateStr
                                    + "\nCheck-Out: "
                                    + checkOutDateStr
                                    + "\nTotal Amount: ₹"
                                    + String.format(
                                    "%.2f",
                                    calculatedTotalAmount
                            ),
                            "Check-Out Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    clearForm();

                } catch (SQLException ex) {

                    conn.rollback();

                    throw ex;
                }

            } catch (SQLException ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error during Check-Out:\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // =================================================
        // PRINT BILL BUTTON (SHOWS RECEIPT PREVIEW DIALOG)
        // =================================================

        printButton.addActionListener(e -> {
            if (totalAmountField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please calculate the bill before printing.",
                        "No Bill Available",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            showBillPreviewDialog();
        });

        // =================================================
        // CLEAR BUTTON
        // =================================================

        clearButton.addActionListener(
                e -> clearForm()
        );

        setVisible(true);
    }

    // =====================================================
    // BILL PREVIEW DIALOG (RECEIPT STYLE)
    // =====================================================

    private void showBillPreviewDialog() {
        JDialog previewDialog = new JDialog(this, "Bill Receipt Preview", true);
        previewDialog.setSize(420, 600);
        previewDialog.setLocationRelativeTo(this);
        previewDialog.setLayout(new BorderLayout());

        String guestName = guestNameField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        String checkInDate = getDateFromSpinner(checkInDateSpinner);
        String checkOutDate = getDateFromSpinner(checkOutDateSpinner);

        String currentDateStr = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());

        // Receipt Content Layout using HTML styling
        JTextPane receiptPane = new JTextPane();
        receiptPane.setContentType("text/html");
        receiptPane.setEditable(false);

        String receiptHtml = "<html><body style='font-family: monospace; font-size: 12pt; text-align: center; color: #000;'>"
                + "<h2 style='margin-bottom: 0;'>ROYAL STAY HOTEL</h2>"
                + "<div style='font-size: 9pt; color: #555;'>500 Luxury Avenue<br>Downtown, USA 10001<br>(555) 800-1234</div>"
                + "<hr style='border: dashed 1px #bbb;'>"
                + "<div style='text-align: left; font-size: 10pt;'>"
                + "<b>DATE:</b> " + currentDateStr + "<br>"
                + "<b>GUEST:</b> " + guestName + "<br>"
                + "<b>ROOM:</b> " + roomNumber + "<br>"
                + "<b>CHECK-IN:</b> " + checkInDate + "<br>"
                + "<b>CHECK-OUT:</b> " + checkOutDate + "<br>"
                + "</div>"
                + "<hr style='border: dashed 1px #bbb;'>"
                + "<table width='100%' style='font-family: monospace; font-size: 10pt; text-align: left;'>"
                + "<tr><td>ROOM CHARGES</td><td align='right'>" + totalAmountField.getText().trim() + "</td></tr>"
                + "<tr><td>TAX (12.5%)</td><td align='right'>Included</td></tr>"
                + "</table>"
                + "<hr style='border: solid 1px #000;'>"
                + "<table width='100%' style='font-family: monospace; font-size: 11pt; font-weight: bold;'>"
                + "<tr><td>TOTAL</td><td align='right'>" + totalAmountField.getText().trim() + "</td></tr>"
                + "</table>"
                + "<hr style='border: dashed 1px #bbb;'>"
                + "<div style='font-size: 9pt; color: #777; margin-top: 15px;'>Thank you for staying with us!</div>"
                + "</body></html>";

        receiptPane.setText(receiptHtml);
        JScrollPane scrollPane = new JScrollPane(receiptPane);

        // Bottom Action Panel inside Preview Dialog
        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton printNowButton = new JButton("PRINT");
        JButton closePreviewButton = new JButton("CLOSE");

        styleButton(printNowButton);
        styleButton(closePreviewButton);

        printNowButton.addActionListener(e -> {
            try {
                boolean complete = receiptPane.print();
                if (complete) {
                    JOptionPane.showMessageDialog(previewDialog, "Bill printed successfully!");
                    previewDialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(previewDialog, "Printing Failed: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        closePreviewButton.addActionListener(e -> previewDialog.dispose());

        dialogButtonPanel.add(printNowButton);
        dialogButtonPanel.add(closePreviewButton);

        previewDialog.add(scrollPane, BorderLayout.CENTER);
        previewDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
        previewDialog.setVisible(true);
    }

    // =====================================================
    // SEARCH METHOD
    // =====================================================

    private void performSearch(String roomNumber) {
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a room number to search.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT r.price, c.check_in_date, c.guest_name "
                    + "FROM rooms r JOIN checkins c ON r.room_number = c.room_number "
                    + "WHERE r.room_number = ? AND c.status = 'Checked In' "
                    + "ORDER BY c.id DESC LIMIT 1";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, roomNumber);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String guestName = rs.getString("guest_name");
                        java.sql.Date dbCheckInDate = rs.getDate("check_in_date");

                        guestNameField.setText(guestName);
                        roomNumberField.setText(roomNumber);

                        if (dbCheckInDate != null) {
                            checkInDateSpinner.setValue(dbCheckInDate);
                        }

                        // Automatically set check-out date to today's date
                        checkOutDateSpinner.setValue(new Date());

                        // Automatically run calculation
                        calculateBillAction();

                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "No active check-in found for Room Number: " + roomNumber,
                                "Not Found",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error during search:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // CALCULATE BILL LOGIC
    // =====================================================

    private void calculateBillAction() {
        String roomNumber = roomNumberField.getText().trim();
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please search or enter room number first.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT r.price, c.check_in_date, c.guest_name "
                    + "FROM rooms r JOIN checkins c ON r.room_number = c.room_number "
                    + "WHERE r.room_number = ? AND c.status = 'Checked In' "
                    + "ORDER BY c.id DESC LIMIT 1";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, roomNumber);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        double pricePerNight = rs.getDouble("price");
                        java.sql.Date dbCheckInDate = rs.getDate("check_in_date");

                        if (dbCheckInDate != null) {
                            checkInDateSpinner.setValue(dbCheckInDate);
                        }

                        LocalDate checkIn = dbCheckInDate.toLocalDate();
                        Date checkOutValue = (Date) checkOutDateSpinner.getValue();
                        LocalDate checkOut = checkOutValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        long days = ChronoUnit.DAYS.between(checkIn, checkOut);

                        if (days < 0) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Check-out date cannot be earlier than check-in date.",
                                    "Invalid Dates",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }

                        if (days == 0) {
                            days = 1;
                        }

                        calculatedTotalAmount = pricePerNight * days;
                        totalAmountField.setText(String.format("₹ %.2f", calculatedTotalAmount));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error while calculating bill:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // CREATE DATE SPINNER
    // =====================================================

    private JSpinner createDateSpinner() {

        SpinnerDateModel dateModel =
                new SpinnerDateModel(
                        new Date(),
                        null,
                        null,
                        java.util.Calendar.DAY_OF_MONTH
                );

        JSpinner spinner =
                new JSpinner(dateModel);

        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(
                        spinner,
                        "dd-MM-yyyy"
                );

        spinner.setEditor(editor);

        spinner.setPreferredSize(
                new Dimension(
                        300,
                        38
                )
        );

        spinner.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        JComponent editorComponent =
                spinner.getEditor();

        if (
                editorComponent
                        instanceof JSpinner.DefaultEditor
        ) {

            JSpinner.DefaultEditor defaultEditor =
                    (JSpinner.DefaultEditor)
                            editorComponent;

            JTextField textField =
                    defaultEditor.getTextField();

            textField.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            15
                    )
            );

            textField.setForeground(
                    Color.WHITE
            );

            textField.setCaretColor(
                    Color.WHITE
            );

            textField.setOpaque(true);

            textField.setBackground(dark);

            textField.setBorder(
                    BorderFactory.createCompoundBorder(

                            BorderFactory.createLineBorder(
                                    gold,
                                    1
                            ),

                            BorderFactory.createEmptyBorder(
                                    6,
                                    10,
                                    6,
                                    10
                            )
                    )
            );
        }

        return spinner;
    }

    // =====================================================
    // GET DATE FROM SPINNER
    // =====================================================

    private String getDateFromSpinner(
            JSpinner spinner
    ) {

        Date date =
                (Date) spinner.getValue();

        java.text.SimpleDateFormat format =
                new java.text.SimpleDateFormat(
                        "dd-MM-yyyy"
                );

        return format.format(date);
    }

    // =====================================================
    // CREATE TEXT FIELD
    // =====================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        styleTextField(field);
        field.setPreferredSize(new Dimension(300, 38));

        return field;
    }

    private void styleTextField(JTextField field) {
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

        field.setCaretColor(
                Color.WHITE
        );

        field.setOpaque(false);

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                gold,
                                1
                        ),

                        BorderFactory.createEmptyBorder(
                                6,
                                10,
                                6,
                                10
                        )
                )
        );
    }

    // =====================================================
    // ADD LABEL
    // =====================================================

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

    // =====================================================
    // ADD COMPONENT
    // =====================================================

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

    // =====================================================
    // BUTTON STYLE
    // =====================================================

    private void styleButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
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
                        gold
                )
        );

        button.setPreferredSize(
                new Dimension(
                        135,
                        40
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

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

        guestNameField.setText("");

        roomNumberField.setText("");

        totalAmountField.setText("");

        searchRoomField.setText("");

        calculatedTotalAmount = 0;

        checkInDateSpinner.setValue(
                new Date()
        );

        checkOutDateSpinner.setValue(
                new Date()
        );
    }

    // =====================================================
    // BACKGROUND PANEL
    // =====================================================

    class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {

            java.net.URL imageURL =
                    CheckOut.class.getResource(
                            "/resource/checkIn.png"
                    );

            if (imageURL != null) {

                backgroundImage =
                        new ImageIcon(
                                imageURL
                        ).getImage();

            } else {

                System.out.println(
                        "Background image not found!"
                );
            }

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            if (backgroundImage != null) {

                g.drawImage(
                        backgroundImage,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new CheckOut()
        );
    }
}