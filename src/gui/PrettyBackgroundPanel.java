package gui;

import java.awt.*;
import javax.swing.JPanel;

/**
 * Decorative background panel for the GUI.
 */
public class PrettyBackgroundPanel extends JPanel {

    public PrettyBackgroundPanel() {
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, new Color(252, 255, 253),
                w, h, new Color(238, 255, 247));
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(new Color(220, 232, 230, 115));
        for (int x = -h; x < w + h; x += 115) {
            g2.drawLine(x, h, x + h, 0);
        }

        g2.setColor(new Color(187, 247, 208, 115));
        g2.fillOval(w - 350, 55, 260, 260);

        g2.setColor(new Color(153, 246, 228, 90));
        g2.fillOval(w - 210, 260, 190, 190);

        g2.setColor(new Color(254, 240, 138, 80));
        g2.fillOval(70, h - 220, 165, 165);

        g2.setColor(new Color(221, 245, 229, 140));
        g2.fillOval(390, 120, 42, 42);

        g2.setColor(new Color(204, 251, 241, 160));
        g2.fillOval(w - 520, 160, 26, 26);

        g2.setColor(new Color(253, 224, 171, 120));
        g2.fillOval(w - 450, 120, 28, 28);

        g2.dispose();
    }
}
