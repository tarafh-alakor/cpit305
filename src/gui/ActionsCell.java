/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ActionsCell {

    public static void apply(JTable table, int actionsColumnIndex) {
        table.getColumnModel().getColumn(actionsColumnIndex)
                .setCellRenderer(new Renderer());
        table.getColumnModel().getColumn(actionsColumnIndex)
                .setCellEditor(new Editor(table));

        table.setRowHeight(36);
    }

    // ===== عرض الخلية =====
    static class Renderer extends JPanel implements TableCellRenderer {
        JButton approve = new JButton("Approve");
        JButton deny = new JButton("Deny");

        Renderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            approve.setBackground(new Color(0, 153, 76)); // أخضر
            approve.setForeground(Color.WHITE);
            approve.setEnabled(false);

            deny.setBackground(new Color(204, 0, 0)); // أحمر
            deny.setForeground(Color.WHITE);
            deny.setEnabled(false);

            add(approve);
            add(deny);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // ===== تفاعل الأزرار =====
    static class Editor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel();
        JButton approve = new JButton("Approve");
        JButton deny = new JButton("Deny");
        JTable table;

        Editor(JTable table) {
            this.table = table;
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            approve.setBackground(new Color(0, 153, 76)); // أخضر
            approve.setForeground(Color.WHITE);

            deny.setBackground(new Color(204, 0, 0)); // أحمر
            deny.setForeground(Color.WHITE);

            approve.addActionListener(e -> action("Approved"));
            deny.addActionListener(e -> action("Denied"));

            panel.add(approve);
            panel.add(deny);
        }

        private void action(String status) {
            int row = table.getSelectedRow();

            // تغيير حالة الطلب في العمود Status (رقمه 5)
            table.setValueAt(status, row, 5);

            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}