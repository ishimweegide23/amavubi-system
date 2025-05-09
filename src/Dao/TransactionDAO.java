package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Transaction;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // ----------------- INSERT METHODS -----------------

    // Insert match ticket booking
    public boolean insertMatchBooking(int fanId, int matchId, double ticketPrice, String paymentMethod) {
        try {
            String query = "INSERT INTO transactions " +
                         "(fan_id, match_id, amount, payment_method, status, item_id) " +
                         "VALUES (?, ?, ?, ?, 'completed', NULL)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, fanId);
                preparedStatement.setInt(2, matchId);
                preparedStatement.setDouble(3, ticketPrice);
                preparedStatement.setString(4, paymentMethod);
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert merchandise purchase (basic)
    public boolean insertMerchandisePurchase(int fanId, int itemId, int quantity, double totalPrice) {
        return insertMerchandisePurchase(fanId, itemId, quantity, totalPrice, null);
    }

    // Insert merchandise purchase with size
    public boolean insertMerchandisePurchase(int fanId, int itemId, int quantity, 
                                           double totalPrice, String selectedSize) {
        try {
            String query = "INSERT INTO transactions " +
                         "(fan_id, item_id, amount, payment_method, status, match_id) " +
                         "VALUES (?, ?, ?, ?, 'completed', NULL)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, fanId);
                preparedStatement.setInt(2, itemId);
                preparedStatement.setDouble(3, totalPrice);
                preparedStatement.setString(4, "mobile_money");
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert fundraising donation
    public boolean insertFundraisingTransaction(int fanId, double amount, String paymentMethod) {
        try {
            String sql = "INSERT INTO transactions " +
                       "(fan_id, amount, payment_method, status, match_id, item_id) " +
                       "VALUES (?, ?, ?, 'completed', NULL, NULL)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, fanId);
                pstmt.setDouble(2, amount);
                pstmt.setString(3, "fundraising");
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generic fundraising transaction
    public boolean insertFundraisingTransaction(int fanId, double amount, String paymentMethod, 
                                             Integer matchId, Integer itemId) {
        try {
            String sql = "INSERT INTO transactions " +
                       "(fan_id, amount, payment_method, status, match_id, item_id) " +
                       "VALUES (?, ?, ?, 'completed', ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, fanId);
                pstmt.setDouble(2, amount);
                pstmt.setString(3, paymentMethod);
                
                if (matchId != null) {
                    pstmt.setInt(4, matchId);
                } else {
                    pstmt.setNull(4, Types.INTEGER);
                }
                
                if (itemId != null) {
                    pstmt.setInt(5, itemId);
                } else {
                    pstmt.setNull(5, Types.INTEGER);
                }
                
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deprecated fallback method
    public boolean insertFundraisingTransaction(int fanId, double amount, String paymentMethod, 
                                              Object object, Object object0) {
        return insertFundraisingTransaction(fanId, amount, paymentMethod, null, null);
    }

    // ----------------- VIEW / READ METHOD -----------------

    public List<Transaction> getTransactionsByFanId(int fanId) {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT * FROM transactions WHERE fan_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, fanId);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setFanId(rs.getInt("fan_id"));
                transaction.setMatchId(rs.getInt("match_id"));
                transaction.setItemId(rs.getInt("item_id"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setPaymentMethod(rs.getString("payment_method"));
                transaction.setStatus(rs.getString("status"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(transaction);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transactions;
}
    // ----------------- UPDATE METHOD -----------------

    public boolean updateTransaction(int transactionId, String paymentMethod, String status) {
        String query = "UPDATE transactions SET payment_method = ?, status = ? WHERE transaction_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, paymentMethod);
            pstmt.setString(2, status);
            pstmt.setInt(3, transactionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ----------------- DELETE METHOD -----------------

    public boolean deleteTransaction(int transactionId) {
        String query = "DELETE FROM transactions WHERE transaction_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
public List<Transaction> getAllTransactions() {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT * FROM transactions";

    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(rs.getInt("transaction_id"));
            transaction.setFanId(rs.getInt("fan_id"));
            transaction.setMatchId(rs.getInt("match_id"));
            transaction.setItemId(rs.getInt("item_id"));
            transaction.setAmount(rs.getDouble("amount"));
            transaction.setPaymentMethod(rs.getString("payment_method"));
            transaction.setStatus(rs.getString("status"));
            transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
            transactions.add(transaction);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transactions;
}

public List<Transaction> searchTransactions(String searchTerm) {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT * FROM transactions WHERE " +
                 "transaction_id LIKE ? OR " +
                 "fan_id LIKE ? OR " +
                 "payment_method LIKE ? OR " +
                 "status LIKE ? OR " +
                 "transaction_date LIKE ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        String likeTerm = "%" + searchTerm + "%";
        pstmt.setString(1, likeTerm);
        pstmt.setString(2, likeTerm);
        pstmt.setString(3, likeTerm);
        pstmt.setString(4, likeTerm);
        pstmt.setString(5, likeTerm);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setFanId(rs.getInt("fan_id"));
                t.setMatchId(rs.getInt("match_id"));
                t.setItemId(rs.getInt("item_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setPaymentMethod(rs.getString("payment_method"));
                t.setStatus(rs.getString("status"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(t);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transactions;
}
public List<Transaction> searchTransactionsWithFanName(String searchTerm) {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT t.* FROM transactions t " +
                 "JOIN fans f ON t.fan_id = f.fan_id " +
                 "WHERE t.transaction_id LIKE ? OR " +
                 "t.amount LIKE ? OR " +
                 "t.payment_method LIKE ? OR " +
                 "t.status LIKE ? OR " +
                 "t.transaction_date LIKE ? OR " +
                 "f.name LIKE ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        String likeTerm = "%" + searchTerm + "%";
        pstmt.setString(1, likeTerm);
        pstmt.setString(2, likeTerm);
        pstmt.setString(3, likeTerm);
        pstmt.setString(4, likeTerm);
        pstmt.setString(5, likeTerm);
        pstmt.setString(6, likeTerm);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setFanId(rs.getInt("fan_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setPaymentMethod(rs.getString("payment_method"));
                t.setStatus(rs.getString("status"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(t);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transactions;
}

public List<Transaction> searchTransactionsForUser(int fanId, String searchTerm) {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT t.* FROM transactions t " +
                 "JOIN fans f ON t.fan_id = f.fan_id " +
                 "WHERE t.fan_id = ? AND (" +
                 "t.transaction_id LIKE ? OR " +
                 "t.amount LIKE ? OR " +
                 "t.payment_method LIKE ? OR " +
                 "t.status LIKE ? OR " +
                 "t.transaction_date LIKE ? OR " +
                 "f.name LIKE ?)";
    
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        String likeTerm = "%" + searchTerm + "%";
        pstmt.setInt(1, fanId);
        pstmt.setString(2, likeTerm);
        pstmt.setString(3, likeTerm);
        pstmt.setString(4, likeTerm);
        pstmt.setString(5, likeTerm);
        pstmt.setString(6, likeTerm);
        pstmt.setString(7, likeTerm);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setFanId(rs.getInt("fan_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setPaymentMethod(rs.getString("payment_method"));
                t.setStatus(rs.getString("status"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(t);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transactions;
}

}