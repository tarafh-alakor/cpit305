package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * ActionsCell - Custom table cell renderer and editor for Leave Requests table.
 * Renders "Approve" and "Deny" buttons in each row, and handles click actions
 * by updating the leave request status in the database.
 *  Demonstrates: Networking in line 107(HRClient), IOSream in line 117(LoggerUtil)
 * Usage: ActionsCell.apply(jTable1, columnIndex);
 */
public class ActionsCell {

    /**
     * Attaches the custom renderer and editor to the specified table column.
     *
     * @param table the JTable to configure
     * @param actionsColumnIndex the column index where buttons should appear
     */
    public static void apply(JTable table, int actionsColumnIndex) {
        table.getColumnModel().getColumn(actionsColumnIndex)
                .setCellRenderer(new Renderer());
        table.getColumnModel().getColumn(actionsColumnIndex)
                .setCellEditor(new Editor(table));
        table.setRowHeight(36);
    }

    // ===== Cell Renderer: displays static (non-interactive) buttons =====
    static class Renderer extends JPanel implements TableCellRenderer {

        JButton approve = new JButton("Approve");
        JButton deny = new JButton("Deny");

        Renderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            approve.setBackground(new Color(0, 153, 76)); // Green
            approve.setForeground(Color.WHITE);
            approve.setEnabled(false);

            deny.setBackground(new Color(204, 0, 0)); // Red
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

    // ===== Cell Editor: handles button clicks and database updates =====
    static class Editor extends AbstractCellEditor implements TableCellEditor {

        JPanel panel = new JPanel();
        JButton approve = new JButton("Approve");
        JButton deny = new JButton("Deny");
        JTable table;

        Editor(JTable table) {
            this.table = table;
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            approve.setBackground(new Color(0, 153, 76));
            approve.setForeground(Color.WHITE);

            deny.setBackground(new Color(204, 0, 0));
            deny.setForeground(Color.WHITE);

            approve.addActionListener(e -> updateStatus("Approved"));
            deny.addActionListener(e -> updateStatus("Rejected"));

            panel.add(approve);
            panel.add(deny);
        }

        /**
         * Updates the status of the selected leave request in the database.
         * Reads the id from column 0 of the selected row.
         *
         * @param status "Approved" or "Denied"
         */
        private void updateStatus(String status) {
            int row = table.getSelectedRow();
            int leaveId = (int) table.getValueAt(row, 0); // id is in column 0

            try {
                java.sql.Connection con = database.DBConnection.getConnection();

                // Update status using the correct primary key column: id
                String sql = "UPDATE leave_requests SET status=? WHERE id=?";
                java.sql.PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, status);
                ps.setInt(2, leaveId);
                ps.executeUpdate();

                // Networking: Sends leave status update notification to HR server
                try {
                    network.HRClient.sendNotification(
                            "Leave request updated for employee ID: " + leaveId
                            + " | New Status: " + status
                    );
                } catch (Exception ex) {
                    System.out.println("Notification skipped: " + ex.getMessage());
                }

                // IOStream: Logs leave request status updates into log file
                try {
                    utils.LoggerUtil.log(
                            "leave_requests_log.txt",
                            "Leave request ID " + leaveId
                            + " updated to status: " + status
                    );
                } catch (Exception ex) {
                    System.out.println("Logging failed: " + ex.getMessage());
                }
                // Reflect the change immediately in the table UI
                table.setValueAt(status, row, 5);

                javax.swing.JOptionPane.showMessageDialog(null, "Status updated to: " + status);

            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Error updating status: " + e.getMessage());
            }

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
