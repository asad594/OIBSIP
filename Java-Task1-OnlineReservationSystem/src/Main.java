import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Checking and downloading dependencies...");
        DependencyLoader.loadDependencies();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Initializing Database...");
        DatabaseManager.initializeDatabase();
        
        System.out.println("Starting Application...");
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
