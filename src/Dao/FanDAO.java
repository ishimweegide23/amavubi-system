package Dao;

import Model.Fan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FanDAO {

    // CREATE: Register a new fan
   public boolean registerFan(Fan fan) {
    String query = "INSERT INTO fans (national_id, name, phone, email, password, tier, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, fan.getNationalId());
        pstmt.setString(2, fan.getName());
        pstmt.setString(3, fan.getPhone());
        pstmt.setString(4, fan.getEmail());
        pstmt.setString(5, fan.getPassword());
        
        String tier = fan.getTier();
        if (tier == null || tier.trim().isEmpty()) {
            tier = "STANDARD"; // default fallback
        }
        pstmt.setString(6, tier);
        
        String role = fan.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "Fan"; // default role
        }
        pstmt.setString(7, role);

        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

   // READ: Authenticate a fan by email and password
public Fan authenticateFan(String email, String password) {
    String query = "SELECT * FROM fans WHERE email = ? AND password = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, email);
        pstmt.setString(2, password);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Create Fan object using available constructor
                Fan fan = new Fan(
                    rs.getInt("fan_id"),
                    rs.getString("national_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("tier")
                );

                // ✅ Set role separately since it's not in constructor
                fan.setRole(rs.getString("role"));
                return fan;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    // Check if a national ID is already registered
    public boolean isNationalIdRegistered(String nationalId) {
        String query = "SELECT fan_id FROM fans WHERE national_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nationalId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if an email is already registered
    public boolean isEmailRegistered(String email) {
        String query = "SELECT fan_id FROM fans WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE: Update a fan's tier
    public boolean updateFanTier(int fanId, String newTier) {
        String query = "UPDATE fans SET tier = ? WHERE fan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newTier);
            pstmt.setInt(2, fanId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Remove a fan by ID
    public boolean deleteFan(int fanId) {
    String query = "DELETE FROM fans WHERE fan_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, fanId);
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

   public Fan getFanById(int fanId) {
    String query = "SELECT * FROM fans WHERE fan_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setInt(1, fanId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Fan fan = new Fan();
                fan.setFanId(rs.getInt("fan_id"));
                fan.setNationalId(rs.getString("national_id"));
                fan.setName(rs.getString("name"));
                fan.setPhone(rs.getString("phone"));
                fan.setEmail(rs.getString("email"));
                fan.setPassword(rs.getString("password"));
                fan.setTier(rs.getString("tier"));
                fan.setRole(rs.getString("role"));
                return fan;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

   public boolean updateFan(Fan fan) {
    String query = "UPDATE fans SET name = ?, email = ?, phone = ?, tier = ?, role = ? WHERE fan_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, fan.getName());
        pstmt.setString(2, fan.getEmail());
        pstmt.setString(3, fan.getPhone());
        pstmt.setString(4, fan.getTier());
        pstmt.setString(5, fan.getRole());
        pstmt.setInt(6, fan.getFanId());

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

   
   
   public List<Fan> getAllFans() {
    List<Fan> fans = new ArrayList<>();
    String query = "SELECT * FROM fans ORDER BY fan_id";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Fan fan = new Fan();
            fan.setFanId(rs.getInt("fan_id"));
            fan.setNationalId(rs.getString("national_id"));
            fan.setName(rs.getString("name"));
            fan.setPhone(rs.getString("phone"));
            fan.setEmail(rs.getString("email"));
            fan.setTier(rs.getString("tier"));
            fan.setRole(rs.getString("role"));
            fans.add(fan);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return fans;
}

   // Add this method to FanDAO.java:
public List<Fan> searchFans(String searchTerm) {
    List<Fan> fans = new ArrayList<>();
    String query = "SELECT * FROM fans WHERE " +
                 "fan_id LIKE ? OR " +
                 "national_id LIKE ? OR " +
                 "name LIKE ? OR " +
                 "email LIKE ? OR " +
                 "phone LIKE ? OR " +
                 "tier LIKE ? OR " +
                 "role LIKE ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        String likeTerm = "%" + searchTerm + "%";
        pstmt.setString(1, likeTerm);
        pstmt.setString(2, likeTerm);
        pstmt.setString(3, likeTerm);
        pstmt.setString(4, likeTerm);
        pstmt.setString(5, likeTerm);
        pstmt.setString(6, likeTerm);
        pstmt.setString(7, likeTerm);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Fan fan = new Fan();
                fan.setFanId(rs.getInt("fan_id"));
                fan.setNationalId(rs.getString("national_id"));
                fan.setName(rs.getString("name"));
                fan.setPhone(rs.getString("phone"));
                fan.setEmail(rs.getString("email"));
                fan.setPassword(rs.getString("password"));
                fan.setTier(rs.getString("tier"));
                fan.setRole(rs.getString("role"));
                fans.add(fan);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return fans;
}
   
   
}
