import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Profile update screen shown right after login, before the exam starts.
 * Lets the student change their display name and/or password.
 */
@SuppressWarnings("serial")
public class ProfilePanel extends JPanel {

    private final ExamFrame frame;
    private final JTextField displayNameField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmPasswordField;
    private final JLabel messageLabel;

    public ProfilePanel(ExamFrame frame) {
        this.frame = frame;
        setOpaque(false); // Let GradientPanel show through
        setLayout(new GridBagLayout());

        GlassPanel innerPanel = new GlassPanel(25, 230); // 230 alpha
        innerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Update Your Profile", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(30, 60, 100));
        innerPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel nameLabel = new JLabel("Display Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(60, 60, 60));
        innerPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        displayNameField = new ModernTextField(15);
        innerPanel.add(displayNameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel newPassLabel = new JLabel("New Password (optional):");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newPassLabel.setForeground(new Color(60, 60, 60));
        innerPanel.add(newPassLabel, gbc);

        gbc.gridx = 1;
        newPasswordField = new ModernPasswordField(15);
        innerPanel.add(newPasswordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmPassLabel.setForeground(new Color(60, 60, 60));
        innerPanel.add(confirmPassLabel, gbc);

        gbc.gridx = 1;
        confirmPasswordField = new ModernPasswordField(15);
        innerPanel.add(confirmPasswordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        messageLabel.setForeground(new Color(220, 53, 69));
        innerPanel.add(messageLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(25, 10, 5, 10);
        
        ModernButton startButton = new ModernButton("Save & Start Exam", new Color(40, 167, 69), new Color(30, 130, 50));
        startButton.setPreferredSize(new Dimension(200, 45));
        startButton.addActionListener(e -> saveAndStart());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(startButton);
        innerPanel.add(btnPanel, gbc);

        add(innerPanel);
    }

    /** Called every time this screen is shown, to pre-fill the current values. */
    public void refreshFields() {
        User user = frame.getCurrentUser();
        if (user != null) {
            displayNameField.setText(user.getDisplayName());
        }
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        messageLabel.setText(" ");
    }

    private void saveAndStart() {
        User user = frame.getCurrentUser();
        String newDisplayName = displayNameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (newDisplayName.isEmpty()) {
            messageLabel.setText("Display name cannot be empty.");
            return;
        }

        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }
            user.setPassword(newPassword);
        }

        user.setDisplayName(newDisplayName);
        messageLabel.setText(" ");
        frame.startExam();
    }
}