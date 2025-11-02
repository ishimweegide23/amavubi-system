package Service;

import Model.Transaction;
import Dao.TransactionDAO;
import Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final SessionFactory sessionFactory;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // CREATE operations
    public boolean createMatchBooking(int fanId, int matchId, double ticketPrice, String paymentMethod) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Transaction transaction = new Transaction();
            transaction.setFanId(fanId);
            transaction.setMatchId(matchId);
            transaction.setAmount(ticketPrice);
            transaction.setPaymentMethod(paymentMethod);
            transaction.setStatus("completed");
            
            session.save(transaction);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return transactionDAO.insertMatchBooking(fanId, matchId, ticketPrice, paymentMethod);
        } finally {
            session.close();
        }
    }

    public boolean createMerchandisePurchase(int fanId, int itemId, int quantity, double totalPrice) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Transaction transaction = new Transaction();
            transaction.setFanId(fanId);
            transaction.setItemId(itemId);
            transaction.setAmount(totalPrice);
            transaction.setPaymentMethod("mobile_money");
            transaction.setStatus("completed");
            
            session.save(transaction);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return transactionDAO.insertMerchandisePurchase(fanId, itemId, quantity, totalPrice);
        } finally {
            session.close();
        }
    }

    public boolean createFundraisingDonation(int fanId, double amount, String paymentMethod) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Transaction transaction = new Transaction();
            transaction.setFanId(fanId);
            transaction.setAmount(amount);
            transaction.setPaymentMethod(paymentMethod);
            transaction.setStatus("completed");
            
            session.save(transaction);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return transactionDAO.insertFundraisingTransaction(fanId, amount, paymentMethod);
        } finally {
            session.close();
        }
    }

    // READ operations
    public Transaction getTransactionById(int transactionId) {
        Session session = sessionFactory.openSession();
        try {
            return (Transaction) session.get(Transaction.class, transactionId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> getAllTransactions() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Transaction").list();
        } catch (Exception e) {
            e.printStackTrace();
            return transactionDAO.getAllTransactions();
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> getTransactionsByFanId(int fanId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from Transaction where fanId = :fanId")
                         .setParameter("fanId", fanId)
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return transactionDAO.getTransactionsByFanId(fanId);
        } finally {
            session.close();
        }
    }

    // UPDATE operations
    public boolean updateTransaction(int transactionId, String paymentMethod, String status) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Transaction transaction = (Transaction) session.get(Transaction.class, transactionId);
            if (transaction != null) {
                transaction.setPaymentMethod(paymentMethod);
                transaction.setStatus(status);
                session.update(transaction);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            try {
                return transactionDAO.updateTransaction(transactionId, paymentMethod, status);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } finally {
            session.close();
        }
    }

    // DELETE operations
    public boolean deleteTransaction(int transactionId) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Transaction transaction = (Transaction) session.get(Transaction.class, transactionId);
            if (transaction != null) {
                session.delete(transaction);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            try {
                return transactionDAO.deleteTransaction(transactionId);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } finally {
            session.close();
        }
    }

    // Search operations (using JDBC)
    public List<Transaction> searchTransactions(String searchTerm) {
        return transactionDAO.searchTransactions(searchTerm);
    }

    public List<Transaction> searchTransactionsWithFanName(String searchTerm) {
        return transactionDAO.searchTransactionsWithFanName(searchTerm);
    }

    public List<Transaction> searchTransactionsForUser(int fanId, String searchTerm) {
        return transactionDAO.searchTransactionsForUser(fanId, searchTerm);
    }

    // Helper methods
    public String getFormattedTransactionId(Transaction t) {
        return transactionDAO.getFormattedTransactionId(t);
    }

    public String getTransactionType(Transaction t) {
        return transactionDAO.getTransactionType(t);
    }

    public String getMatchName(int matchId) {
        try {
            return transactionDAO.getMatchName(matchId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Match ID: " + matchId;
        }
    }

    public String getMerchandiseName(int itemId) {
        try {
            return transactionDAO.getMerchandiseName(itemId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Item ID: " + itemId;
        }
    }
    
}