import javax.swing.*;
import java.awt.*;

public class GlassPane extends JPanel {
    private int radius = 20;
    private Color backgroundColor = new Color(255, 255, 255, 40); // Semi-transparent white
    private Color borderColor = new Color(255, 255, 255, 100);    // Semi-transparent border

    public GlassPane() {
        setOpaque(false); // Important for glass effect
    }

    public GlassPane(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Fill background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, width - 1, height - 1, radius, radius);

        // Draw border for the 'frosted glass' specular highlight
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
