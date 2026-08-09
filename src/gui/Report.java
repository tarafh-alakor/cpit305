/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

/**
 * Report - HR Report generation screen. Generates employee reports as .txt or
 * .csv files using Java IOStreams (FileWriter). Supports filtering by employee
 * name and department. Demonstrates IOStream: FileWriter writes report data to
 * disk files.
 */
public class Report extends javax.swing.JFrame {

    // Report table is created in code so the .form file does not need new classes or frames.
    private javax.swing.JTable reportTable;
    private javax.swing.table.DefaultTableModel reportModel;
    private javax.swing.JScrollPane reportScrollPane;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Report.class.getName());

    /**
     * Creates new form REPOR
     */
    public Report() {
        initComponents();
        
        // Apply visual styling only.
        getContentPane().setBackground(new java.awt.Color(250, 255, 252));
        VisualStyle.apply(getContentPane());
jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Employee Report", "Leave Report", "Department Report", "Contract Report"
        }));

        jComboBox2.setEditable(false);
        jComboBox3.setEditable(false);
        jComboBox4.setEditable(false);
        jComboBox5.setEditable(false);
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "All", "Active", "Resigned", "Terminated", "Pending", "Approved", "Rejected", "Expired"
        }));

        loadEmployeesToComboBox();
        installReportTable();
        setupDateChooserField(jFormattedTextField1, "Select start date");
        setupDateChooserField(jFormattedTextField2, "Select end date");
        improveReportStyle();
    }

    /**
     * Adds a results table under the filter area. Generate/Export uses this
     * same table.
     */
    private void installReportTable() {
        reportModel = new javax.swing.table.DefaultTableModel();
        reportTable = new javax.swing.JTable(reportModel);
        reportTable.setRowHeight(24);
        reportScrollPane = new javax.swing.JScrollPane(reportTable);
        reportScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Report Results"));

        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel2.setLayout(new java.awt.BorderLayout(0, 8));
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        topPanel.setBackground(java.awt.Color.WHITE);
        topPanel.add(jPanel3, java.awt.BorderLayout.NORTH);
        topPanel.add(jLabel8, java.awt.BorderLayout.CENTER);
        topPanel.add(jPanel4, java.awt.BorderLayout.SOUTH);
        jPanel2.removeAll();
        jPanel2.add(topPanel, java.awt.BorderLayout.NORTH);
        jPanel2.add(reportScrollPane, java.awt.BorderLayout.CENTER);
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    /**
     * Makes a date field behave like a small DateChooser.
     */
    private void setupDateChooserField(javax.swing.JFormattedTextField field, String placeholder) {
        field.setFormatterFactory(null);
        field.setText(placeholder);
        field.setForeground(new java.awt.Color(120, 120, 120));
        field.setEditable(false);
        field.setBackground(java.awt.Color.WHITE);
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(150, 165, 180)),
                javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        field.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        field.setToolTipText("Click to choose a date");

        field.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showDateChooser(field, placeholder);
            }
        });
    }

    /**
     * Opens a small calendar popup and writes the selected date as yyyy-MM-dd.
     */
    private void showDateChooser(javax.swing.JFormattedTextField target, String placeholder) {
        javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
        popup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(160, 160, 160)));

        java.time.LocalDate currentDate = java.time.LocalDate.now();
        String currentText = target.getText() == null ? "" : target.getText().trim();
        try {
            if (!currentText.isEmpty() && !currentText.equals(placeholder) && !currentText.startsWith("Select")) {
                currentDate = java.time.LocalDate.parse(currentText);
            }
        } catch (Exception ignored) {
            currentDate = java.time.LocalDate.now();
        }

        final java.time.YearMonth[] month = {java.time.YearMonth.from(currentDate)};
        javax.swing.JPanel main = new javax.swing.JPanel(new java.awt.BorderLayout(6, 6));
        main.setBackground(java.awt.Color.WHITE);
        main.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        javax.swing.JPanel header = new javax.swing.JPanel(new java.awt.BorderLayout(5, 5));
        header.setBackground(java.awt.Color.WHITE);
        javax.swing.JButton previous = new javax.swing.JButton("<");
        javax.swing.JButton next = new javax.swing.JButton(">");
        javax.swing.JLabel title = new javax.swing.JLabel("", javax.swing.SwingConstants.CENTER);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        header.add(previous, java.awt.BorderLayout.WEST);
        header.add(title, java.awt.BorderLayout.CENTER);
        header.add(next, java.awt.BorderLayout.EAST);

        javax.swing.JPanel calendarGrid = new javax.swing.JPanel(new java.awt.GridLayout(0, 7, 3, 3));
        calendarGrid.setBackground(java.awt.Color.WHITE);

        final Runnable[] refresh = new Runnable[1];
        refresh[0] = () -> {
            calendarGrid.removeAll();
            title.setText(month[0].getMonth().toString() + " " + month[0].getYear());

            String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String d : days) {
                javax.swing.JLabel dayLabel = new javax.swing.JLabel(d, javax.swing.SwingConstants.CENTER);
                dayLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
                calendarGrid.add(dayLabel);
            }

            java.time.LocalDate first = month[0].atDay(1);
            int blanks = first.getDayOfWeek().getValue() % 7;
            for (int i = 0; i < blanks; i++) {
                calendarGrid.add(new javax.swing.JLabel(""));
            }

            int daysInMonth = month[0].lengthOfMonth();
            for (int day = 1; day <= daysInMonth; day++) {
                final java.time.LocalDate selectedDate = month[0].atDay(day);
                javax.swing.JButton dayButton = new javax.swing.JButton(String.valueOf(day));
                dayButton.setFocusPainted(false);
                dayButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
                dayButton.setBackground(java.awt.Color.WHITE);
                dayButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                dayButton.addActionListener(e -> {
                    target.setText(selectedDate.toString());
                    target.setForeground(java.awt.Color.BLACK);
                    popup.setVisible(false);
                });
                calendarGrid.add(dayButton);
            }

            calendarGrid.revalidate();
            calendarGrid.repaint();
            popup.pack();
        };

        previous.addActionListener(e -> {
            month[0] = month[0].minusMonths(1);
            refresh[0].run();
        });
        next.addActionListener(e -> {
            month[0] = month[0].plusMonths(1);
            refresh[0].run();
        });

        main.add(header, java.awt.BorderLayout.NORTH);
        main.add(calendarGrid, java.awt.BorderLayout.CENTER);
        popup.add(main);
        refresh[0].run();
        popup.show(target, 0, target.getHeight());
    }

    /**
     * Small visual improvements for the report screen.
     */
    private void improveReportStyle() {
        java.awt.Font labelFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        java.awt.Font buttonFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13);

        javax.swing.JLabel[] labels = {jLabel11, jLabel12, jLabel23, jLabel24, jLabel25, jLabel26};
        for (javax.swing.JLabel label : labels) {
            label.setFont(labelFont);
            label.setForeground(new java.awt.Color(40, 40, 40));
        }

        javax.swing.JButton[] buttons = {jButton1, jButton7, jButton8};
        for (javax.swing.JButton button : buttons) {
            button.setFont(buttonFont);
            button.setFocusPainted(false);
            button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        reportTable.setRowHeight(28);
        reportTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        reportTable.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        reportTable.getTableHeader().setBackground(new java.awt.Color(0, 204, 153));
        reportTable.getTableHeader().setForeground(java.awt.Color.WHITE);
        reportTable.setShowGrid(true);
        reportTable.setGridColor(new java.awt.Color(220, 220, 220));
    }

    /**
     * Loads all employee names from the database into the Employee filter
     * ComboBox. Adds "All" as default first option, then each employee's full
     * name.
     */
    private void loadEmployeesToComboBox() {
        try {
            java.sql.Connection con = database.DBConnection.getConnection();
            String sql = "SELECT full_name FROM employees ORDER BY full_name";
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            // Clear and re-add to avoid duplicates on re-open
            jComboBox3.removeAllItems();
            jComboBox3.addItem("All");
            while (rs.next()) {
                jComboBox3.addItem(rs.getString("full_name"));
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Could not load employees: " + e.getMessage());
        }
    }

    /**
     * Builds the SQL query and fetches report data based on current filter
     * selections. Supports Employee Report (from employees table) and Leave
     * Report (from leave_requests table).
     *
     * @return ResultSet with the report data, or null on error
     */
    /**
     * Builds and executes the correct SQL query based on filters. Supports
     * Employee Report and Leave Report types. Applies employee, department, and
     * date-range filters as needed.
     */
    /**
     * Builds and executes the correct SQL query based on the selected report
     * filters.
     */
    private java.sql.ResultSet fetchReportData(java.sql.Connection con) throws Exception {
        String reportType = selected(jComboBox2);
        String employee = selected(jComboBox3);
        String department = selected(jComboBox4);
        String status = selected(jComboBox5);
        String startTxt = cleanDate(jFormattedTextField1.getText());
        String endTxt = cleanDate(jFormattedTextField2.getText());

        java.util.List<Object> params = new java.util.ArrayList<>();
        String sql;

        if (reportType.equals("Leave Report")) {
            sql = "SELECT emp_name AS Employee, leave_type AS Leave_Type, start_date AS Start_Date, end_date AS End_Date, total_days AS Days, status AS Status "
                    + "FROM leave_requests WHERE 1=1";
            if (!employee.equals("All")) {
                sql += " AND emp_name = ?";
                params.add(employee);
            }
            if (!status.equals("All")) {
                sql += " AND status = ?";
                params.add(status);
            }
            if (!startTxt.isEmpty()) {
                sql += " AND start_date >= ?";
                params.add(java.sql.Date.valueOf(startTxt));
            }
            if (!endTxt.isEmpty()) {
                sql += " AND end_date <= ?";
                params.add(java.sql.Date.valueOf(endTxt));
            }
            sql += " ORDER BY start_date DESC";
        } else if (reportType.equals("Contract Report")) {
            sql = "SELECT c.emp_name AS Employee_ID, e.full_name AS Name, c.contract_type AS Contract_Type, "
                    + "c.start_date AS Start_Date, c.end_date AS End_Date, c.status AS Status "
                    + "FROM contracts c LEFT JOIN employees e ON c.emp_name = e.emp_id WHERE 1=1";
            if (!employee.equals("All")) {
                sql += " AND (e.full_name = ? OR c.emp_name = ?)";
                params.add(employee);
                params.add(employee);
            }
            if (!department.equals("All")) {
                sql += " AND e.department = ?";
                params.add(department);
            }
            if (!status.equals("All")) {
                sql += " AND c.status = ?";
                params.add(status);
            }
            if (!startTxt.isEmpty()) {
                sql += " AND c.start_date >= ?";
                params.add(java.sql.Date.valueOf(startTxt));
            }
            if (!endTxt.isEmpty()) {
                sql += " AND c.end_date <= ?";
                params.add(java.sql.Date.valueOf(endTxt));
            }
            sql += " ORDER BY c.id DESC";
        } else {
            sql = "SELECT emp_id AS ID, full_name AS Name, department AS Department, join_date AS Join_Date, email AS Email, phone AS Phone "
                    + "FROM employees WHERE 1=1";
            if (!employee.equals("All")) {
                sql += " AND full_name = ?";
                params.add(employee);
            }
            if (!department.equals("All")) {
                sql += " AND department = ?";
                params.add(department);
            }
            if (!startTxt.isEmpty()) {
                sql += " AND join_date >= ?";
                params.add(java.sql.Date.valueOf(startTxt));
            }
            if (!endTxt.isEmpty()) {
                sql += " AND join_date <= ?";
                params.add(java.sql.Date.valueOf(endTxt));
            }
            sql += " ORDER BY full_name";
        }

        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
        return ps.executeQuery();
    }

    private String selected(javax.swing.JComboBox<String> combo) {
        Object value = combo.getSelectedItem();
        return value == null ? "All" : value.toString().trim();
    }

    private String cleanDate(String value) {
        if (value == null) {
            return "";
        }
        value = value.trim();
        if (value.isEmpty() || value.startsWith("YYYY") || value.startsWith("Select")) {
            return "";
        }

        // Validate format before using java.sql.Date.valueOf in the SQL parameters.
        try {
            java.time.LocalDate.parse(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Date must be in yyyy-MM-dd format.");
        }
        return value;
    }

    /**
     * Checks that the selected date range is valid before generating the
     * report.
     */
    private boolean validateDateRange() {
        try {
            String startTxt = cleanDate(jFormattedTextField1.getText());
            String endTxt = cleanDate(jFormattedTextField2.getText());

            if (!startTxt.isEmpty() && !endTxt.isEmpty()) {
                java.time.LocalDate start = java.time.LocalDate.parse(startTxt);
                java.time.LocalDate end = java.time.LocalDate.parse(endTxt);
                if (end.isBefore(start)) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "End date must be after start date.");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, e.getMessage());
            return false;
        }
    }

    /**
     * Loads the selected report into JTable so the user can see the exact
     * filtered result.
     */
    private void loadReportToTable() {
        if (!validateDateRange()) {
            return;
        }
        new Thread(() -> {
            try (java.sql.Connection con = database.DBConnection.getConnection(); java.sql.ResultSet rs = fetchReportData(con)) {

                javax.swing.table.DefaultTableModel model = buildTableModel(rs);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    reportTable.setModel(model);
                    reportModel = model;
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Report generated successfully: " + model.getRowCount() + " records");
                });
                //Logging(IO):
                utils.LoggerUtil.log("reports.txt", "Generated " + selected(jComboBox2) + " using filters from Reports screen");
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(()
                        -> javax.swing.JOptionPane.showMessageDialog(this, "Report error: " + e.getMessage()));
            }
        }).start();
    }

    private javax.swing.table.DefaultTableModel buildTableModel(java.sql.ResultSet rs) throws Exception {
        java.sql.ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        for (int i = 1; i <= columns; i++) {
            model.addColumn(meta.getColumnLabel(i));
        }
        while (rs.next()) {
            Object[] row = new Object[columns];
            for (int i = 1; i <= columns; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }
        return model;
    }

    /**
     * Writes a formatted text report to a .txt file using FileWriter
     * (IOStream). Handles both Employee and Leave report types with appropriate
     * columns.
     */
    private void generateTxtReport() {
        loadReportToTable();
    }

    /**
     * Exports report data to a CSV file using FileWriter (IOStream). Column
     * headers and data vary based on selected report type.
     */
    private void exportCsv() {
        if (reportTable == null || reportTable.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Generate the report first, then export CSV.");
            return;
        }
        try (java.io.FileWriter writer = new java.io.FileWriter("HR_Report.csv")) {
            for (int c = 0; c < reportTable.getColumnCount(); c++) {
                writer.write(reportTable.getColumnName(c));
                if (c < reportTable.getColumnCount() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            for (int r = 0; r < reportTable.getRowCount(); r++) {
                for (int c = 0; c < reportTable.getColumnCount(); c++) {
                    Object value = reportTable.getValueAt(r, c);
                    writer.write(value == null ? "" : value.toString().replace(",", " "));
                    if (c < reportTable.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            writer.write("Total," + reportTable.getRowCount() + "\n");
            //Logging(IO):
            utils.LoggerUtil.log("reports.txt", "CSV exported from JTable: " + reportTable.getRowCount() + " records");
            javax.swing.JOptionPane.showMessageDialog(this, "Exported to HR_Report.csv");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "CSV error: " + e.getMessage());
        }
    }

    /**
     * Exports report data as a formatted PDF-style text file (.txt with PDF
     * layout). Uses FileWriter IOStream. Shows a summary dialog with total
     * record count.
     */
    private void exportPdf() {
        if (reportTable == null || reportTable.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Generate the report first, then export PDF.");
            return;
        }
        try (java.io.FileWriter writer = new java.io.FileWriter("HR_Report_PDF.txt")) {
            writer.write("+=====================================================+\n");
            writer.write("|              HR MANAGEMENT SYSTEM REPORT            |\n");
            writer.write("|  Report Type : " + selected(jComboBox2) + "\n");
            writer.write("|  Generated   : " + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n");
            writer.write("+=====================================================+\n\n");

            for (int c = 0; c < reportTable.getColumnCount(); c++) {
                writer.write(String.format("%-18s", reportTable.getColumnName(c)));
            }
            writer.write("\n" + "-".repeat(100) + "\n");
            for (int r = 0; r < reportTable.getRowCount(); r++) {
                for (int c = 0; c < reportTable.getColumnCount(); c++) {
                    Object value = reportTable.getValueAt(r, c);
                    writer.write(String.format("%-18s", value == null ? "" : value.toString()));
                }
                writer.write("\n");
            }
            writer.write("\nTotal Records: " + reportTable.getRowCount() + "\n");
            //Logging(IO):
            utils.LoggerUtil.log("reports.txt", "PDF-style report exported from JTable");
            javax.swing.JOptionPane.showMessageDialog(this, "Exported to HR_Report_PDF.txt");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "PDF error: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new BackgroundPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Generate Reports");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addContainerGap(574, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 227)));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Report Start Date");

        jFormattedTextField1.setForeground(new java.awt.Color(153, 153, 153));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Report End Date");

        jFormattedTextField2.setForeground(new java.awt.Color(153, 153, 153));
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        jButton1.setBackground(new java.awt.Color(0, 204, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Generate Report");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Report Type");

        jComboBox2.setEditable(true);
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Employee Report", "Leave Report", "Department Report", "Contract Report" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton7.setText("Export CSV");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton8.setText("Export PDF");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("Employee");

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Department");

        jComboBox3.setEditable(true);
        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox4.setEditable(true);
        jComboBox4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "HR", "Finance", "Sales", "IT", "Engineering", "Operations", "Admin" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jComboBox5.setEditable(true);
        jComboBox5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Resigned", "Terminated" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("Statues");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel23)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7))
                    .addComponent(jFormattedTextField2)
                    .addComponent(jFormattedTextField1)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24)
                    .addComponent(jLabel26))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox5)
                    .addComponent(jFormattedTextField1))
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox3)
                    .addComponent(jFormattedTextField2))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Configure parameters to generate custmoized HR reports");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 229, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(340, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(246, 246, 246));
        jPanel1.setPreferredSize(new java.awt.Dimension(180, 450));
        jPanel1.setLayout(new java.awt.GridLayout(10, 1));

        jButton2.setBackground(new java.awt.Color(246, 246, 246));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton2.setText("Dashboard");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setBackground(new java.awt.Color(246, 246, 246));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton3.setText("Employee Search & View");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jButton4.setBackground(new java.awt.Color(246, 246, 246));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton4.setText("Approve Leave");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);

        jButton5.setBackground(new java.awt.Color(246, 246, 246));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton5.setText("Contract Management");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setMinimumSize(new java.awt.Dimension(180, 30));
        jButton5.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setBackground(new java.awt.Color(246, 246, 246));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton6.setText("Reports");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Log out");
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel13);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1180, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        var dash = new Dashboard();
        dash.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        var emp = new Employee_Search_View();
        emp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        var Report = new LeaveRequests();
        Report.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        var contract = new Contract();
        contract.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        var REPOR = new Report();
        REPOR.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        // TODO add your handling code here:
        var login = new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        // Date filters are available for all report types.
        // Employee Report uses the employee join_date column.
        jFormattedTextField1.setEnabled(true);
        jFormattedTextField2.setEnabled(true);
        jLabel11.setEnabled(true);
        jLabel12.setEnabled(true);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        exportCsv();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        generateTxtReport();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        exportPdf();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Report().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
