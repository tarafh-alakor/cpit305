package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Simple decorative background for the system screens.
 * It keeps the original screen colors, but adds soft shapes in the background.
 */
public class BackgroundPanel extends javax.swing.JPanel {

    public BackgroundPanel() {
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Soft circles in the corners
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.16f));
        g2.setColor(new Color(0, 204, 153));
        g2.fillOval(w - 210, 25, 170, 170);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
        g2.setColor(new Color(0, 120, 215));
        g2.fillOval(w - 145, 135, 95, 95);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
        g2.setColor(new Color(255, 193, 7));
        g2.fillOval(35, h - 175, 130, 130);

        // Light diagonal lines for a modern dashboard look
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.09f));
        g2.setColor(new Color(80, 80, 80));
        g2.setStroke(new BasicStroke(2f));
        for (int x = -h; x < w; x += 55) {
            g2.drawLine(x, h, x + h, 0);
        }

        g2.dispose();
    }
}
