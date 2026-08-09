package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * Visual helper class only. It changes colors, fonts, backgrounds, and tables.
 * It does not change system logic.
 */
public class VisualStyle {

    public static final Color GREEN = new Color(0, 199, 161);
    public static final Color DARK = new Color(35, 43, 52);
    public static final Color LIGHT_BG = new Color(250, 255, 252);
    public static final Color SOFT_GREEN = new Color(221, 245, 229);
    public static final Color SOFT_BLUE = new Color(224, 247, 250);
    public static final Color SOFT_YELLOW = new Color(255, 248, 220);

    public static void styleButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (button.getText() != null && button.getText().toLowerCase().contains("log")) {
            button.setForeground(new Color(210, 0, 0));
            button.setBackground(Color.WHITE);
            return;
        }

        if (button.getBackground() == null || button.getBackground().equals(new Color(238,238,238))) {
            button.setBackground(GREEN);
            button.setForeground(Color.WHITE);
        }
    }

    public static void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", label.getFont().isBold() ? Font.BOLD : Font.PLAIN, Math.max(label.getFont().getSize(), 14)));
        label.setForeground(DARK);
    }

    public static void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 210, 205), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        field.setBackground(Color.WHITE);
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(new Color(170, 205, 200), 1, true));
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(225, 235, 232));
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(200, 245, 235));
        table.setSelectionForeground(DARK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(GREEN);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 34));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(3, 8, 3, 8));

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 253, 251));
                    c.setForeground(DARK);

                    if (value != null) {
                        String text = value.toString().toLowerCase();
                        if (text.contains("expired")) {
                            c.setBackground(new Color(255, 230, 230));
                            c.setForeground(new Color(160, 20, 20));
                        } else if (text.contains("expiring")) {
                            c.setBackground(new Color(255, 246, 190));
                            c.setForeground(new Color(145, 90, 0));
                        } else if (text.contains("active") || text.contains("approved")) {
                            c.setBackground(new Color(220, 250, 230));
                            c.setForeground(new Color(0, 120, 70));
                        }
                    }
                }
                return c;
            }
        });
    }

    public static void stylePanel(JPanel panel) {
        panel.setOpaque(false);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 235, 232), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
    }

    public static void apply(Component component) {
        if (component instanceof JButton) {
            styleButton((JButton) component);
        } else if (component instanceof JLabel) {
            styleLabel((JLabel) component);
        } else if (component instanceof JTextField) {
            styleTextField((JTextField) component);
        } else if (component instanceof JComboBox) {
            styleComboBox((JComboBox<?>) component);
        } else if (component instanceof JTable) {
            styleTable((JTable) component);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                apply(child);
            }
        }
    }
}
