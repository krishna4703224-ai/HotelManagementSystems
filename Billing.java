package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Billing extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private final Color GOLD = new Color(220, 170, 70);
    private final Color DARK_NAVY = new Color(5, 18, 32);
    private final Color FIELD_COLOR = new Color(25, 40, 57);
    private final Color WHITE = Color.WHITE;

    // =========================================================
    // FORM COMPONENTS
    // =========================================================

    private JTextField guestNameField;

    private JLabel bookingIdValue;
    private JLabel guestNameValue;
    private JLabel roomNumberValue;
    private JLabel roomTypeValue;
    private JLabel checkInValue;
    private JLabel checkOutValue;
    private JLabel phoneValue;
    private JLabel idTypeValue;
    private JLabel guestsValue;
    private JLabel statusValue;

    private JLabel grandTotalLabel;

    // Payment labels
    private JLabel roomChargeLabel;
    private JLabel otherChargeLabel;
    private JLabel paymentGrandTotalLabel;

    // =========================================================
    // TABLE
    // =========================================================

    private JTable billTable;
    private DefaultTableModel tableModel;

    // =========================================================
    // PAYMENT
    // =========================================================

    private JRadioButton onlineRadio;
    private JRadioButton hotelRadio;

    // =========================================================
    // CURRENT BILL DATA
    // =========================================================

    private String currentBookingId = "";
    private String currentGuestName = "";
    private String currentRoomNumber = "";
    private String currentRoomType = "";
    private String currentCheckInDate = "";
    private String currentPhone = "";
    private String currentIdType = "";
    private String currentNumberOfGuests = "";
    private String currentStatus = "";

    /*
     * IMPORTANT:
     * This amount comes directly from CheckOut.java
     * and checkins.total_amount column.
     */
    private double savedCheckoutAmount = 0.0;

    private double roomCharge = 0;
    private double otherCharge = 0;
    private double grandTotal = 0;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Billing() {

        this(null);
    }

    // =========================================================
    // CONSTRUCTOR WITH GUEST NAME
    // =========================================================

    public Billing(String guestName) {

        setTitle("Hotel Management System - Generate Bill");

        setSize(1250, 800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =====================================================
        // BACKGROUND
        // =====================================================

        BackgroundPanel backgroundPanel =
                new BackgroundPanel();

        backgroundPanel.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // MAIN OVERLAY
        // =====================================================

        JPanel overlay =
                new JPanel(
                        new BorderLayout()
                );

        overlay.setOpaque(false);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        35,
                        15,
                        35
                )
        );

        JLabel title =
                new JLabel(
                        "₹ GENERATE BILL"
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        32
                )
        );

        title.setForeground(GOLD);

        JLabel subtitle =
                new JLabel(
                        "Search guest name to view saved check-in details"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(WHITE);

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

        JButton backButton =
                new JButton(
                        "← Dashboard"
                );

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
        // CONTENT PANEL
        // =====================================================

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        contentPanel.setOpaque(false);

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        35,
                        20,
                        35
                )
        );

        // =====================================================
        // SEARCH PANEL
        // =====================================================

        JPanel searchPanel =
                createSearchPanel();

        contentPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER PANEL
        // =====================================================

        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        centerPanel.setOpaque(false);

        centerPanel.add(
                createGuestInfoPanel()
        );

        centerPanel.add(
                createBillTablePanel()
        );

        contentPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM PANEL
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        bottomPanel.setOpaque(false);

        bottomPanel.add(
                createPaymentPanel(),
                BorderLayout.CENTER
        );

        bottomPanel.add(
                createButtonPanel(),
                BorderLayout.SOUTH
        );

        contentPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        overlay.add(
                contentPanel,
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
        // IF GUEST NAME IS PROVIDED
        // =====================================================

        if (guestName != null
                && !guestName.trim().isEmpty()) {

            SwingUtilities.invokeLater(() -> {

                guestNameField.setText(
                        guestName.trim()
                );

                searchGuest();
            });
        }

        setVisible(true);
    }

    // =========================================================
    // SEARCH PANEL
    // =========================================================

    private JPanel createSearchPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        panel.setBackground(
                new Color(
                        10,
                        25,
                        40,
                        235
                )
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        GOLD,
                        1
                )
        );

        JLabel searchLabel =
                new JLabel(
                        "Guest Name:"
                );

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        searchLabel.setForeground(
                WHITE
        );

        guestNameField =
                new JTextField(
                        22
                );

        guestNameField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        guestNameField.setForeground(
                WHITE
        );

        guestNameField.setBackground(
                FIELD_COLOR
        );

        guestNameField.setCaretColor(
                WHITE
        );

        guestNameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                GOLD
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                10,
                                7,
                                10
                        )
                )
        );

        JButton searchButton =
                new JButton(
                        "SEARCH GUEST"
                );

        styleButton(searchButton);

        searchButton.setPreferredSize(
                new Dimension(
                        160,
                        40
                )
        );

        searchButton.addActionListener(
                e -> searchGuest()
        );

        guestNameField.addActionListener(
                e -> searchGuest()
        );

        panel.add(searchLabel);

        panel.add(guestNameField);

        panel.add(searchButton);

        return panel;
    }

    // =========================================================
    // SEARCH GUEST FROM MYSQL
    // =========================================================

    private void searchGuest() {

        String guestName =
                guestNameField
                        .getText()
                        .trim();

        if (guestName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter guest name.",
                    "Search Guest",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        /*
         * total_amount is now selected from database.
         *
         * Only Checked Out guest is searched because
         * amount is saved during CheckOut.
         */
        String sql =
                "SELECT booking_id, "
                        + "guest_name, "
                        + "room_number, "
                        + "room_type, "
                        + "check_in_date, "
                        + "phone, "
                        + "id_type, "
                        + "number_of_guests, "
                        + "status, "
                        + "total_amount "
                        + "FROM checkins "
                        + "WHERE guest_name LIKE ? "
                        + "AND status = 'Checked Out' "
                        + "ORDER BY id DESC "
                        + "LIMIT 1";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "%" + guestName + "%"
            );

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                loadGuestData(resultSet);

                calculateBill();

                JOptionPane.showMessageDialog(
                        this,
                        "Guest details and saved checkout amount loaded successfully.",
                        "Guest Found",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                clearBillDetails();

                JOptionPane.showMessageDialog(
                        this,
                        "No checked-out guest found with name:\n"
                                + guestName,
                        "Guest Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + ex.getMessage()
                            + "\n\nPlease check whether total_amount column exists.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================================
    // LOAD DATA FROM RESULT SET
    // =========================================================

    private void loadGuestData(
            ResultSet rs
    ) throws SQLException {

        currentBookingId =
                rs.getString(
                        "booking_id"
                );

        currentGuestName =
                rs.getString(
                        "guest_name"
                );

        currentRoomNumber =
                rs.getString(
                        "room_number"
                );

        currentRoomType =
                rs.getString(
                        "room_type"
                );

        Date checkInDate =
                rs.getDate(
                        "check_in_date"
                );

        if (checkInDate != null) {

            currentCheckInDate =
                    new SimpleDateFormat(
                            "dd-MM-yyyy"
                    ).format(
                            checkInDate
                    );

        } else {

            currentCheckInDate = "-";
        }

        currentPhone =
                rs.getString(
                        "phone"
                );

        currentIdType =
                rs.getString(
                        "id_type"
                );

        currentNumberOfGuests =
                String.valueOf(
                        rs.getInt(
                                "number_of_guests"
                        )
                );

        currentStatus =
                rs.getString(
                        "status"
                );

        // =====================================================
        // IMPORTANT: READ SAVED CHECKOUT AMOUNT
        // =====================================================

        savedCheckoutAmount =
                rs.getDouble(
                        "total_amount"
                );

        if (rs.wasNull()) {

            savedCheckoutAmount = 0.0;
        }

        // =====================================================
        // DISPLAY DATA
        // =====================================================

        bookingIdValue.setText(
                safeText(currentBookingId)
        );

        guestNameValue.setText(
                safeText(currentGuestName)
        );

        roomNumberValue.setText(
                safeText(currentRoomNumber)
        );

        roomTypeValue.setText(
                safeText(currentRoomType)
        );

        checkInValue.setText(
                safeText(currentCheckInDate)
        );

        checkOutValue.setText(
                getCurrentDate()
        );

        phoneValue.setText(
                safeText(currentPhone)
        );

        idTypeValue.setText(
                safeText(currentIdType)
        );

        guestsValue.setText(
                safeText(currentNumberOfGuests)
        );

        statusValue.setText(
                safeText(currentStatus)
        );
    }

    // =========================================================
    // CALCULATE BILL USING CHECKOUT SAVED AMOUNT
    // =========================================================

    private void calculateBill() {

        /*
         * IMPORTANT:
         *
         * Billing no longer calculates amount from:
         *
         * room type
         * room rate
         * current date
         * number of nights
         *
         * It directly uses the amount saved by CheckOut.java.
         */

        roomCharge =
                savedCheckoutAmount;

        otherCharge =
                0.0;

        grandTotal =
                roomCharge
                        + otherCharge;

        // =====================================================
        // UPDATE TABLE
        // =====================================================

        tableModel.setRowCount(0);

        tableModel.addRow(
                new Object[]{
                        "Room Rent / Checkout Amount",
                        "As per Check-Out",
                        "-",
                        formatCurrency(roomCharge)
                }
        );

        tableModel.addRow(
                new Object[]{
                        "Additional Charges",
                        "-",
                        "-",
                        formatCurrency(otherCharge)
                }
        );

        tableModel.addRow(
                new Object[]{
                        "TOTAL",
                        "",
                        "",
                        formatCurrency(grandTotal)
                }
        );

        // =====================================================
        // UPDATE PAYMENT SUMMARY
        // =====================================================

        roomChargeLabel.setText(
                formatCurrency(roomCharge)
        );

        otherChargeLabel.setText(
                formatCurrency(otherCharge)
        );

        paymentGrandTotalLabel.setText(
                formatCurrency(grandTotal)
        );

        grandTotalLabel.setText(
                "Grand Total: "
                        + formatCurrency(grandTotal)
        );
    }

    // =========================================================
    // CURRENT DATE
    // =========================================================

    private String getCurrentDate() {

        return new SimpleDateFormat(
                "dd-MM-yyyy"
        ).format(
                new Date()
        );
    }

    // =========================================================
    // SAFE TEXT
    // =========================================================

    private String safeText(
            String text
    ) {

        if (text == null
                || text.trim().isEmpty()) {

            return "-";
        }

        return text;
    }

    // =========================================================
    // GUEST INFORMATION PANEL
    // =========================================================

    private JPanel createGuestInfoPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                8
                        )
                );

        panel.setBackground(
                new Color(
                        10,
                        25,
                        40,
                        235
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                GOLD
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(
                                        GOLD
                                ),
                                " GUEST INFORMATION ",
                                0,
                                0,
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        15
                                ),
                                GOLD
                        )
                )
        );

        bookingIdValue =
                createValueLabel("-");

        guestNameValue =
                createValueLabel("-");

        roomNumberValue =
                createValueLabel("-");

        roomTypeValue =
                createValueLabel("-");

        checkInValue =
                createValueLabel("-");

        checkOutValue =
                createValueLabel("-");

        phoneValue =
                createValueLabel("-");

        idTypeValue =
                createValueLabel("-");

        guestsValue =
                createValueLabel("-");

        statusValue =
                createValueLabel("-");

        addInfoRow(
                panel,
                "Booking ID",
                bookingIdValue
        );

        addInfoRow(
                panel,
                "Guest Name",
                guestNameValue
        );

        addInfoRow(
                panel,
                "Room Number",
                roomNumberValue
        );

        addInfoRow(
                panel,
                "Room Type",
                roomTypeValue
        );

        addInfoRow(
                panel,
                "Check-In Date",
                checkInValue
        );

        addInfoRow(
                panel,
                "Billing Date",
                checkOutValue
        );

        addInfoRow(
                panel,
                "Phone",
                phoneValue
        );

        addInfoRow(
                panel,
                "ID Type",
                idTypeValue
        );

        addInfoRow(
                panel,
                "Number of Guests",
                guestsValue
        );

        addInfoRow(
                panel,
                "Status",
                statusValue
        );

        return panel;
    }

    // =========================================================
    // ADD INFORMATION ROW
    // =========================================================

    private void addInfoRow(
            JPanel panel,
            String name,
            JLabel value
    ) {

        JLabel label =
                new JLabel(
                        name
                                + ":"
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                WHITE
        );

        panel.add(label);

        panel.add(value);
    }

    // =========================================================
    // BILL TABLE PANEL
    // =========================================================

    private JPanel createBillTablePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                new Color(
                        10,
                        25,
                        40,
                        235
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                GOLD
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(
                                        GOLD
                                ),
                                " BILL DETAILS ",
                                0,
                                0,
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        15
                                ),
                                GOLD
                        )
                )
        );

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Description",
                                "Quantity",
                                "Rate",
                                "Amount"
                        },
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

        billTable =
                new JTable(
                        tableModel
                );

        billTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        billTable.setRowHeight(
                30
        );

        billTable.setBackground(
                FIELD_COLOR
        );

        billTable.setForeground(
                WHITE
        );

        billTable.setGridColor(
                new Color(
                        70,
                        85,
                        100
                )
        );

        billTable.setSelectionBackground(
                GOLD
        );

        billTable.setSelectionForeground(
                Color.BLACK
        );

        billTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        billTable.getTableHeader()
                .setBackground(
                        DARK_NAVY
                );

        billTable.getTableHeader()
                .setForeground(
                        GOLD
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        billTable
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        grandTotalLabel =
                new JLabel(
                        "Grand Total: ₹0.00"
                );

        grandTotalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        grandTotalLabel.setForeground(
                GOLD
        );

        grandTotalLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        grandTotalLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panel.add(
                grandTotalLabel,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // =========================================================
    // PAYMENT PANEL
    // =========================================================

    private JPanel createPaymentPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );

        panel.setBackground(
                new Color(
                        10,
                        25,
                        40,
                        235
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                GOLD
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                15,
                                10,
                                15
                        )
                )
        );

        roomChargeLabel =
                createPaymentValue(
                        "₹0.00"
                );

        otherChargeLabel =
                createPaymentValue(
                        "₹0.00"
                );

        paymentGrandTotalLabel =
                createPaymentValue(
                        "₹0.00"
                );

        panel.add(
                createPaymentBox(
                        "Room Charges",
                        roomChargeLabel
                )
        );

        panel.add(
                createPaymentBox(
                        "Other Charges",
                        otherChargeLabel
                )
        );

        panel.add(
                createPaymentBox(
                        "Grand Total",
                        paymentGrandTotalLabel
                )
        );

        return panel;
    }

    // =========================================================
    // PAYMENT BOX
    // =========================================================

    private JPanel createPaymentBox(
            String title,
            JLabel value
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setForeground(
                WHITE
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        value.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        panel.add(
                value,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // BUTTON PANEL
    // =========================================================

    private JPanel createButtonPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                8
                        )
                );

        panel.setOpaque(false);

        JButton printButton =
                new JButton(
                        "PRINT BILL"
                );

        JButton downloadButton =
                new JButton(
                        "DOWNLOAD BILL"
                );

        JButton clearButton =
                new JButton(
                        "CLEAR"
                );

        styleButton(printButton);

        styleButton(downloadButton);

        styleButton(clearButton);

        JLabel paymentLabel =
                new JLabel(
                        "Payment:"
                );

        paymentLabel.setForeground(
                WHITE
        );

        paymentLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        onlineRadio =
                new JRadioButton(
                        "Online"
                );

        hotelRadio =
                new JRadioButton(
                        "Hotel"
                );

        onlineRadio.setOpaque(false);

        hotelRadio.setOpaque(false);

        onlineRadio.setForeground(
                WHITE
        );

        hotelRadio.setForeground(
                WHITE
        );

        ButtonGroup group =
                new ButtonGroup();

        group.add(
                onlineRadio
        );

        group.add(
                hotelRadio
        );

        hotelRadio.setSelected(
                true
        );

        printButton.addActionListener(
                e -> printBill()
        );

        downloadButton.addActionListener(
                e -> downloadBill()
        );

        clearButton.addActionListener(
                e -> clearBillDetails()
        );

        panel.add(paymentLabel);

        panel.add(onlineRadio);

        panel.add(hotelRadio);

        panel.add(printButton);

        panel.add(downloadButton);

        panel.add(clearButton);

        return panel;
    }

    // =========================================================
    // CREATE VALUE LABEL
    // =========================================================

    private JLabel createValueLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                GOLD
        );

        return label;
    }

    // =========================================================
    // CREATE PAYMENT VALUE
    // =========================================================

    private JLabel createPaymentValue(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        label.setForeground(
                GOLD
        );

        return label;
    }

    // =========================================================
    // FORMAT CURRENCY
    // =========================================================

    private String formatCurrency(
            double amount
    ) {

        return String.format(
                "₹%.2f",
                amount
        );
    }

    // =========================================================
    // PRINT BILL
    // =========================================================

    private void printBill() {

        if (currentGuestName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please search a guest first.",
                    "No Bill",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            boolean complete =
                    billTable.print();

            if (complete) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bill printed successfully.",
                        "Print Bill",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (PrinterException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to print bill:\n"
                            + ex.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // DOWNLOAD BILL
    // =========================================================

    private void downloadBill() {

        if (currentGuestName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please search a guest first.",
                    "No Bill",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setSelectedFile(
                new File(
                        "Bill_"
                                + currentBookingId
                                + ".txt"
                )
        );

        int result =
                fileChooser.showSaveDialog(
                        this
                );

        if (result != JFileChooser.APPROVE_OPTION) {

            return;
        }

        File file =
                fileChooser.getSelectedFile();

        try (
                PrintWriter writer =
                        new PrintWriter(file)
        ) {

            writer.println(
                    "=========================================="
            );

            writer.println(
                    "             HOTEL BILL"
            );

            writer.println(
                    "=========================================="
            );

            writer.println();

            writer.println(
                    "Booking ID       : "
                            + currentBookingId
            );

            writer.println(
                    "Guest Name       : "
                            + currentGuestName
            );

            writer.println(
                    "Phone            : "
                            + currentPhone
            );

            writer.println(
                    "Room Number      : "
                            + currentRoomNumber
            );

            writer.println(
                    "Room Type        : "
                            + currentRoomType
            );

            writer.println(
                    "Check-In Date    : "
                            + currentCheckInDate
            );

            writer.println(
                    "Billing Date     : "
                            + getCurrentDate()
            );

            writer.println(
                    "Number of Guests : "
                            + currentNumberOfGuests
            );

            writer.println(
                    "Status           : "
                            + currentStatus
            );

            writer.println();

            writer.println(
                    "------------------------------------------"
            );

            writer.println(
                    "BILL DETAILS"
            );

            writer.println(
                    "------------------------------------------"
            );

            for (
                    int row = 0;
                    row < billTable.getRowCount();
                    row++
            ) {

                writer.println(
                        billTable.getValueAt(
                                row,
                                0
                        )
                                + " | "
                                + billTable.getValueAt(
                                row,
                                1
                        )
                                + " | "
                                + billTable.getValueAt(
                                row,
                                2
                        )
                                + " | "
                                + billTable.getValueAt(
                                row,
                                3
                        )
                );
            }

            writer.println();

            writer.println(
                    "Payment Method   : "
                            + getPaymentMethod()
            );

            writer.println(
                    "Grand Total      : "
                            + formatCurrency(
                            grandTotal
                    )
            );

            writer.println();

            writer.println(
                    "=========================================="
            );

            writer.println(
                    "          THANK YOU FOR STAYING"
            );

            writer.println(
                    "=========================================="
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Bill downloaded successfully.",
                    "Download Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save bill:\n"
                            + ex.getMessage(),
                    "Download Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // PAYMENT METHOD
    // =========================================================

    private String getPaymentMethod() {

        if (onlineRadio.isSelected()) {

            return "Online";
        }

        return "Hotel";
    }

    // =========================================================
    // CLEAR BILL
    // =========================================================

    private void clearBillDetails() {

        currentBookingId = "";
        currentGuestName = "";
        currentRoomNumber = "";
        currentRoomType = "";
        currentCheckInDate = "";
        currentPhone = "";
        currentIdType = "";
        currentNumberOfGuests = "";
        currentStatus = "";

        savedCheckoutAmount = 0.0;

        roomCharge = 0;
        otherCharge = 0;
        grandTotal = 0;

        bookingIdValue.setText("-");
        guestNameValue.setText("-");
        roomNumberValue.setText("-");
        roomTypeValue.setText("-");
        checkInValue.setText("-");
        checkOutValue.setText("-");
        phoneValue.setText("-");
        idTypeValue.setText("-");
        guestsValue.setText("-");
        statusValue.setText("-");

        tableModel.setRowCount(0);

        roomChargeLabel.setText(
                "₹0.00"
        );

        otherChargeLabel.setText(
                "₹0.00"
        );

        paymentGrandTotalLabel.setText(
                "₹0.00"
        );

        grandTotalLabel.setText(
                "Grand Total: ₹0.00"
        );

        guestNameField.setText("");
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
                        12
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                DARK_NAVY
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        GOLD,
                        1
                )
        );

        button.setPreferredSize(
                new Dimension(
                        160,
                        40
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                GOLD
                        );

                        button.setForeground(
                                Color.BLACK
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                DARK_NAVY
                        );

                        button.setForeground(
                                WHITE
                        );
                    }
                }
        );
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
                                        "/resource/background.png"
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

                g.setColor(
                        DARK_NAVY
                );

                g.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

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
                    (double)
                            width
                            /
                            height;

            int drawWidth;

            int drawHeight;

            if (panelRatio > imageRatio) {

                drawWidth =
                        width;

                drawHeight =
                        (int)
                                (
                                        width
                                                /
                                                imageRatio
                                );

            } else {

                drawHeight =
                        height;

                drawWidth =
                        (int)
                                (
                                        height
                                                *
                                                imageRatio
                                );
            }

            int x =
                    (
                            width
                                    -
                                    drawWidth
                    )
                            /
                            2;

            int y =
                    (
                            height
                                    -
                                    drawHeight
                    )
                            /
                            2;

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
                () -> new Billing()
        );
    }
}