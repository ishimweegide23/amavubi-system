package view;

import Dao.MerchandiseDAO;
import Dao.TransactionDAO;
import Model.Fan;
import Model.Merchandise;
import controller.MerchandiseController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;

public class MerchandisePage extends JFrame {
    private JList<Merchandise> merchandiseList;
    private DefaultListModel<Merchandise> listModel;
    private Fan fan;
    private JSpinner quantitySpinner;
    private JComboBox<String> sizeComboBox;
    private MerchandiseController merchandiseController;

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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

    public MerchandisePage(Fan fan) {
        this.fan = fan;
        initComponents();
    }

    private void initComponents() {
        setTitle("FanHub - Merchandise Store");
        setSize(800, 650); // Increased size for better card display
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Header panel
        JLabel titleLabel = new JLabel("AMAVUBI MERCHANDISE STORE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 209, 0)); // Rwanda yellow
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Merchandise list panel
        listModel = new DefaultListModel<>();
        merchandiseList = new JList<>(listModel);
        merchandiseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        merchandiseList.setCellRenderer(new MerchandiseCardRenderer());
        merchandiseList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        merchandiseList.setVisibleRowCount(-1);
        merchandiseList.setFixedCellWidth(350);
        merchandiseList.setFixedCellHeight(150);

        JScrollPane scrollPane = new JScrollPane(merchandiseList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        JPanel listPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.CENTER);

        // Bottom panel with controls
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Selection controls panel
        JPanel selectionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        selectionPanel.setOpaque(false);

        // Quantity panel
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.setOpaque(false);
        
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        quantityLabel.setForeground(Color.WHITE);
        quantityPanel.add(quantityLabel);

        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        quantityPanel.add(quantitySpinner);

        // Size panel
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.setOpaque(false);
        
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sizeLabel.setForeground(Color.WHITE);
        sizePanel.add(sizeLabel);

        sizeComboBox = new JComboBox<>();
        sizeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sizeComboBox.setEnabled(false); // Disabled until item is selected
        sizePanel.add(sizeComboBox);

        selectionPanel.add(quantityPanel);
        selectionPanel.add(sizePanel);
        bottomPanel.add(selectionPanel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("BACK TO DASHBOARD");
        styleButton(backButton, new Dimension(180, 35));
        backButton.addActionListener(e -> {
            new FanDashboard(fan).setVisible(true);
            dispose();
        });

        JButton buyButton = new JButton("PURCHASE");
        styleButton(buyButton, new Dimension(120, 35));
        buyButton.addActionListener(e -> purchaseItem());

        buttonPanel.add(backButton);
        buttonPanel.add(buyButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add selection listener for size combo box updates
        merchandiseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && merchandiseList.getSelectedValue() != null) {
                updateSizeComboBox(merchandiseList.getSelectedValue());
            }
        });

        loadMerchandiseData();
    }

    private void styleButton(JButton button, Dimension size) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 75, 14)); // Dark green
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 209, 0), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(size);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 55, 10));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 75, 14));
            }
        });
    }

public void loadMerchandiseData() {
        merchandiseController = new MerchandiseController();
        merchandiseController.setView(this); // Connect the controller to this view
        
        List<Merchandise> merchandiseListData = merchandiseController.getAllMerchandise();
        
        listModel.clear();
        for (Merchandise merchandise : merchandiseListData) {
            listModel.addElement(merchandise);
        }
    }

      private void updateSizeComboBox(Merchandise selectedItem) {
        sizeComboBox.removeAllItems();
        sizeComboBox.setEnabled(true);
        
        List<String> sizes = merchandiseController.getAvailableSizes(selectedItem.getItemId());
        for (String size : sizes) {
            sizeComboBox.addItem(size);
        }
        
        if (sizes.size() == 1 && sizes.get(0).equals("One Size")) {
            sizeComboBox.setEnabled(false);
        }
    }

     private void purchaseItem() {
        Merchandise selectedItem = merchandiseList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an item to purchase", 
                "Selection Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int quantity = (int) quantitySpinner.getValue();
            String size = (String) sizeComboBox.getSelectedItem();
            
            boolean success = merchandiseController.purchaseMerchandise(
                fan, selectedItem, quantity, size);
                
            if (success) {
                String sizeInfo = size.equals("One Size") ? "" : "\nSize: " + size;
                String message = String.format(
                    "🎉 Purchase Confirmation for %s\n\n" +
                    "Item: %d × %s%s\n" +
                    "Total: RWF %,.0f\n\n" +
                    "Thank you for supporting Amavubi!",
                    fan.getName(), quantity, selectedItem.getName(), sizeInfo, 
                    selectedItem.getPrice() * quantity);
                
                JOptionPane.showMessageDialog(this,
                    message,
                    "Purchase Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadMerchandiseData(); // Refresh the list
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "An error occurred during purchase: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

   private class MerchandiseCardRenderer implements ListCellRenderer<Merchandise> {
    private final Color selectionColor = new Color(0, 102, 0, 50);
    private final Color cardColor = new Color(255, 255, 255, 220);
    private final int imageSize = 100;

    @Override
    public Component getListCellRendererComponent(JList<? extends Merchandise> list, Merchandise item, 
                                                int index, boolean isSelected, boolean cellHasFocus) {
        
        // Main card panel
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Product image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Try loading image with multiple extensions
        ImageIcon productIcon = loadProductIcon(item.getItemId());
        imageLabel.setIcon(productIcon);

        // Text content
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        // Category label with different color
        JLabel categoryLabel = new JLabel(item.getCategory());
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
        categoryLabel.setForeground(new Color(0, 75, 150)); // Blue color for category
        
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));

        JLabel priceLabel = new JLabel(String.format("RWF %,.0f", item.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 102, 0));

        JLabel stockLabel = new JLabel("Stock: " + item.getStockQuantity());
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(100, 100, 100));

        // Add components to text panel with proper spacing
        textPanel.add(categoryLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3))); // Small space after category
        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(priceLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(stockLabel);

        // Add components to card
        card.add(imageLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        // Selection styling
        if (isSelected) {
            card.setBackground(selectionColor);
            card.setOpaque(true);
        }

        return card;
    }

    private ImageIcon loadProductIcon(int itemId) {
        // Try different image extensions
        String[] extensions = {".png", ".jpg", ".jpeg"};
        
        for (String ext : extensions) {
            try {
                URL imageUrl = getClass().getResource("/images/merchandise/" + itemId + ext);
                if (imageUrl != null) {
                    Image original = ImageIO.read(imageUrl);
                    Image scaled = original.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            } catch (IOException e) {
                // Continue to next extension
            }
        }
        
        // If no image found, return placeholder
        return createPlaceholderIcon(itemId);
    }

    private ImageIcon createPlaceholderIcon(int itemId) {
        BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw background
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, imageSize, imageSize);
        
        // Draw border
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, imageSize-1, imageSize-1);
        
        // Draw text
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String text = "Item " + itemId;
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, (imageSize - textWidth)/2, imageSize/2);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
}
}