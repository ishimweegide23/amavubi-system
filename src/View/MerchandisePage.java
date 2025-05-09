/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Dao.MerchandiseDAO;
import Dao.TransactionDAO;
import Model.Fan;
import Model.Merchandise;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Hey
 */
public class MerchandisePage extends javax.swing.JFrame {
   
     private Fan fan;
    private DefaultListModel<String> listModel;

   
    
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
     * Creates new form MerchandisePage
     */
   public MerchandisePage(Fan fan) {
        this.fan = fan;
        initComponents();
        setupListModel();
        addListSelectionListener();
        setLocationRelativeTo(null);
    }

    private void setupListModel() {
        listModel = new DefaultListModel<>();
        merchandiseList.setModel(listModel);
        merchandiseList.setCellRenderer(new MerchandiseListRenderer());
        loadMerchandiseData();
    }
   private void addListSelectionListener() {
        merchandiseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && merchandiseList.getSelectedValue() != null) {
                updateSizeComboBox(merchandiseList.getSelectedValue());
            }
        });
    }
  private void loadMerchandiseData() {
        MerchandiseDAO merchandiseDAO = new MerchandiseDAO();
        List<Merchandise> merchandiseListData = merchandiseDAO.getMerchandiseList();

        listModel.clear();
        for (Merchandise merchandise : merchandiseListData) {
            String text = String.format("%s - %s - RWF %,.0f (Stock: %d)", 
                merchandise.getName(),
                merchandise.getCategory(), 
                merchandise.getPrice(), 
                merchandise.getStockQuantity());
            
            if (merchandise.getSize() != null && !merchandise.getSize().equalsIgnoreCase("NULL")) {
                text += " [Sizes: " + merchandise.getSize() + "]";
            }
            listModel.addElement(text);
        }
    }


private void updateSizeComboBox(String selectedItem) {
        sizeComboBox.removeAllItems();
        sizeComboBox.setEnabled(true);
        
        int sizesStart = selectedItem.indexOf("[Sizes: ");
        if (sizesStart != -1) {
            int sizesEnd = selectedItem.indexOf("]", sizesStart);
            String sizesStr = selectedItem.substring(sizesStart + 8, sizesEnd);
            String[] sizes = sizesStr.split("\\s*,\\s*");
            
            for (String size : sizes) {
                if (!size.trim().isEmpty()) {
                    sizeComboBox.addItem(size.trim());
                }
            }
        } else {
            sizeComboBox.addItem("One Size");
            sizeComboBox.setEnabled(false);
        }
    }
  private class MerchandiseListRenderer extends DefaultListCellRenderer {
    private final Color selectionColor = new Color(0, 102, 0); // Dark green
    private final Color evenRowColor = new Color(240, 240, 240); // Light gray
    private final Color oddRowColor = Color.WHITE;
    private final Color borderColor = new Color(200, 200, 200); // Light gray border
    
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, 
            boolean isSelected, boolean cellHasFocus) {
        
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        // Set fonts and alignment
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Alternate row colors
        if (!isSelected) {
            setBackground(index % 2 == 0 ? evenRowColor : oddRowColor);
        }
        
        // Selection styling
        if (isSelected) {
            setBackground(selectionColor);
            setForeground(Color.WHITE);
        } else {
            setForeground(Color.BLACK);
        }
        
        return this;
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

        mainpanel = new GradientPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        merchandiseList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        quantitySpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        sizeComboBox = new javax.swing.JComboBox<>();
        buyButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FanHub - Merchandise Store");
        setResizable(false);
        setSize(new java.awt.Dimension(650, 550));

        mainpanel.setBackground(new java.awt.Color(255, 204, 204));
        mainpanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainpanel.setPreferredSize(new java.awt.Dimension(650, 550));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 208, 0));
        jLabel1.setText("AMAVUBI MERCHANDISE STORE");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        merchandiseList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.darkGray, java.awt.Color.black, java.awt.Color.black));
        merchandiseList.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        merchandiseList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        merchandiseList.setPreferredSize(new java.awt.Dimension(600, 450));
        jScrollPane1.setViewportView(merchandiseList);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Quantity");

        quantitySpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Size");

        sizeComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sizeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "s", "Item 2", "Item 3", "Item 4" }));
        sizeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeComboBoxActionPerformed(evt);
            }
        });

        buyButton.setBackground(new java.awt.Color(0, 102, 0));
        buyButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        buyButton.setForeground(new java.awt.Color(255, 255, 255));
        buyButton.setText("PURCHASE");
        buyButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyButtonActionPerformed(evt);
            }
        });

        backButton.setBackground(new java.awt.Color(255, 255, 0));
        backButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 0));
        backButton.setText("BACK TO DASHBOARD");
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 255));
        jLabel4.setText("© 2025 Amavubi FanHub - Official Fan Platform");

        javax.swing.GroupLayout mainpanelLayout = new javax.swing.GroupLayout(mainpanel);
        mainpanel.setLayout(mainpanelLayout);
        mainpanelLayout.setHorizontalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel1)
                .addContainerGap(158, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(backButton)
                                .addGap(19, 19, 19))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        mainpanelLayout.setVerticalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buyButton)
                            .addComponent(backButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(sizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(mainpanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyButtonActionPerformed
        // TODO add your handling code here:
        // Validate item selection
    String selectedItem = merchandiseList.getSelectedValue();
    if (selectedItem == null) {
        JOptionPane.showMessageDialog(this, 
            "Please select an item to purchase", 
            "Selection Required", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validate quantity
    int quantity = (int) quantitySpinner.getValue();
    if (quantity < 1) {
        JOptionPane.showMessageDialog(this, 
            "Quantity must be at least 1", 
            "Invalid Quantity", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validate size if the product has sizes
    String size = (String) sizeComboBox.getSelectedItem();
    if (selectedItem.contains("[Sizes:") && (size == null || size.trim().isEmpty())) {
        JOptionPane.showMessageDialog(this, 
            "Please select a size for this product", 
            "Size Required", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        String[] itemParts = selectedItem.split(" - ");
        String itemName = itemParts[0];
        double itemPrice = Double.parseDouble(itemParts[2].split(" ")[1].replace(",", ""));
        double totalPrice = itemPrice * quantity;

        MerchandiseDAO merchandiseDAO = new MerchandiseDAO();
        Merchandise merchandise = merchandiseDAO.getMerchandiseByName(itemName);

        if (merchandise != null && merchandise.getStockQuantity() >= quantity) {
            boolean success = new TransactionDAO().insertMerchandisePurchase(
                fan.getFanId(), merchandise.getItemId(), quantity, totalPrice, size);
            
            if (success) {
                merchandiseDAO.updateStockQuantity(merchandise.getItemId(), 
                    merchandise.getStockQuantity() - quantity);
                
                String sizeInfo = size.equals("One Size") ? "" : "\nSize: " + size;
                String message = String.format(
                    "🎉 Purchase Confirmation for %s\n\n" +
                    "Item: %d × %s%s\n" +
                    "Total: RWF %,.0f\n\n" +
                    "Thank you for supporting Amavubi!",
                    fan.getName(), quantity, itemName, sizeInfo, totalPrice);
                
                JOptionPane.showMessageDialog(this,
                    message,
                    "Purchase Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadMerchandiseData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Purchase failed. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Not enough stock available", 
                "Insufficient Stock", 
                JOptionPane.WARNING_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "An error occurred during purchase: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_buyButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new FanDashboard(fan).setVisible(true);
    dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void sizeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sizeComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton buyButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JList<String> merchandiseList;
    private javax.swing.JSpinner quantitySpinner;
    private javax.swing.JComboBox<String> sizeComboBox;
    // End of variables declaration//GEN-END:variables
}
