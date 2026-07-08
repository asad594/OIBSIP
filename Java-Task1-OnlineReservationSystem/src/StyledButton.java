import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    private Color normalBackgroundColor;
    private int radius = 20;
    private Color borderColor = new Color(255, 255, 255, 150);

    public StyledButton(String text) {
        this(text, new Color(255, 255, 255, 50), Color.WHITE);
    }

    public StyledButton(String text, Color bgColor, Color fgColor) {
        super(text);
        super.setContentAreaFilled(false);
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        
        // Convert the given color to a translucent version if it's solid, to fit the glass theme
        this.normalBackgroundColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 100);
        this.hoverBackgroundColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 180);
        this.pressedBackgroundColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 220);
        
        setForeground(fgColor);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) setBackground(normalBackgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) setBackground(hoverBackgroundColor);
            }
        });
        
        setBackground(normalBackgroundColor);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (!b) {
            setBackground(new Color(150, 150, 150, 50));
            setForeground(new Color(255, 255, 255, 100));
        } else {
            setBackground(normalBackgroundColor);
            setForeground(Color.WHITE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        // Glass border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
