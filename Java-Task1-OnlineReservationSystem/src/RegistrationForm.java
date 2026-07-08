import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrationForm extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;

    public RegistrationForm() {
        setTitle("Train Reservation System - Create Account");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientBackgroundPanel bgPanel = new GradientBackgroundPanel();
        setContentPane(bgPanel);

        GlassPane glassPane = new GlassPane(new GridBagLayout());
        glassPane.setPreferredSize(new Dimension(350, 300));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Register");
        UIUtils.styleTitle(titleLabel);

        JLabel userLabel = new JLabel("New Username:");
        UIUtils.styleLabel(userLabel);
        userField = new JTextField(15);
        UIUtils.styleTextField(userField);

        JLabel passLabel = new JLabel("Password:");
        UIUtils.styleLabel(passLabel);
        passField = new JPasswordField(15);
        UIUtils.stylePasswordField(passField);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        UIUtils.styleLabel(confirmPassLabel);
        confirmPassField = new JPasswordField(15);
        UIUtils.stylePasswordField(confirmPassField);

        StyledButton registerButton = new StyledButton("Register", new Color(34, 139, 34), Color.WHITE);
        registerButton.addActionListener(this::handleRegister);

        StyledButton backButton = new StyledButton("Back to Login", new Color(255, 255, 255, 30), Color.WHITE);
        backButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        glassPane.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        glassPane.add(userLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        glassPane.add(passLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        glassPane.add(confirmPassLabel, gbc);
        gbc.gridx = 1;
        glassPane.add(confirmPassField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        glassPane.add(buttonPanel, gbc);

        bgPanel.add(glassPane);
    }

    private void handleRegister(ActionEvent e) {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String confirmPassword = new String(confirmPassField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String checkQuery = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account Created Successfully!\nYou can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LoginForm().setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
