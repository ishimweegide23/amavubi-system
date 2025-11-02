
package view;

import Service.TransactionService;
import Service.FanService;
import Dao.TransactionDAO;

import Dao.FanDAO;
import Model.Fan;
import Model.Transaction;
import Util.ExportUtil;
import com.sun.prism.paint.Gradient;
import controller.TransactionController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;


public class TransactionsPage extends javax.swing.JFrame {
    private DefaultTableModel transactionTableModel;
    private DefaultTableModel fanTableModel;
    private final TransactionController transactionController;
    private final FanService fanService;
    private Fan currentUser;
    private final FanDAO fanDAO;
    

    

 private void performSearch() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a search term", 
                "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tabbedPane.getSelectedIndex() == 0) {
            transactionTableModel.setRowCount(0);
            List<Transaction> results;
            
            if (currentUser != null && !"admin".equalsIgnoreCase(currentUser.getRole())) {
                results = transactionController.searchTransactionsForUser(currentUser.getFanId(), searchTerm);
            } else {
                results = transactionController.searchTransactionsWithFanName(searchTerm);
            }
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No transactions found matching: " + searchTerm,
                    "No Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
           for (Transaction t : results) {
    transactionTableModel.addRow(new Object[]{
        transactionController.getFormattedTransactionId(t),
        transactionController.getFanName(t.getFanId()), // This now shows correct names
        transactionController.getTransactionType(t),
        transactionController.getTransactionDescription(t), // Detailed description
        t.getAmount(),
        t.getPaymentMethod(),
        t.getStatus(),
        t.getTransactionDate()
    });
}
        } else {
            fanTableModel.setRowCount(0);
            List<Fan> results = fanService.searchFans(searchTerm);
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No fans found matching: " + searchTerm,
                    "No Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            for (Fan f : results) {
                fanTableModel.addRow(new Object[]{
                    f.getFanId(),
                    f.getNationalId(),
                    f.getName(),
                    f.getEmail(),
                    f.getPhone(),
                    f.getTier(),
                    f.getRole()
                });
            }
        }
    }

// Example: Replace loadTransactions() method
private void loadTransactions() {
    transactionTableModel.setRowCount(0);
    try {
        List<Transaction> transactions;
        
        if (currentUser != null && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            transactions = transactionController.getTransactionsByFanId(currentUser.getFanId());
        } else {
            transactions = transactionController.getAllTransactions();
        }

        for (Transaction t : transactions) {
            transactionTableModel.addRow(new Object[]{
                transactionController.getFormattedTransactionId(t),
                transactionController.getFanName(t.getFanId()), // Fixed: Use controller method
                transactionController.getTransactionType(t),
                transactionController.getTransactionDescription(t), // Fixed: Use controller method
                t.getAmount(),
                t.getPaymentMethod(),
                t.getStatus(),
                t.getTransactionDate()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading transactions: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

 private String getTransactionDescription(Transaction t) {
    try {
        if (t.getMatchId() > 0) {
            // Get match name from controller
            String matchName = transactionController.getMatchName(t.getMatchId());
            return matchName != null ? matchName : "Match ID: " + t.getMatchId();
        } else if (t.getItemId() > 0) {
            // Get merchandise name from controller
            String itemName = transactionController.getMerchandiseName(t.getItemId());
            return itemName != null ? itemName : "Item ID: " + t.getItemId();
        } else if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) {
            return "Fundraising Donation";
        }
    } catch (Exception e) {
        e.printStackTrace();
        // Fallback to ID if there's an error
        if (t.getMatchId() > 0) {
            return "Match ID: " + t.getMatchId();
        } else if (t.getItemId() > 0) {
            return "Item ID: " + t.getItemId();
        }
    }
    return "Other Transaction";
}
private void loadFans() {
    fanTableModel.setRowCount(0); // Clear existing data
    try {
        List<Fan> fans = fanService.getAllFans();
        for (Fan fan : fans) {
            fanTableModel.addRow(new Object[]{
                fan.getFanId(),
                fan.getNationalId(),
                fan.getName(),
                fan.getEmail(),
                fan.getPhone(),
                fan.getTier(),
                fan.getRole()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading fans: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
      private void clearSelection() {
        transactionTable.clearSelection();
        comboPaymentMethod.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
    }

    private String getFanName(int fanId) {
        try {
            Fan fan = fanDAO.getFanById(fanId);
            return fan != null ? fan.getName() : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
    
    
    class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color1 = new Color(0, 32, 91);  // Dark blue
        Color color2 = new Color(0, 102, 0);  // Rwanda green
        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

    /**
     * Creates new form TransactionsPage
     */
public TransactionsPage(Fan fan) {
        this.currentUser = fan;
        this.transactionController = new TransactionController();
        this.fanService = new FanService();
        this.fanDAO = new FanDAO();
        
        initComponents();
        initializeTableModels();
        setLocationRelativeTo(null);
        
        loadTransactions();
        
        if (fan != null && !"admin".equalsIgnoreCase(fan.getRole())) {
            tabbedPane.removeTabAt(1);
        } else {
            loadFans();
        }
}
  
   private void initializeTableModels() {
    // Transaction table model
    transactionTableModel = (DefaultTableModel) transactionTable.getModel();
    
    // Fan table model
    fanTableModel = (DefaultTableModel) fanTable.getModel();
      // Style the table headers
    styleTableHeaders();
    
    // Add selection listener for fan table
    fanTable.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && fanTable.getSelectedRow() >= 0) {
            int selectedRow = fanTable.getSelectedRow();
            txtFanName.setText(fanTableModel.getValueAt(selectedRow, 2).toString());
            txtFanEmail.setText(fanTableModel.getValueAt(selectedRow, 3).toString());
            txtFanPhone.setText(fanTableModel.getValueAt(selectedRow, 4).toString());
            comboTier.setSelectedItem(fanTableModel.getValueAt(selectedRow, 5).toString());
        }
    });
}
private static class CustomHeaderRenderer extends DefaultTableCellRenderer {
    private final Font headerFont;
    private final Color bgColor;
    private final Color fgColor;

    public CustomHeaderRenderer(Font font, Color background, Color foreground) {
        this.headerFont = font;
        this.bgColor = background;
        this.fgColor = foreground;
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(headerFont);
        setBackground(bgColor);
        setForeground(fgColor);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return this;
    }
}
private void styleTableHeaders() {
    // Style transaction table
    styleSingleTableHeader(transactionTable, 
        new Font("Segoe UI", Font.BOLD, 14),
        new Color(0, 32, 91), // Dark green
        Color.WHITE,
        35);
    
    // Style fan table
    styleSingleTableHeader(fanTable,
        new Font("Segoe UI", Font.BOLD, 14),
        new Color(0, 32, 91), // Dark blue
        Color.WHITE,
        35);
}

private void styleSingleTableHeader(JTable table, Font font, Color bgColor, Color fgColor, int height) {
    JTableHeader header = table.getTableHeader();
    header.setDefaultRenderer(new CustomHeaderRenderer(font, bgColor, fgColor));
    header.setReorderingAllowed(false);
    
    Dimension headerSize = header.getPreferredSize();
    headerSize.height = height;
    header.setPreferredSize(headerSize);
    
    // Optional: Add hover effect
    header.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            header.setBackground(bgColor.brighter());
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            header.setBackground(bgColor);
        }
    });
    // Add this after styling the headers
transactionTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (!isSelected) {
            String type = table.getModel().getValueAt(row, 2).toString();
            switch (type) {
                case "Match Ticket":
                    c.setBackground(new Color(220, 240, 255)); // Light blue
                    break;
                case "Merchandise":
                    c.setBackground(new Color(220, 255, 220)); // Light green
                    break;
                case "Fundraising":
                    c.setBackground(new Color(255, 240, 220)); // Light orange
                    break;
                default:
                    c.setBackground(table.getBackground());
            }
        }
        return c;
    }
});

// Set column widths
transactionTable.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
transactionTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Fan Name
transactionTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Type
transactionTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Description
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new GradientPanel();
        headPanel = new GradientPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnDisplayAll = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        transactionPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        transactionTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        comboPaymentMethod = new javax.swing.JComboBox<>();
        comboStatus = new javax.swing.JComboBox<>();
        btnClear = new javax.swing.JButton();
        btnUpdateTransaction = new javax.swing.JButton();
        btnDeleteTransaction = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnExportTransactionsPDF = new javax.swing.JButton();
        btnExportTransactionsExcel = new javax.swing.JButton();
        btnExportTransactionsCSV = new javax.swing.JButton();
        fansPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        fanTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        NAME = new javax.swing.JLabel();
        EMAIL = new javax.swing.JLabel();
        Phone = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFanName = new javax.swing.JTextField();
        txtFanPhone = new javax.swing.JTextField();
        txtFanEmail = new javax.swing.JTextField();
        comboTier = new javax.swing.JComboBox<>();
        btnDeleteFan = new javax.swing.JButton();
        btnAddFan = new javax.swing.JButton();
        btnUpdateFan = new javax.swing.JButton();
        btnExportFansPDF = new javax.swing.JButton();
        btnExportFansExcel = new javax.swing.JButton();
        btnExportFansCSV = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Amavubi FanHub - Management System");
        setPreferredSize(new java.awt.Dimension(1000, 702));
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(0, 0, 102));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setPreferredSize(new java.awt.Dimension(1000, 802));

        headPanel.setBackground(new java.awt.Color(0, 0, 51));
        headPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 15, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1.setText("AMAVUBI FANHUB TRANSACTIONS");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(200, 200, 200));
        jLabel2.setText("Official Transaction Management System");

        txtSearch.setColumns(25);
        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearch.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 75, 15)), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(0, 102, 14));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("SEARCH");
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnDisplayAll.setBackground(new java.awt.Color(0, 102, 14));
        btnDisplayAll.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnDisplayAll.setForeground(new java.awt.Color(255, 255, 255));
        btnDisplayAll.setText("SHOW ALL");
        btnDisplayAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDisplayAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headPanelLayout = new javax.swing.GroupLayout(headPanel);
        headPanel.setLayout(headPanelLayout);
        headPanelLayout.setHorizontalGroup(
            headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headPanelLayout.createSequentialGroup()
                .addContainerGap(538, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(btnDisplayAll, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headPanelLayout.createSequentialGroup()
                .addGroup(headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1))
                    .addGroup(headPanelLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        headPanelLayout.setVerticalGroup(
            headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDisplayAll, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.setBackground(new java.awt.Color(255, 255, 255));
        tabbedPane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        transactionPanel.setBackground(new java.awt.Color(255, 255, 255));
        transactionPanel.setPreferredSize(new java.awt.Dimension(876, 253));

        transactionTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Transaction ID", "Fan Name", "Type", "Item/Match", "Amount", "Payment Method", "Status", "Date"
            }
        ));
        transactionTable.setSelectionBackground(new java.awt.Color(0, 51, 0));
        jScrollPane1.setViewportView(transactionTable);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        comboPaymentMethod.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        comboPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mobile money", "Credit card", "Bank Transfer", "Cash", "fundraising" }));
        comboPaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPaymentMethodActionPerformed(evt);
            }
        });

        comboStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pending", "completed", "cancelled" }));
        comboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboStatusActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(0, 102, 14));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("CLEAR");
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnUpdateTransaction.setBackground(new java.awt.Color(0, 102, 14));
        btnUpdateTransaction.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdateTransaction.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateTransaction.setText("UPDATE");
        btnUpdateTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTransactionActionPerformed(evt);
            }
        });

        btnDeleteTransaction.setBackground(new java.awt.Color(153, 153, 0));
        btnDeleteTransaction.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDeleteTransaction.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteTransaction.setText("DELETE");
        btnDeleteTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTransactionActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 204, 0));
        jLabel3.setText("STATUS");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 204, 0));
        jLabel4.setText("PAYMENT METHOD");

        btnExportTransactionsPDF.setBackground(new java.awt.Color(153, 153, 0));
        btnExportTransactionsPDF.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExportTransactionsPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnExportTransactionsPDF.setText("Export Transactions (PDF)");
        btnExportTransactionsPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportTransactionsPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportTransactionsPDFActionPerformed(evt);
            }
        });

        btnExportTransactionsExcel.setBackground(new java.awt.Color(153, 153, 0));
        btnExportTransactionsExcel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExportTransactionsExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnExportTransactionsExcel.setText("Export Transactions (Excel)");
        btnExportTransactionsExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportTransactionsExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportTransactionsExcelActionPerformed(evt);
            }
        });

        btnExportTransactionsCSV.setBackground(new java.awt.Color(153, 153, 0));
        btnExportTransactionsCSV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExportTransactionsCSV.setForeground(new java.awt.Color(255, 255, 255));
        btnExportTransactionsCSV.setText("Export Transactions (CSV)");
        btnExportTransactionsCSV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportTransactionsCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportTransactionsCSVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(btnExportTransactionsPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(btnExportTransactionsExcel)
                .addGap(51, 51, 51)
                .addComponent(btnExportTransactionsCSV)
                .addContainerGap(87, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdateTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(143, 143, 143)
                        .addComponent(btnDeleteTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(comboPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteTransaction)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnClear, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnUpdateTransaction)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportTransactionsCSV)
                    .addComponent(btnExportTransactionsExcel)
                    .addComponent(btnExportTransactionsPDF))
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout transactionPanelLayout = new javax.swing.GroupLayout(transactionPanel);
        transactionPanel.setLayout(transactionPanelLayout);
        transactionPanelLayout.setHorizontalGroup(
            transactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(transactionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        transactionPanelLayout.setVerticalGroup(
            transactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactionPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Transactions", transactionPanel);

        fansPanel.setBackground(new java.awt.Color(255, 255, 255));

        fanTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fanTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "National ID", "Name", "Email", "Phone", "Tier", "Role"
            }
        ));
        jScrollPane2.setViewportView(fanTable);

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));

        NAME.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        NAME.setForeground(new java.awt.Color(255, 204, 0));
        NAME.setText("Name");

        EMAIL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        EMAIL.setForeground(new java.awt.Color(255, 204, 0));
        EMAIL.setText("Email");

        Phone.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Phone.setForeground(new java.awt.Color(255, 204, 0));
        Phone.setText("Phone");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 204, 0));
        jLabel9.setText("Tier");

        txtFanName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFanName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFanNameActionPerformed(evt);
            }
        });

        txtFanPhone.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFanPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFanPhoneActionPerformed(evt);
            }
        });

        txtFanEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFanEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFanEmailActionPerformed(evt);
            }
        });

        comboTier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        comboTier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STANDARD", "PREMIUM", "VIP " }));
        comboTier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTierActionPerformed(evt);
            }
        });

        btnDeleteFan.setBackground(new java.awt.Color(153, 153, 0));
        btnDeleteFan.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnDeleteFan.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteFan.setText("DELETE");
        btnDeleteFan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteFan.setMinimumSize(new java.awt.Dimension(101, 33));
        btnDeleteFan.setPreferredSize(new java.awt.Dimension(101, 33));
        btnDeleteFan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFanActionPerformed(evt);
            }
        });

        btnAddFan.setBackground(new java.awt.Color(0, 102, 14));
        btnAddFan.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnAddFan.setForeground(new java.awt.Color(255, 255, 255));
        btnAddFan.setText("ADD FAN");
        btnAddFan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddFan.setPreferredSize(new java.awt.Dimension(101, 33));
        btnAddFan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFanActionPerformed(evt);
            }
        });

        btnUpdateFan.setBackground(new java.awt.Color(0, 102, 14));
        btnUpdateFan.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnUpdateFan.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateFan.setText("UPDATE");
        btnUpdateFan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateFan.setMinimumSize(new java.awt.Dimension(101, 33));
        btnUpdateFan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateFanActionPerformed(evt);
            }
        });

        btnExportFansPDF.setBackground(new java.awt.Color(0, 102, 14));
        btnExportFansPDF.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnExportFansPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnExportFansPDF.setText("Export Fans (PDF)");
        btnExportFansPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportFansPDF.setPreferredSize(new java.awt.Dimension(101, 33));
        btnExportFansPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportFansPDFActionPerformed(evt);
            }
        });

        btnExportFansExcel.setBackground(new java.awt.Color(0, 102, 14));
        btnExportFansExcel.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnExportFansExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnExportFansExcel.setText("Export Fans (Excel)");
        btnExportFansExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportFansExcel.setMinimumSize(new java.awt.Dimension(101, 33));
        btnExportFansExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportFansExcelActionPerformed(evt);
            }
        });

        btnExportFansCSV.setBackground(new java.awt.Color(0, 102, 14));
        btnExportFansCSV.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnExportFansCSV.setForeground(new java.awt.Color(255, 255, 255));
        btnExportFansCSV.setText("Export Fans (CSV)");
        btnExportFansCSV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportFansCSV.setMinimumSize(new java.awt.Dimension(101, 33));
        btnExportFansCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportFansCSVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(NAME, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EMAIL, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtFanName, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFanEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtFanPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboTier, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(270, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAddFan, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportFansPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdateFan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(btnDeleteFan, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(235, 235, 235))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(btnExportFansExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExportFansCSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(NAME, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFanName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFanPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFanEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EMAIL, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboTier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUpdateFan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddFan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteFan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExportFansExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExportFansCSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExportFansPDF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout fansPanelLayout = new javax.swing.GroupLayout(fansPanel);
        fansPanel.setLayout(fansPanelLayout);
        fansPanelLayout.setHorizontalGroup(
            fansPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(fansPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                .addContainerGap())
        );
        fansPanelLayout.setVerticalGroup(
            fansPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fansPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        tabbedPane.addTab("Fan Management", fansPanel);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(183, 180, 184));
        jLabel5.setText("© 2025 Amavubi FanHub - Official Fan Platform");

        backButton.setBackground(new java.awt.Color(255, 209, 0));
        backButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 209, 0));
        backButton.setText("BACK TO DASHBOARD");
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(headPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 893, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(176, 176, 176)
                                .addComponent(backButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        class GradientPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(0, 3, 91);  // Dark blue
                Color color2 = new Color(0, 90, 0);  // Rwanda green
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        getContentPane().add(mainPanel, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        performSearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnDisplayAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayAllActionPerformed
        // TODO add your handling code here:
                                                
    txtSearch.setText("");
    if (tabbedPane.getSelectedIndex() == 0) {
        loadTransactions();
    } else if (currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole())) {
        loadFans();
    }

    }//GEN-LAST:event_btnDisplayAllActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        
      try {
        // Dispose the current window
        this.dispose();
        
        // Create and show the FanDashboard
        FanDashboard dashboard = new FanDashboard(currentUser);
        dashboard.setVisible(true);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, 
            "Error returning to dashboard: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_backButtonActionPerformed

    private void comboPaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPaymentMethodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPaymentMethodActionPerformed

    private void comboStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboStatusActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        transactionTable.clearSelection();
comboPaymentMethod.setSelectedIndex(0);
comboStatus.setSelectedIndex(0);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUpdateTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTransactionActionPerformed
        // TODO add your handling code here:
                                                       
     int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to update", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String formattedId = transactionTableModel.getValueAt(selectedRow, 0).toString();
            int transactionId = Integer.parseInt(formattedId.split("-")[1]);
            String fanName = transactionTableModel.getValueAt(selectedRow, 1).toString();
            
            if (currentUser != null && !"admin".equalsIgnoreCase(currentUser.getRole())) {
                if (!fanName.equals(currentUser.getName())) {
                    JOptionPane.showMessageDialog(this, 
                        "You can only update your own transactions", 
                        "Access Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            String paymentMethod = (String) comboPaymentMethod.getSelectedItem();
            String status = (String) comboStatus.getSelectedItem();
            
            String message = "Update Transaction Details:\n\n" +
                             "Transaction ID: " + formattedId + "\n" +
                             "Fan Name: " + fanName + "\n" +
                             "Payment Method: " + paymentMethod + "\n" +
                             "Status: " + status + "\n\n" +
                             "Are you sure you want to update this transaction?";
            
            int confirm = JOptionPane.showConfirmDialog(this, message, 
                "Confirm Update", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = transactionController.updateTransaction(
                    transactionId,
                    paymentMethod,
                    status
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Transaction updated successfully");
                    loadTransactions();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update transaction",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating transaction: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnUpdateTransactionActionPerformed

    private void btnDeleteTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTransactionActionPerformed
        // TODO add your handling code here:
                                                       
    int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String formattedId = transactionTableModel.getValueAt(selectedRow, 0).toString();
            int transactionId = Integer.parseInt(formattedId.split("-")[1]);
            String fanName = transactionTableModel.getValueAt(selectedRow, 1).toString();
            String amount = transactionTableModel.getValueAt(selectedRow, 4).toString();
            
            String message = "Delete Transaction Details:\n\n" +
                             "Transaction ID: " + formattedId + "\n" +
                             "Fan Name: " + fanName + "\n" +
                             "Amount: " + amount + "\n\n" +
                             "Are you sure you want to delete this transaction?";
            
            int confirm = JOptionPane.showConfirmDialog(this, message, 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = transactionController.deleteTransaction(transactionId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Transaction deleted successfully");
                    loadTransactions();
                    clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete transaction",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting transaction: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnDeleteTransactionActionPerformed

    private void btnDeleteFanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFanActionPerformed
        // TODO add your handling code here:
                                                    
    int selectedRow = fanTable.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Please select a fan to delete", 
            "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int fanId = (int) fanTableModel.getValueAt(selectedRow, 0);
    String fanName = fanTableModel.getValueAt(selectedRow, 2).toString();
    
    // Confirm deletion
    String message = "Delete Fan:\n\n" +
                     "ID: " + fanId + "\n" +
                     "Name: " + fanName + "\n\n" +
                     "This action cannot be undone. Continue?";
    
    int confirm = JOptionPane.showConfirmDialog(this, message, 
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            boolean success = fanDAO.deleteFan(fanId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Fan deleted successfully");
                loadFans();
                // Clear input fields
                txtFanName.setText("");
                txtFanEmail.setText("");
                txtFanPhone.setText("");
                comboTier.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete fan");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting fan: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 

    }//GEN-LAST:event_btnDeleteFanActionPerformed

    private void btnUpdateFanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateFanActionPerformed
        // TODO add your handling code here:
     int selectedRow = fanTable.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Please select a fan to update", 
            "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        // Get fan ID from selected row
        int fanId = (int) fanTableModel.getValueAt(selectedRow, 0);
        
        // Validate input fields
        if (txtFanName.getText().trim().isEmpty() || 
            txtFanEmail.getText().trim().isEmpty() || 
            txtFanPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create updated fan object
        Fan updatedFan = fanDAO.getFanById(fanId); // Get existing fan first
        if (updatedFan == null) {
            JOptionPane.showMessageDialog(this, "Fan not found in database", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update fields
        updatedFan.setName(txtFanName.getText().trim());
        updatedFan.setEmail(txtFanEmail.getText().trim());
        updatedFan.setPhone(txtFanPhone.getText().trim());
        updatedFan.setTier(comboTier.getSelectedItem().toString());
        
        // Confirm update
        String message = "Update Fan Details:\n\n" +
                         "ID: " + fanId + "\n" +
                         "Name: " + updatedFan.getName() + "\n" +
                         "Email: " + updatedFan.getEmail() + "\n" +
                         "Phone: " + updatedFan.getPhone() + "\n" +
                         "Tier: " + updatedFan.getTier() + "\n\n" +
                         "Confirm changes?";
        
        int confirm = JOptionPane.showConfirmDialog(this, message, 
            "Confirm Update", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = fanDAO.updateFan(updatedFan);
            if (success) {
                JOptionPane.showMessageDialog(this, "Fan updated successfully");
                loadFans(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update fan");
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error updating fan: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnUpdateFanActionPerformed

    private void btnAddFanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFanActionPerformed
        // TODO add your handling code here:
       AddFanDialog dialog = new AddFanDialog(this, true);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    
    // Refresh the fan list after dialog closes
    loadFans();
    
    // Clear selection and input fields
    fanTable.clearSelection();
    txtFanName.setText("");
    txtFanEmail.setText("");
    txtFanPhone.setText("");
    comboTier.setSelectedIndex(0);
    
    
    }//GEN-LAST:event_btnAddFanActionPerformed

    private void txtFanNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFanNameActionPerformed
        // TODO add your handling code here:
         if (txtFanName.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Name cannot be empty", 
            "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_txtFanNameActionPerformed

    private void txtFanEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFanEmailActionPerformed
        // TODO add your handling code here:
        String email = txtFanEmail.getText().trim();
    if (!email.contains("@") || !email.contains(".")) {
        JOptionPane.showMessageDialog(this, "Please enter a valid email address", 
            "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_txtFanEmailActionPerformed

    private void txtFanPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFanPhoneActionPerformed
        // TODO add your handling code here:
         // Basic phone number validation
    String phone = txtFanPhone.getText().trim();
    if (!phone.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Phone number should contain only digits", 
            "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_txtFanPhoneActionPerformed

    private void comboTierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTierActionPerformed
        // TODO add your handling code here:
         String selectedTier = comboTier.getSelectedItem().toString();
    }//GEN-LAST:event_comboTierActionPerformed

    private void btnExportTransactionsPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportTransactionsPDFActionPerformed
        // TODO add your handling code here:
        
        try {
        List<Transaction> transactions = transactionController.getAllTransactions();
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String filePath = ExportUtil.getExportFilePath(
            "Export Transactions to PDF", 
            "Amavubi_Transactions_" + new SimpleDateFormat("yyyyMMdd").format(new Date()),
            ".pdf");
        
        if (filePath != null) {
            boolean success = ExportUtil.exportTransactionsToPDF(transactions, filePath);
            if (success) {
                JOptionPane.showMessageDialog(this, "Transactions exported to PDF successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export transactions", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error exporting transactions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnExportTransactionsPDFActionPerformed

    private void btnExportTransactionsExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportTransactionsExcelActionPerformed
        // TODO add your handling code here:
         try {
        List<Transaction> transactions = transactionController.getAllTransactions();
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String filePath = ExportUtil.getExportFilePath(
            "Export Transactions to Excel", 
            "Amavubi_Transactions_" + new SimpleDateFormat("yyyyMMdd").format(new Date()),
            ".xlsx");
        
        if (filePath != null) {
            boolean success = ExportUtil.exportToExcel(transactions, filePath, "transactions");
            if (success) {
                JOptionPane.showMessageDialog(this, "Transactions exported to Excel successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export transactions", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error exporting transactions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    }//GEN-LAST:event_btnExportTransactionsExcelActionPerformed

    private void btnExportTransactionsCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportTransactionsCSVActionPerformed
        // TODO add your handling code here:
        try {
        List<Transaction> transactions = transactionController.getAllTransactions();
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String filePath = ExportUtil.getExportFilePath(
            "Export Transactions to CSV", 
            "Amavubi_Transactions_" + new SimpleDateFormat("yyyyMMdd").format(new Date()),
            ".csv");
        
        if (filePath != null) {
            boolean success = ExportUtil.exportToCSV(transactions, filePath, "transactions");
            if (success) {
                JOptionPane.showMessageDialog(this, "Transactions exported to CSV successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export transactions", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error exporting transactions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnExportTransactionsCSVActionPerformed

    private void btnExportFansPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportFansPDFActionPerformed
        // TODO add your handling code here:
        
        try {
        List<Fan> fans = fanService.getAllFans();
        if (fans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fans to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String filePath = ExportUtil.getExportFilePath(
            "Export Fans to PDF", 
            "Amavubi_Fans_" + new SimpleDateFormat("yyyyMMdd").format(new Date()),
            ".pdf");
        
        if (filePath != null) {
            boolean success = ExportUtil.exportFansToPDF(fans, filePath);
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    fans.size() + " fans exported to PDF successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export fans", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, 
            "Error exporting fans: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnExportFansPDFActionPerformed

    private void btnExportFansExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportFansExcelActionPerformed
        // TODO add your handling code here:
         try {
        List<Fan> fans = fanService.getAllFans();
        if (fans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fans to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String filePath = ExportUtil.getExportFilePath(
            "Export Fans to Excel", 
            "Amavubi_Fans_" + new SimpleDateFormat("yyyyMMdd").format(new Date()),
            ".xlsx");
        
        if (filePath != null) {
            boolean success = ExportUtil.exportToExcel(fans, filePath, "fans");
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    fans.size() + " fans exported to Excel successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export fans", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, 
            "Error exporting fans: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnExportFansExcelActionPerformed

    private void btnExportFansCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportFansCSVActionPerformed
        // TODO add your handling code here:
        try {
        List<Fan> fans = fanService.getAllFans();
        if (fans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fans to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String filePath = ExportUtil.getExportFilePath(
            "Export Fans to CSV", 
            "Amavubi_Fans_" + new SimpleDateFormat("yyyyMMdd").format(new Date()),
            ".csv");
        
        if (filePath != null) {
            boolean success = ExportUtil.exportToCSV(fans, filePath, "fans");
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    fans.size() + " fans exported to CSV successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export fans", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, 
            "Error exporting fans: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnExportFansCSVActionPerformed

    
    private String getFormattedTransactionId(Transaction t) {
    if (t.getMatchId() != 0) { // Assuming 0 means NULL in your model
        return "TKT-" + t.getTransactionId();
    } else if (t.getItemId() != 0) {
        return "MER-" + t.getTransactionId();
    } else if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) {
        return "FUND-" + t.getTransactionId();
    } else {
        return "TRN-" + t.getTransactionId();
    }
}

private String getTransactionType(Transaction t) {
    if (t.getMatchId() != 0) {
        return "Match Ticket";
    } else if (t.getItemId() != 0) {
        return "Merchandise";
    } else if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) {
        return "Fundraising";
    } else {
        return "Other";
    }
}


private void exportTransactions(String format) {
    try {
        List<Transaction> transactions = transactionController.getAllTransactions();
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Transaction Export");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        String extension = format.equals("pdf") ? ".pdf" : format.equals("excel") ? ".xlsx" : ".csv";
        fileChooser.setSelectedFile(new File("Amavubi_Transactions_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + extension));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            
            switch (format) {
                case "pdf":
                    ExportUtil.exportTransactionsToPDF(transactions, filePath);
                    break;
                case "excel":
                    ExportUtil.exportToExcel(transactions, filePath, "transactions");
                    break;
                case "csv":
                    ExportUtil.exportToCSV(transactions, filePath, "transactions");
                    break;
            }
            
            JOptionPane.showMessageDialog(this, "Transactions exported successfully to " + filePath, 
                "Export Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error exporting transactions: " + ex.getMessage(), 
            "Export Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

private void exportFans(String format) {
    try {
        List<Fan> fans = fanService.getAllFans();
        if (fans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fans to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Fans Export");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        String extension = format.equals("pdf") ? ".pdf" : format.equals("excel") ? ".xlsx" : ".csv";
        fileChooser.setSelectedFile(new File("Amavubi_Fans_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + extension));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            
            switch (format) {
                case "pdf":
                    ExportUtil.exportFansToPDF(fans, filePath);
                    break;
                case "excel":
                    ExportUtil.exportToExcel(fans, filePath, "fans");
                    break;
                case "csv":
                    ExportUtil.exportToCSV(fans, filePath, "fans");
                    break;
            }
            
            JOptionPane.showMessageDialog(this, "Fans exported successfully to " + filePath, 
                "Export Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error exporting fans: " + ex.getMessage(), 
            "Export Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}


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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransactionsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              //  new TransactionsPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EMAIL;
    private javax.swing.JLabel NAME;
    private javax.swing.JLabel Phone;
    private javax.swing.JButton backButton;
    private javax.swing.JButton btnAddFan;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDeleteFan;
    private javax.swing.JButton btnDeleteTransaction;
    private javax.swing.JButton btnDisplayAll;
    private javax.swing.JButton btnExportFansCSV;
    private javax.swing.JButton btnExportFansExcel;
    private javax.swing.JButton btnExportFansPDF;
    private javax.swing.JButton btnExportTransactionsCSV;
    private javax.swing.JButton btnExportTransactionsExcel;
    private javax.swing.JButton btnExportTransactionsPDF;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateFan;
    private javax.swing.JButton btnUpdateTransaction;
    private javax.swing.JComboBox<String> comboPaymentMethod;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JComboBox<String> comboTier;
    private javax.swing.JTable fanTable;
    private javax.swing.JPanel fansPanel;
    private javax.swing.JPanel headPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel transactionPanel;
    private javax.swing.JTable transactionTable;
    private javax.swing.JTextField txtFanEmail;
    private javax.swing.JTextField txtFanName;
    private javax.swing.JTextField txtFanPhone;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
