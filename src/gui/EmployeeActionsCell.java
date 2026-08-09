package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * EmployeeActionsCell - Custom table cell renderer and editor for the Employees table.
 * Usage: EmployeeActionsCell.apply(jTable1, columnIndex);
 */
public class EmployeeActionsCell {

    /**
     * Attaches the custom renderer and editor to the given table column.
     * @param table       the JTable to configure
     * @param columnIndex the column index where action buttons appear
     */
    public static void apply(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(new Renderer());
        table.getColumnModel().getColumn(columnIndex).setCellEditor(new Editor(table));
        table.setRowHeight(36);
    }

    // ===== Renderer: shows static non-interactive buttons =====
    static class Renderer extends JPanel implements TableCellRenderer {

        JButton view   = new JButton("View Leave");
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

    // ===== Editor: handles button clicks =====
    static class Editor extends AbstractCellEditor implements TableCellEditor {

        JPanel panel   = new JPanel();
        JButton view   = new JButton("View Leave");
        JButton manage = new JButton("Manage Contract");
        JTable table;

        Editor(JTable table) {
            this.table = table;
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            view.addActionListener(e -> openLeaveView());
            manage.addActionListener(e -> openContractView());

            panel.add(view);
            panel.add(manage);
        }

        /** Opens the LeaveRequests screen for the selected employee. */
        private void openLeaveView() {
            int row = table.getSelectedRow();
            String empID = table.getValueAt(row, 0).toString();
            fireEditingStopped();
            new LeaveRequests().setVisible(true);
        }

        /** Opens the Contract management screen for the selected employee. */
        private void openContractView() {
            int row = table.getSelectedRow();
            String empID = table.getValueAt(row, 0).toString();
            fireEditingStopped();
            // Pass empId to Contract so it can pre-select this employee
            new Contract(empID).setVisible(true);
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
