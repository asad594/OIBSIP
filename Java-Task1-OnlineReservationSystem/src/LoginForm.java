import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginForm() {
        setTitle("Train Reservation System - Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientBackgroundPanel bgPanel = new GradientBackgroundPanel();
        setContentPane(bgPanel);

        GlassPane glassPane = new GlassPane(new GridBagLayout());
        glassPane.setPreferredSize(new Dimension(350, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome Back");
        UIUtils.styleTitle(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        UIUtils.styleLabel(userLabel);
        userField = new JTextField(15);
        UIUtils.styleTextField(userField);

        JLabel passLabel = new JLabel("Password:");
        UIUtils.styleLabel(passLabel);
        passField = new JPasswordField(15);
        UIUtils.stylePasswordField(passField);

        StyledButton loginButton = new StyledButton("Login", new Color(60, 130, 200), Color.WHITE);
        loginButton.addActionListener(this::handleLogin);

        StyledButton createAccountButton = new StyledButton("Create Account", new Color(255, 255, 255, 30), Color.WHITE);
        createAccountButton.addActionListener(e -> {
            new RegistrationForm().setVisible(true);
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        glassPane.add(buttonPanel, gbc);

        bgPanel.add(glassPane);
    }

    private void handleLogin(ActionEvent e) {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT id FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        SwingUtilities.invokeLater(() -> new DashboardForm(username).setVisible(true));
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid credentials. Access Denied.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
