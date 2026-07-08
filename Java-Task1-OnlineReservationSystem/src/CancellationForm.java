import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CancellationForm extends JFrame {

    private JTextField pnrField;
    private JTextArea detailsArea;
    private StyledButton confirmButton;

    public CancellationForm() {
        setTitle("Cancel Ticket");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientBackgroundPanel bgPanel = new GradientBackgroundPanel();
        setContentPane(bgPanel);

        GlassPane glassPane = new GlassPane(new GridBagLayout());
        glassPane.setPreferredSize(new Dimension(420, 380));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Ticket Cancellation");
        UIUtils.styleTitle(titleLabel);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        glassPane.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel pnrLabel = new JLabel("PNR Number:");
        UIUtils.styleLabel(pnrLabel);
        pnrField = new JTextField(12);
        UIUtils.styleTextField(pnrField);
        
        StyledButton fetchButton = new StyledButton("Fetch", new Color(60, 130, 200), Color.WHITE);
        fetchButton.addActionListener(e -> fetchDetails());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        topPanel.setOpaque(false);
        topPanel.add(pnrLabel);
        topPanel.add(pnrField);
        topPanel.add(fetchButton);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        glassPane.add(topPanel, gbc);

        detailsArea = new JTextArea(8, 30);
        UIUtils.styleTextArea(detailsArea);
        detailsArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        glassPane.add(scrollPane, gbc);

        confirmButton = new StyledButton("Confirm Cancellation", new Color(220, 20, 60), Color.WHITE);
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> cancelTicket());
        
        StyledButton backButton = new StyledButton("Back", new Color(255, 255, 255, 30), Color.WHITE);
        backButton.addActionListener(e -> {
            new DashboardForm("User").setVisible(true);
            this.dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        glassPane.add(bottomPanel, gbc);

        bgPanel.add(glassPane);
    }

    private void fetchDetails() {
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter PNR Number.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT r.*, t.train_name FROM reservations r " +
                           "JOIN trains t ON r.train_number = t.train_number " +
                           "WHERE r.pnr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, pnr);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String details = "Booking Details:\n\n" +
                                         "PNR: " + rs.getString("pnr") + "\n" +
                                         "Passenger: " + rs.getString("passenger_name") + "\n" +
                                         "Train: " + rs.getInt("train_number") + " - " + rs.getString("train_name") + "\n" +
                                         "Class: " + rs.getString("class_type") + "\n" +
                                         "Date: " + rs.getString("date_of_journey") + "\n" +
                                         "From: " + rs.getString("source_station") + "\n" +
                                         "To: " + rs.getString("destination_station");
                        detailsArea.setText(details);
                        confirmButton.setEnabled(true);
                    } else {
                        detailsArea.setText("No booking found for PNR: " + pnr);
                        confirmButton.setEnabled(false);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelTicket() {
        String pnr = pnrField.getText().trim();
        int opt = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the ticket with PNR: " + pnr + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        
        if (opt == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseManager.getConnection()) {
                String query = "DELETE FROM reservations WHERE pnr = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, pnr);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Ticket cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        detailsArea.setText("");
                        pnrField.setText("");
                        confirmButton.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
