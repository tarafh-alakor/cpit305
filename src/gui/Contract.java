/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import javax.swing.JOptionPane;

/**
 * Contract - Manages employee contracts in the HR system. Allows adding new
 * contracts and viewing all contracts with auto-calculated status. Contract
 * status is computed in real-time: Active, Expiring Soon (< 90 days), or
 * Expired. Demonstrates: Database (CRUD), Networking in line 641(HRClient
 * notification), IOStream in line 644(LoggerUtil).
 */
public class Contract extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Contract.class.getName());
    private String empId;

    /**
     * Creates new form Contract
     */
    public Contract(String empId) {
        initComponents();
        
        // Apply visual styling only.
        getContentPane().setBackground(new java.awt.Color(250, 255, 252));
        VisualStyle.apply(getContentPane());
this.empId = empId;
    }

    public Contract() {
        initComponents();
        loadEmployees();
        setupPlaceholders();
        setupContractTable();
        loadContractsTable();
    }

    /**
     * Applies the same calendar DateChooser style used in the Reports screen.
     */
    private void setupPlaceholders() {
        setupDateChooserField(jTextField1, "Select start date");
        setupDateChooserField(jTextField2, "Select end date");
    }

    /**
     * Makes a text field behave like a small calendar DateChooser.
     */
    private void setupDateChooserField(javax.swing.JTextField field, String placeholder) {
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
    private void showDateChooser(javax.swing.JTextField target, String placeholder) {
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
     * Returns empty when the date field still contains its placeholder.
     */
    private String cleanDateField(javax.swing.JTextField field) {
        String value = field.getText() == null ? "" : field.getText().trim();
        if (value.isEmpty() || value.startsWith("Select") || value.startsWith("YYYY")) {
            return "";
        }
        return value;
    }

    /**
     * Sets up column widths and the coloured Status cell renderer
     */
    private void setupContractTable() {
        jTable1.setRowHeight(32);
        jTable1.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        jTable1.getTableHeader().setBackground(new java.awt.Color(0, 204, 153));
        jTable1.getTableHeader().setForeground(java.awt.Color.WHITE);

        // Column widths
        int[] widths = {90, 140, 110, 110, 110, 110};
        for (int i = 0; i < widths.length; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Colour-coded Status column (index 5)
        jTable1.getColumnModel().getColumn(5).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(CENTER);
                setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
                String s = val == null ? "" : val.toString();
                switch (s) {
                    case "Active":
                        setBackground(new java.awt.Color(200, 255, 210));
                        setForeground(new java.awt.Color(0, 130, 50));
                        break;
                    case "Expiring Soon":
                        setBackground(new java.awt.Color(255, 243, 180));
                        setForeground(new java.awt.Color(180, 110, 0));
                        break;
                    case "Expired":
                        setBackground(new java.awt.Color(255, 210, 210));
                        setForeground(new java.awt.Color(180, 0, 0));
                        break;
                    default:
                        setBackground(java.awt.Color.WHITE);
                        setForeground(java.awt.Color.BLACK);
                }
                if (sel) {
                    setBackground(getBackground().darker());
                }
                return this;
            }
        }
        );
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
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
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
        jLabel1.setText("Contract Management");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addContainerGap(572, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 227)));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Update contract details and upload relevant documents");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Manage Employee Contract");

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Employee Name");

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Contract Start Date");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Contract End Date");

        jButton1.setBackground(new java.awt.Color(0, 204, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Save/Renew Contract");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton7.setText("Cancel");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Contract Type");

        jComboBox2.setEditable(true);
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Full Time", "Part Time", "Temporary" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);

        jTextField2.setPreferredSize(new java.awt.Dimension(64, 25));
        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 330, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(25, 25, 25)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 227)));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Recent Contracts Overview");

        jTable1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Employee ID", "Name", "Contract Type", "Start Date", "End Date", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] editable = new boolean[]{false,false,false,false,false,false};
            public Class getColumnClass(int i) { return types[i]; }
            public boolean isCellEditable(int r, int c) { return editable[c]; }
        });
        jTable1.setToolTipText("");
        jTable1.setAlignmentX(2.0F);
        jTable1.setAlignmentY(1.0F);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setRowHeight(30);
        jTable1.setRowMargin(4);
        jTable1.setSelectionBackground(new java.awt.Color(102, 204, 255));
        jTable1.setShowGrid(true);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        // Fix status column width
        try {
            jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(90);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(140);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(170);
        } catch (Exception e) {
        }

        try { jTable1.getColumnModel().getColumn(jTable1.getColumnCount()-1).setPreferredWidth(150); } catch (Exception e) { }


        // Keep full text visible with horizontal scrolling
        try {
            jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            jTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(170);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(170);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(130);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(220);

            jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        } catch (Exception e) {
        }


        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(342, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(187, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(246, 246, 246));
        jPanel1.setPreferredSize(new java.awt.Dimension(180, 450));
        jPanel1.setLayout(new java.awt.GridLayout(10, 1));

        jButton2.setBackground(new java.awt.Color(246, 246, 246));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton2.setText("Dashboard");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel1.add(jButton2);

        jButton3.setBackground(new java.awt.Color(246, 246, 246));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton3.setText("Employee Search & View");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jPanel1.add(jButton3);

        jButton4.setBackground(new java.awt.Color(246, 246, 246));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton4.setText("Approve Leave");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton4.addActionListener(this::jButton4ActionPerformed);
        jPanel1.add(jButton4);

        jButton5.setBackground(new java.awt.Color(246, 246, 246));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton5.setText("Contract Management");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setMinimumSize(new java.awt.Dimension(180, 30));
        jButton5.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton5.addActionListener(this::jButton5ActionPerformed);
        jPanel1.add(jButton5);

        jButton6.setBackground(new java.awt.Color(246, 246, 246));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton6.setText("Reports");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton6.addActionListener(this::jButton6ActionPerformed);
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

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents
  /**
     * Loads all employee IDs from the database into the employee selector
     * ComboBox.
     */
    private void loadEmployees() {
        try {
            java.sql.Connection con = database.DBConnection.getConnection();
            String sql = "SELECT emp_id FROM employees";
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jComboBox1.addItem(rs.getString("emp_id"));
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * Calculates the contract status from the end date. Expired: end date
     * passed. Expiring Soon: 90 days or less remaining. Active: more than 90
     * days remaining.
     */
    private String getStatus(java.sql.Date endDate) {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate end = endDate.toLocalDate();

        if (end.isBefore(today)) {
            return "Expired";
        }

        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, end);

        if (daysLeft <= 90) {
            return "Expiring Soon";
        }

        return "Active";
    }

    /**
     * Loads all contracts (joined with employee names) into the contracts
     * table. Computes and displays the status for each contract dynamically.
     */
    private void loadContractsTable() {
        try {
            java.sql.Connection con = database.DBConnection.getConnection();

            String sql = "SELECT c.id, c.emp_name AS emp_id, e.full_name, c.contract_type, c.start_date, c.end_date, c.status "
                    + "FROM contracts c "
                    + "LEFT JOIN employees e ON c.emp_name = e.emp_id "
                    + "ORDER BY c.id DESC";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);

            java.sql.ResultSet rs = ps.executeQuery();

            javax.swing.table.DefaultTableModel model
                    = (javax.swing.table.DefaultTableModel) jTable1.getModel();

            model.setRowCount(0);

            while (rs.next()) {
                String id = rs.getString("emp_id");
                String name = rs.getString("full_name");
                String type = rs.getString("contract_type");
                java.sql.Date startDate = rs.getDate("start_date");
                java.sql.Date endDate = rs.getDate("end_date");
                String status = getStatus(endDate);

                model.addRow(new Object[]{id, name, type, startDate, endDate, status});
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String empID = jComboBox1.getSelectedItem().toString().trim();
            String contractType = jComboBox2.getSelectedItem().toString();
            String startText = cleanDateField(jTextField1);
            String endText = cleanDateField(jTextField2);

            if (startText.isEmpty() || endText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both Start Date and End Date\nFormat: YYYY-MM-DD");
                return;
            }

            java.sql.Date startDate = java.sql.Date.valueOf(startText);
            java.sql.Date endDate = java.sql.Date.valueOf(endText);
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date");
                return;
            }

            jButton1.setEnabled(false);

            // Multi-threading: save contract in a background thread so the GUI does not freeze.
            new Thread(() -> {
                try {
                    java.sql.Connection con = database.DBConnection.getConnection();
                    String sql = "INSERT INTO contracts (emp_name, contract_type, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
                    java.sql.PreparedStatement ps = con.prepareStatement(sql);

                    ps.setString(1, empID);
                    ps.setString(2, contractType);
                    ps.setDate(3, startDate);
                    ps.setDate(4, endDate);
                    ps.setString(5, getStatus(endDate));
                    ps.executeUpdate();

                    // Networking: notify HR server about the contract update.
                    network.HRClient.sendNotification("Contract updated for employee: " + empID);

                    // IOStream: write the action to a log file.
                    utils.LoggerUtil.log("contract.txt", "Contract updated for: " + empID);

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        loadContractsTable();
                        JOptionPane.showMessageDialog(this, "Contract saved successfully");
                        jTextField1.setText("Select start date");
                        jTextField1.setForeground(new java.awt.Color(120, 120, 120));
                        jTextField2.setText("Select end date");
                        jTextField2.setForeground(new java.awt.Color(120, 120, 120));
                        jComboBox1.setSelectedIndex(0);
                        jButton1.setEnabled(true);
                    });

                } catch (Exception e) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
                        jButton1.setEnabled(true);
                    });
                }
            }).start();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date. Use format YYYY-MM-DD (example: 2025-05-01)");
            jButton1.setEnabled(true);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        var dash = new Dashboard();
        dash.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

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

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        if (jComboBox1.getSelectedItem() != null) {
            String empId = jComboBox1.getSelectedItem().toString();
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Contract().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
