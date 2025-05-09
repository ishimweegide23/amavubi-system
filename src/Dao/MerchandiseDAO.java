package Dao;

import Model.Merchandise;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MerchandiseDAO {

    // Get all merchandise items with their available sizes
    public List<Merchandise> getMerchandiseList() {
        List<Merchandise> merchandiseList = new ArrayList<>();
        String query = "SELECT * FROM merchandise";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Merchandise merchandise = new Merchandise(
                    resultSet.getInt("item_id"),
                    resultSet.getString("name"),
                    resultSet.getString("category"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("stock_quantity"),
                    resultSet.getString("size")  // Now properly handling sizes
                );
                merchandiseList.add(merchandise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return merchandiseList;
    }

    // Get merchandise by name with size information
    public Merchandise getMerchandiseByName(String name) {
        String query = "SELECT * FROM merchandise WHERE name = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Merchandise(
                        resultSet.getInt("item_id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getString("size")  // Include size information
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update stock quantity for a specific size (if applicable)
    public boolean updateStockQuantity(int itemId, int newQuantity) {
        String query = "UPDATE merchandise SET stock_quantity = ? WHERE item_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, itemId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check available sizes for an item
    public String getAvailableSizes(int itemId) {
        String query = "SELECT size FROM merchandise WHERE item_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, itemId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("size");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check stock availability for specific size
    public boolean checkSizeAvailability(int itemId, String size) {
        String query = "SELECT stock_quantity FROM merchandise WHERE item_id = ? AND size LIKE ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, itemId);
            preparedStatement.setString(2, "%" + size + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("stock_quantity") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get merchandise by ID with size info
    public Merchandise getMerchandiseById(int itemId) {
        String query = "SELECT * FROM merchandise WHERE item_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, itemId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Merchandise(
                        resultSet.getInt("item_id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getString("size")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}