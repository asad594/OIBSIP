import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Entry point for the Online Examination System.
 *
 * Demo Login Credentials:
 *   Username: student1 | Password: pass123
 *   Username: student2 | Password: pass456
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(ExamFrame::new);
    }
}

// --- UI Utilities for Premium Look ---

@SuppressWarnings("serial")
class GradientPanel extends JPanel {
    private Color color1 = new Color(30, 87, 153); // Deep blue
    private Color color2 = new Color(125, 185, 232); // Light blue

    public GradientPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}

@SuppressWarnings("serial")
class GlassPanel extends JPanel {
    private int radius;
    private Color bgColor;

    public GlassPanel(int radius, int alpha) {
        this.radius = radius;
        this.bgColor = new Color(255, 255, 255, alpha); // White with transparency
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        g2.dispose();
    }
}

@SuppressWarnings("serial")
class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private int radius;

    public ModernButton(String text, Color normalColor, Color hoverColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.radius = 15;
        
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        g2.dispose();
        super.paintComponent(g);
    }
}

@SuppressWarnings("serial")
class ModernTextField extends JTextField {
    public ModernTextField(int columns) {
        super(columns);
        setup();
    }
    private void setup() {
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)),
            new EmptyBorder(5, 5, 5, 5)
        ));
    }
}

@SuppressWarnings("serial")
class ModernPasswordField extends JPasswordField {
    public ModernPasswordField(int columns) {
        super(columns);
        setup();
    }
    private void setup() {
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)),
            new EmptyBorder(5, 5, 5, 5)
        ));
    }
}