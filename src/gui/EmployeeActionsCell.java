package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
//import javax.swing.table.TableCellEditor;
//import javax.swing.table.TableCellRenderer;

public class EmployeeActionsCell {

    public static void apply(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex)
                .setCellRenderer(new Renderer());
        table.getColumnModel().getColumn(columnIndex)
                .setCellEditor(new Editor(table));

        table.setRowHeight(36);
    }

    // ===== عرض الأزرار =====
    static class Renderer extends JPanel implements TableCellRenderer {

        JButton view = new JButton("View Leave");
        JButton manage = new JButton("Manage Contract");

        Renderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            view.setEnabled(false);
            manage.setEnabled(false);

            add(view);
            add(manage);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    // ===== تفاعل الأزرار =====
    static class Editor extends AbstractCellEditor implements TableCellEditor {

        JPanel panel = new JPanel();
        JButton view = new JButton("View Leave");
        JButton manage = new JButton("Manage Contract");
        JTable table;

        Editor(JTable table) {
            this.table = table;

            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            view.addActionListener(e -> openLeave());
            manage.addActionListener(e -> openContract());

            panel.add(view);
            panel.add(manage);
        }

        private void openLeave() {
            int row = table.getSelectedRow();
            String empID = table.getValueAt(row, 0).toString();

            JOptionPane.showMessageDialog(null,
                    "Opening Leave for Employee: " + empID);

            fireEditingStopped();
        }

        private void openContract() {
            int row = table.getSelectedRow();
            String empID = table.getValueAt(row, 0).toString();

            JOptionPane.showMessageDialog(null,
                    "Opening Contract for Employee: " + empID);

            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}
