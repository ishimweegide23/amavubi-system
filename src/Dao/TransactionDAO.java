package Dao;

import static Dao.DatabaseConnection.getConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Transaction;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO() {
        this.connection = DatabaseConnection.getConnection();
        if (this.connection == null) {
            throw new IllegalStateException("❌ Database connection is null in TransactionDAO.");
        }
    }

    // ------------ INSERT METHODS ------------

    public boolean insertMatchBooking(int fanId, int matchId, double ticketPrice, String paymentMethod) {
        String query = "INSERT INTO transactions (fan_id, match_id, amount, payment_method, status, item_id) VALUES (?, ?, ?, ?, 'completed', NULL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, fanId);
            preparedStatement.setInt(2, matchId);
            preparedStatement.setDouble(3, ticketPrice);
            preparedStatement.setString(4, paymentMethod);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertMerchandisePurchase(int fanId, int itemId, int quantity, double totalPrice, String selectedSize) {
        String query = "INSERT INTO transactions (fan_id, item_id, amount, payment_method, status, match_id) VALUES (?, ?, ?, ?, 'completed', NULL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, fanId);
            preparedStatement.setInt(2, itemId);
            preparedStatement.setDouble(3, totalPrice);
            preparedStatement.setString(4, "mobile_money");
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

public boolean insertFundraisingTransaction(int fanId, double amount, String paymentMethod) {
    String sql = "INSERT INTO transactions (fan_id, amount, payment_method, status, match_id, item_id, transaction_date) " +
                 "VALUES (?, ?, ?, 'completed', NULL, NULL, NOW())";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, fanId);
        pstmt.setDouble(2, amount);
        pstmt.setString(3, paymentMethod); // ✅ Correctly using the parameter now
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // ------------ VIEW METHODS ------------

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, f.name as fan_name FROM transactions t LEFT JOIN fans f ON t.fan_id = f.fan_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transaction t = mapResultSetToTransaction(rs);
                transactions.add(t);
            }
            if (transactions.isEmpty()) {
                System.out.println("⚠ No transactions found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByFanId(int fanId) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, f.name as fan_name FROM transactions t LEFT JOIN fans f ON t.fan_id = f.fan_id WHERE t.fan_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, fanId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // ------------ UTIL MAPPING ------------

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setFanId(rs.getInt("fan_id"));
        t.setFanName(rs.getString("fan_name"));
        t.setMatchId((Integer) rs.getObject("match_id"));
        t.setItemId((Integer) rs.getObject("item_id"));
        t.setAmount(rs.getDouble("amount"));
        t.setPaymentMethod(rs.getString("payment_method"));
        t.setStatus(rs.getString("status"));
        t.setTransactionDate(rs.getTimestamp("transaction_date"));
        return t;
    }

    // ------------ UPDATE ------------

    public boolean updateTransaction(int transactionId, String paymentMethod, String status) throws SQLException {
        String query = "UPDATE transactions SET payment_method = ?, status = ? WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, paymentMethod);
            pstmt.setString(2, status);
            pstmt.setInt(3, transactionId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // ------------ DELETE ------------

    public boolean deleteTransaction(int transactionId) throws SQLException {
        String query = "DELETE FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // ------------ SEARCH ------------

    public List<Transaction> searchTransactions(String searchTerm) {
        return searchTransactionsWithFilter("WHERE t.transaction_id LIKE ? OR t.fan_id LIKE ? OR t.payment_method LIKE ? OR t.status LIKE ? OR t.transaction_date LIKE ?", searchTerm);
    }

    public List<Transaction> searchTransactionsWithFanName(String searchTerm) {
        return searchTransactionsWithFilter("WHERE t.transaction_id LIKE ? OR t.amount LIKE ? OR t.payment_method LIKE ? OR t.status LIKE ? OR t.transaction_date LIKE ? OR f.name LIKE ?", searchTerm);
    }

    public List<Transaction> searchTransactionsForUser(int fanId, String searchTerm) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, f.name as fan_name FROM transactions t LEFT JOIN fans f ON t.fan_id = f.fan_id " +
                "WHERE t.fan_id = ? AND (t.transaction_id LIKE ? OR t.amount LIKE ? OR t.payment_method LIKE ? OR t.status LIKE ? OR t.transaction_date LIKE ? OR f.name LIKE ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String likeTerm = "%" + searchTerm + "%";
            pstmt.setInt(1, fanId);
            for (int i = 2; i <= 7; i++) {
                pstmt.setString(i, likeTerm);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private List<Transaction> searchTransactionsWithFilter(String filterClause, String searchTerm) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, f.name as fan_name FROM transactions t LEFT JOIN fans f ON t.fan_id = f.fan_id " + filterClause;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String likeTerm = "%" + searchTerm + "%";
            for (int i = 1; i <= pstmt.getParameterMetaData().getParameterCount(); i++) {
                pstmt.setString(i, likeTerm);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // ------------ OTHER UTILS ------------

    public String getMatchName(int matchId) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT opponent FROM matches WHERE match_id = ?")) {
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return "Match vs " + rs.getString("opponent");
        }
        return "Match ID: " + matchId;
    }

    public String getMerchandiseName(int itemId) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name, size FROM merchandise WHERE item_id = ?")) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                return name + (size != null ? " (Size: " + size + ")" : "");
            }
        }
        return "Item ID: " + itemId;
    }

    public String getFormattedTransactionId(Transaction t) {
        if (t.getMatchId() != null && t.getMatchId() > 0) return "TKT-" + t.getTransactionId();
        if (t.getItemId() != null && t.getItemId() > 0) return "MER-" + t.getTransactionId();
        if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) return "FUND-" + t.getTransactionId();
        return "TRN-" + t.getTransactionId();
    }

    public String getTransactionType(Transaction t) {
        if (t.getMatchId() != null && t.getMatchId() > 0) return "Match Ticket";
        if (t.getItemId() != null && t.getItemId() > 0) return "Merchandise";
        if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) return "Fundraising";
        return "Other";
    }

    public boolean insertMerchandisePurchase(int fanId, int itemId, int quantity, double totalPrice) {
    return insertMerchandisePurchase(fanId, itemId, quantity, totalPrice, null);
}

}
