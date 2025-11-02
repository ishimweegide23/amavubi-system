package Model;

import javax.persistence.*;

@Entity
@Table(name = "merchandise") // Maps to "merchandise" table in database
public class Merchandise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    @Column(name = "item_id") // Maps to item_id column
    private int itemId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "price", nullable = false)
    private double price;
    
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
    
    @Column(name = "size", length = 10)
    private String size;

    // No-arg constructor REQUIRED by Hibernate
    public Merchandise() {
    }

    // Your existing constructor
    public Merchandise(int itemId, String name, String category, double price, int stockQuantity, String size) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.size = size;
    }

    // Getters and Setters (keep all existing ones)
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}