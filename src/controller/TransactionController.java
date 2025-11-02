package controller;

import Model.Transaction;
import Model.Fan;
import Service.TransactionService;
import Service.FanService;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TransactionController {
    private final TransactionService transactionService;
    private final FanService fanService;
    
    // Define allowed payment methods and statuses
    private static final Set<String> ALLOWED_PAYMENT_METHODS = new HashSet<>(Arrays.asList(
        "mobile_money", "credit_card", "bank_transfer", "cash", "fundraising"
    ));
    
    private static final Set<String> ALLOWED_STATUSES = new HashSet<>(Arrays.asList(
        "pending", "completed", "cancelled", "refunded"
    ));

    public TransactionController() {
        this.transactionService = new TransactionService();
        this.fanService = new FanService();
    }

    // CREATE operations - remain unchanged
    public boolean createMatchBooking(int fanId, int matchId, double ticketPrice, String paymentMethod) {
        validateFanExists(fanId);
        validatePositiveAmount(ticketPrice);
        validatePaymentMethod(paymentMethod);
        
        return transactionService.createMatchBooking(fanId, matchId, ticketPrice, paymentMethod);
    }

    public boolean createMerchandisePurchase(int fanId, int itemId, int quantity, double totalPrice) {
        validateFanExists(fanId);
        validatePositiveAmount(totalPrice);
        validatePositiveQuantity(quantity);
        
        return transactionService.createMerchandisePurchase(fanId, itemId, quantity, totalPrice);
    }

    public boolean createFundraisingDonation(int fanId, double amount, String paymentMethod) {
        validateFanExists(fanId);
        validatePositiveAmount(amount);
        validatePaymentMethod(paymentMethod);
        
        return transactionService.createFundraisingDonation(fanId, amount, paymentMethod);
    }

    // READ operations - remain unchanged
    public Transaction getTransactionById(int transactionId) {
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Invalid transaction ID");
        }
        return transactionService.getTransactionById(transactionId);
    }

    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    public List<Transaction> getTransactionsByFanId(int fanId) {
        validateFanExists(fanId);
        return transactionService.getTransactionsByFanId(fanId);
    }

    // UPDATE operations - remain unchanged
    public boolean updateTransaction(int transactionId, String paymentMethod, String status) {
        validateTransactionExists(transactionId);
        validatePaymentMethod(paymentMethod);
        validateStatus(status);
        
        return transactionService.updateTransaction(transactionId, paymentMethod, status);
    }

    // DELETE operations - remain unchanged
    public boolean deleteTransaction(int transactionId) {
        validateTransactionExists(transactionId);
        return transactionService.deleteTransaction(transactionId);
    }

    // SEARCH operations - remain unchanged
    public List<Transaction> searchTransactions(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return transactionService.searchTransactions(searchTerm);
    }

    public List<Transaction> searchTransactionsWithFanName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return transactionService.searchTransactionsWithFanName(searchTerm);
    }

    public List<Transaction> searchTransactionsForUser(int fanId, String searchTerm) {
        validateFanExists(fanId);
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return transactionService.searchTransactionsForUser(fanId, searchTerm);
    }

    // HELPER methods
    public String getFormattedTransactionId(Transaction t) {
        if (t == null) return "TRN-0";
        
        if (t.getMatchId() != null && t.getMatchId() > 0) {
            return "TKT-" + t.getTransactionId();
        } else if (t.getItemId() != null && t.getItemId() > 0) {
            return "MER-" + t.getTransactionId();
        } else if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) {
            return "FUND-" + t.getTransactionId();
        }
        return "TRN-" + t.getTransactionId();
    }

    public String getTransactionType(Transaction t) {
        if (t == null) return "Other";
        
        if (t.getMatchId() != null && t.getMatchId() > 0) {
            return "Match Ticket";
        } else if (t.getItemId() != null && t.getItemId() > 0) {
            return "Merchandise";
        } else if ("fundraising".equalsIgnoreCase(t.getPaymentMethod())) {
            return "Fundraising";
        }
        return "Other";
    }

    public String getMatchName(int matchId) {
        if (matchId <= 0) {
            throw new IllegalArgumentException("Invalid match ID");
        }
        return transactionService.getMatchName(matchId);
    }

    public String getMerchandiseName(int itemId) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("Invalid item ID");
        }
        return transactionService.getMerchandiseName(itemId);
    }

    // Enhanced method to get transaction description with proper names
    public String getTransactionDescription(Transaction transaction) {
        if (transaction == null) {
            return "Invalid Transaction";
        }
        
        try {
            if (transaction.getMatchId() != null && transaction.getMatchId() > 0) {
                String matchInfo = getMatchName(transaction.getMatchId());
                return "Match: " + (matchInfo != null ? matchInfo : "ID " + transaction.getMatchId());
            } else if (transaction.getItemId() != null && transaction.getItemId() > 0) {
                String itemInfo = getMerchandiseName(transaction.getItemId());
                return "Item: " + (itemInfo != null ? itemInfo : "ID " + transaction.getItemId());
            } else if ("fundraising".equalsIgnoreCase(transaction.getPaymentMethod())) {
                return "Fundraising Donation";
            }
        } catch (Exception e) {
            System.err.println("Error getting transaction description: " + e.getMessage());
            // Fallback to IDs if there's an error
            if (transaction.getMatchId() != null && transaction.getMatchId() > 0) {
                return "Match ID: " + transaction.getMatchId();
            } else if (transaction.getItemId() != null && transaction.getItemId() > 0) {
                return "Item ID: " + transaction.getItemId();
            }
        }
        
        return "General Transaction";
    }

    // VALIDATION methods - remain unchanged
    private void validateFanExists(int fanId) {
        Fan fan = fanService.getFanById(fanId);
        if (fan == null) {
            throw new IllegalArgumentException("Fan with ID " + fanId + " does not exist");
        }
    }

    private void validateTransactionExists(int transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction with ID " + transactionId + " does not exist");
        }
    }

    private void validatePositiveAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    private void validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }
        
        if (!ALLOWED_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())) {
            throw new IllegalArgumentException("Invalid payment method. Allowed methods are: " 
                + String.join(", ", ALLOWED_PAYMENT_METHODS));
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        
        if (!ALLOWED_STATUSES.contains(status.toLowerCase())) {
            throw new IllegalArgumentException("Invalid status. Allowed statuses are: " 
                + String.join(", ", ALLOWED_STATUSES));
        }
    }

    public String getFanName(int fanId) {
        try {
            Fan fan = fanService.getFanById(fanId);
            if (fan != null) {
                return fan.getName();
            } else {
                System.err.println("Fan not found with ID: " + fanId);
                return "Fan ID: " + fanId;
            }
        } catch (Exception e) {
            System.err.println("Error fetching fan name for ID " + fanId + ": " + e.getMessage());
            return "Fan ID: " + fanId;
        }
    }
}