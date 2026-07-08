import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UIUtils {
    
    public static void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    public static void styleTitle(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void styleTextField(JTextField field) {
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        
        Border line = new LineBorder(new Color(255, 255, 255, 150), 1, true);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        field.setBorder(new CompoundBorder(line, empty));
        
        // Custom background painter for the text field
        field.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintBackground(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, field.getWidth()-1, field.getHeight()-1, 10, 10);
                g2.dispose();
            }
        });
    }

    public static void stylePasswordField(JPasswordField field) {
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setEchoChar('*'); // Explicitly set masking character
        
        Border line = new LineBorder(new Color(255, 255, 255, 150), 1, true);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        field.setBorder(new CompoundBorder(line, empty));
        
        // Custom background painter for the password field
        field.setUI(new javax.swing.plaf.basic.BasicPasswordFieldUI() {
            @Override
            protected void paintBackground(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, field.getWidth()-1, field.getHeight()-1, 10, 10);
                g2.dispose();
            }
        });
    }

    public static void styleComboBox(JComboBox<?> box) {
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        // ComboBoxes are notoriously hard to style perfectly transparent in pure Swing, 
        // but we can adjust colors.
        box.setBackground(new Color(255, 255, 255, 220));
        box.setForeground(Color.BLACK);
    }

    public static void styleTextArea(JTextArea area) {
        area.setOpaque(false);
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // Custom background
        area.setUI(new javax.swing.plaf.basic.BasicTextAreaUI() {
            @Override
            protected void paintBackground(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(0, 0, area.getWidth()-1, area.getHeight()-1, 10, 10);
                g2.dispose();
            }
        });
    }
}
