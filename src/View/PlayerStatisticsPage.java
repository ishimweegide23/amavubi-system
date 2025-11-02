package view;

import Dao.PlayerDAO;
import Model.Fan;
import Model.Player;

import controller.PlayerController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class PlayerStatisticsPage extends JFrame {
    private PlayerController playerController; 
    private JTable table;
    private DefaultTableModel tableModel;
    private Fan fan;
    private JTextField searchField;
    private Map<Integer, ImageIcon> playerImages = new HashMap<>();

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

    // In the constructor, initialize it
public PlayerStatisticsPage(Fan fan) {
    this.fan = fan;
    this.playerController = new PlayerController();  // Add this line
    initComponents();
    loadPlayerData();
}

    private void initComponents() {
        setTitle("FanHub - Player Statistics");
        setSize(1200, 750); // Increased size to accommodate images
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("AMAVUBI PLAYER STATISTICS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 209, 0)); // Rwanda yellow
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Official Player Performance Data");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 75, 14), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        JButton searchButton = new JButton("SEARCH PLAYER");
        styleButton(searchButton, new Dimension(180, 40));
        searchButton.addActionListener(e -> searchPlayer());
        
        JButton showAllButton = new JButton("SHOW ALL");
        styleButton(showAllButton, new Dimension(140, 40));
        showAllButton.addActionListener(e -> loadPlayerData());
        
        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        searchPanel.add(searchButton);
        searchPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        searchPanel.add(showAllButton);

        headerPanel.add(searchPanel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table setup with image column
        String[] columnNames = {"ID", "Photo", "Player Name", "Position", "Jersey No.", "Date of Birth", 
                              "Nationality", "Active", "Goals", "Assists", "Matches"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? Boolean.class : Object.class;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);

        // Set custom renderer for the image column
        table.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());
        table.getColumnModel().getColumn(1).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        JPanel tablePanel = new JPanel(new BorderLayout()) {
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
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Back button
        JButton backButton = new JButton("BACK TO DASHBOARD");
        styleButton(backButton, new Dimension(220, 45));
        backButton.addActionListener(e -> {
            new FanDashboard(fan).setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 Amavubi FanHub - Official Fan Platform");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(180, 180, 180));
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(footerLabel, BorderLayout.SOUTH);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    // Custom renderer for displaying player images
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);
            
            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
            } else {
                // Default player silhouette if no image is available
                label.setIcon(createDefaultPlayerIcon());
            }
            
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
            } else {
                label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
            }
            
            return label;
        }
    }

    // Create a default player silhouette icon
    private ImageIcon createDefaultPlayerIcon() {
        BufferedImage image = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw silhouette
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(5, 5, 50, 50);
        
        // Draw face
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(15, 15, 30, 30);
        
        g2d.dispose();
        return new ImageIcon(image);
    }

    // Method to load player image (placeholder implementation)
    private ImageIcon loadPlayerImage(int playerId, String playerName) {
        // In a real application, you would load these from a database or file system
        // Here we'll use placeholder images from a dummy URL or local resources
        
        try {
            // Try to load from local resources first
            URL imageUrl = getClass().getResource("/images/players/" + playerId + ".png");
            if (imageUrl != null) {
                Image image = ImageIO.read(imageUrl);
                return new ImageIcon(image.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            }
            
            // If no local image, try to generate a placeholder based on player name initials
            return generateInitialsIcon(playerName);
            
        } catch (IOException e) {
            return createDefaultPlayerIcon();
        }
    }

    // Generate an icon with player initials
    private ImageIcon generateInitialsIcon(String playerName) {
        BufferedImage image = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw background circle
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(0, 75, 14)); // Rwanda green
        g2d.fillOval(5, 5, 50, 50);
        
        // Get initials
        String[] names = playerName.split(" ");
        String initials = "";
        if (names.length > 0) {
            initials += names[0].substring(0, 1);
            if (names.length > 1) {
                initials += names[names.length - 1].substring(0, 1);
            }
        }
        
        // Draw initials
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (60 - fm.stringWidth(initials)) / 2;
        int y = ((60 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initials, x, y);
        
        g2d.dispose();
        return new ImageIcon(image);
    }

private void searchPlayer() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a player name to search", 
                "Search Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // List of retired players (could also check database is_active flag)
        String[] retiredPlayers = {
            "Haruna Niyonzima",
            "Olivier Karekezi",
            "Jimmy Mulisa",
            "Jean Lomami"
        };

        // Check if searched player is retired
       try {
            List<Player> players = playerController.searchPlayers(searchTerm);
            
            tableModel.setRowCount(0);
            playerImages.clear();

            if (players != null && !players.isEmpty()) {
                for (Player player : players) {
                    ImageIcon playerImage = loadPlayerImage(player.getPlayerId(), player.getName());
                    playerImages.put(player.getPlayerId(), playerImage);
                    
                    Object[] rowData = {
                        player.getPlayerId(),
                        playerImage,
                        player.getName(),
                        player.getPosition(),
                        player.getJerseyNumber(),
                        player.getDateOfBirth(),
                        player.getNationality(),
                        player.isActive(),
                        player.getGoals(),
                        player.getAssists(),
                        player.getMatchesPlayed()
                    };
                    tableModel.addRow(rowData);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No current players found matching: " + searchTerm, 
                    "Player Not Found", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadPlayerData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching player data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(60); // Increased row height for images
        table.setSelectionBackground(new Color(0, 75, 14));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 75, 14));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, 
                        hasFocus, row, column);
                c.setForeground(Color.BLACK);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(column == 4 || column >= 8 ? CENTER : LEFT);
                return c;
            }
        });
    }

    private void styleButton(JButton button, Dimension size) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 75, 14));
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

     private void loadPlayerData() {
        try {
            List<Player> players = playerController.getAllPlayers();
            
            tableModel.setRowCount(0);
            playerImages.clear();

            if (players != null && !players.isEmpty()) {
                for (Player player : players) {
                    ImageIcon playerImage = loadPlayerImage(player.getPlayerId(), player.getName());
                    playerImages.put(player.getPlayerId(), playerImage);
                    
                    Object[] rowData = {
                        player.getPlayerId(),
                        playerImage,
                        player.getName(),
                        player.getPosition(),
                        player.getJerseyNumber(),
                        player.getDateOfBirth(),
                        player.getNationality(),
                        player.isActive(),
                        player.getGoals(),
                        player.getAssists(),
                        player.getMatchesPlayed()
                    };
                    tableModel.addRow(rowData);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No player data available", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading player data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}