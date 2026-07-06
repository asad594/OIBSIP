import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Login screen: username + password. On success, hands off to the Profile
 * screen.
 */
@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

    private final ExamFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;

    public LoginPanel(ExamFrame frame) {
        this.frame = frame;
        setOpaque(false); // Let GradientPanel show through
        setLayout(new GridBagLayout());

        GlassPanel innerPanel = new GlassPanel(25, 230); // 230 alpha (semi-transparent white)
        innerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Welcome to Exam Portal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 60, 100));
        innerPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(new Color(60, 60, 60));
        innerPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new ModernTextField(15);
        innerPanel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(new Color(60, 60, 60));
        innerPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new ModernPasswordField(15);
        innerPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 5, 10);

        ModernButton loginButton = new ModernButton("Login", new Color(0, 120, 215), new Color(0, 90, 180));
        loginButton.setPreferredSize(new Dimension(150, 45));
        loginButton.addActionListener(e -> attemptLogin());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(loginButton);
        innerPanel.add(btnPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 10, 10);
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        messageLabel.setForeground(new Color(220, 53, 69));
        innerPanel.add(messageLabel, gbc);

        add(innerPanel);

        // Allow pressing Enter in the password field to submit
        passwordField.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = frame.authenticate(username, password);
        if (user != null) {
            frame.setCurrentUser(user);
            messageLabel.setText(" ");
            usernameField.setText("");
            passwordField.setText("");
            frame.showProfile();
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }
}