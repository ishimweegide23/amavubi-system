package Dao;

import Model.Fan;
import Model.Transaction;
import Util.HibernateUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.hibernate.Query;
import org.hibernate.Session;

public class FanDAO {
    private Connection connection;

    public FanDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // CREATE: Register a new fan
   // Register a new fan with OTP
     public boolean registerFan(Fan fan) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = (Transaction) session.beginTransaction();
            session.save(fan);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


   // READ: Authenticate a fan by email and password
 public Fan authenticateFan(String email, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Fan WHERE email = :email AND password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            return (Fan) query.uniqueResult();
        } finally {
            session.close();
        }
    }
   public String generateAndStoreOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        String query = "UPDATE fans SET otp = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, otp);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            return otp;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
        String query = "SELECT * FROM fans WHERE fan_id LIKE ? OR national_id LIKE ? OR name LIKE ? OR email LIKE ? OR phone LIKE ? OR tier LIKE ? OR role LIKE ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String likeTerm = "%" + searchTerm + "%";
            for (int i = 1; i <= 7; i++) {
                pstmt.setString(i, likeTerm);
            }
            
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
