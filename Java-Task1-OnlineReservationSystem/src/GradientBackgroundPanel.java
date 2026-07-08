import javax.swing.*;
import java.awt.*;

public class GradientBackgroundPanel extends JPanel {
    private Color color1;
    private Color color2;

    public GradientBackgroundPanel() {
        // Default modern vibrant gradient (Deep purple to vibrant pink/orange)
        this(new Color(36, 11, 54), new Color(195, 20, 50));
    }

    public GradientBackgroundPanel(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
        setLayout(new GridBagLayout()); // Default layout to center glass panels easily
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        // Diagonal gradient
        GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();
    }
}
