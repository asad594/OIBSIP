import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationForm extends JFrame {

    private JTextField nameField, trainNoField, trainNameField;
    private JTextField sourceField, destField, dateField;
    private JComboBox<String> classBox;

    public ReservationForm() {
        setTitle("Book Ticket");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientBackgroundPanel bgPanel = new GradientBackgroundPanel();
        setContentPane(bgPanel);

        GlassPane glassPane = new GlassPane(new GridBagLayout());
        glassPane.setPreferredSize(new Dimension(450, 420));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Book Reservation");
        UIUtils.styleTitle(titleLabel);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        glassPane.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel nameLabel = new JLabel("Passenger Name:");
        UIUtils.styleLabel(nameLabel);
        nameField = new JTextField(15);
        UIUtils.styleTextField(nameField);
        
        gbc.gridy = 1; gbc.gridx = 0;
        glassPane.add(nameLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(nameField, gbc);

        JLabel trainNoLabel = new JLabel("Train Number:");
        UIUtils.styleLabel(trainNoLabel);
        trainNoField = new JTextField(15);
        UIUtils.styleTextField(trainNoField);

        gbc.gridy = 2; gbc.gridx = 0;
        glassPane.add(trainNoLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(trainNoField, gbc);

        JLabel trainNameLabel = new JLabel("Train Name:");
        UIUtils.styleLabel(trainNameLabel);
        trainNameField = new JTextField(15);
        UIUtils.styleTextField(trainNameField);
        trainNameField.setEditable(false);

        gbc.gridy = 3; gbc.gridx = 0;
        glassPane.add(trainNameLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(trainNameField, gbc);

        JLabel classLabel = new JLabel("Class Type:");
        UIUtils.styleLabel(classLabel);
        classBox = new JComboBox<>(new String[]{"1AC", "2AC", "3AC", "Sleeper", "General"});
        UIUtils.styleComboBox(classBox);

        gbc.gridy = 4; gbc.gridx = 0;
        glassPane.add(classLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(classBox, gbc);

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        UIUtils.styleLabel(dateLabel);
        dateField = new JTextField(15);
        UIUtils.styleTextField(dateField);

        gbc.gridy = 5; gbc.gridx = 0;
        glassPane.add(dateLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(dateField, gbc);

        JLabel sourceLabel = new JLabel("Source Station:");
        UIUtils.styleLabel(sourceLabel);
        sourceField = new JTextField(15);
        UIUtils.styleTextField(sourceField);

        gbc.gridy = 6; gbc.gridx = 0;
        glassPane.add(sourceLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(sourceField, gbc);

        JLabel destLabel = new JLabel("Destination:");
        UIUtils.styleLabel(destLabel);
        destField = new JTextField(15);
        UIUtils.styleTextField(destField);

        gbc.gridy = 7; gbc.gridx = 0;
        glassPane.add(destLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(destField, gbc);

        StyledButton bookButton = new StyledButton("Book Ticket", new Color(34, 139, 34), Color.WHITE);
        StyledButton backButton = new StyledButton("Back", new Color(255, 255, 255, 30), Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(bookButton);
        buttonPanel.add(backButton);

        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2;
        glassPane.add(buttonPanel, gbc);

        bgPanel.add(glassPane);

        // Event Listeners
        backButton.addActionListener(e -> {
            new DashboardForm("User").setVisible(true);
            this.dispose();
        });

        trainNoField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                fetchTrainName();
            }
        });

        bookButton.addActionListener(e -> bookTicket());
    }

    private void fetchTrainName() {
        String trainNoStr = trainNoField.getText().trim();
        if (trainNoStr.isEmpty()) return;

        try {
            int trainNo = Integer.parseInt(trainNoStr);
            try (Connection conn = DatabaseManager.getConnection()) {
                String query = "SELECT train_name FROM trains WHERE train_number = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, trainNo);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            trainNameField.setText(rs.getString("train_name"));
                        } else {
                            trainNameField.setText("Train not found");
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            trainNameField.setText("Invalid Train No");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void bookTicket() {
        String name = nameField.getText().trim();
        String trainNoStr = trainNoField.getText().trim();
        String classType = (String) classBox.getSelectedItem();
        String dateStr = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String dest = destField.getText().trim();
        String trainName = trainNameField.getText().trim();

        if (name.isEmpty() || trainNoStr.isEmpty() || dateStr.isEmpty() || source.isEmpty() || dest.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (trainName.equals("Train not found") || trainName.equals("Invalid Train No") || trainName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Valid Train Number is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int trainNo;
        try {
            trainNo = Integer.parseInt(trainNoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Train Number must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pnr = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "INSERT INTO reservations (pnr, passenger_name, train_number, class_type, date_of_journey, source_station, destination_station) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, pnr);
                stmt.setString(2, name);
                stmt.setInt(3, trainNo);
                stmt.setString(4, classType);
                stmt.setString(5, dateStr);
                stmt.setString(6, source);
                stmt.setString(7, dest);

                stmt.executeUpdate();
                
                String details = "Ticket Booked Successfully!\n\n" +
                                 "PNR: " + pnr + "\n" +
                                 "Name: " + name + "\n" +
                                 "Train: " + trainNo + " - " + trainName + "\n" +
                                 "Class: " + classType + "\n" +
                                 "Date: " + dateStr + "\n" +
                                 "From: " + source + " To: " + dest;
                JOptionPane.showMessageDialog(this, details, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
                
                nameField.setText("");
                trainNoField.setText("");
                trainNameField.setText("");
                dateField.setText("");
                sourceField.setText("");
                destField.setText("");
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
