import javax.swing.*;
import java.awt.*;

public class DashboardForm extends JFrame {

    public DashboardForm(String username) {
        setTitle("Train Reservation System - Dashboard");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientBackgroundPanel bgPanel = new GradientBackgroundPanel();
        setContentPane(bgPanel);

        GlassPane glassPane = new GlassPane(new GridBagLayout());
        glassPane.setPreferredSize(new Dimension(350, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        UIUtils.styleTitle(welcomeLabel);

        StyledButton bookButton = new StyledButton("Book Ticket", new Color(34, 139, 34), Color.WHITE);
        bookButton.addActionListener(e -> {
            new ReservationForm().setVisible(true);
            this.dispose();
        });

        StyledButton cancelButton = new StyledButton("Cancel Ticket", new Color(220, 20, 60), Color.WHITE);
        cancelButton.addActionListener(e -> {
            new CancellationForm().setVisible(true);
            this.dispose();
        });

        StyledButton logoutButton = new StyledButton("Logout", new Color(255, 255, 255, 30), Color.WHITE);
        logoutButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        gbc.gridx = 0; gbc.gridy = 0;
        glassPane.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        glassPane.add(bookButton, gbc);

        gbc.gridy = 2;
        glassPane.add(cancelButton, gbc);

        gbc.gridy = 3;
        glassPane.add(logoutButton, gbc);

        bgPanel.add(glassPane);
    }
}
