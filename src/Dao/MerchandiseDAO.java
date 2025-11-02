package Dao;

import Model.Merchandise;
import Model.Transaction;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import Util.HibernateUtil;

public class MerchandiseDAO {

    // Get all merchandise items
    public List<Merchandise> getMerchandiseList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Merchandise");
            return query.list();
        } finally {
            session.close();
        }
    }

    // Get merchandise by category
    public List<Merchandise> getMerchandiseByCategory(String category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Merchandise WHERE category = :category");
            query.setParameter("category", category);
            return query.list();
        } finally {
            session.close();
        }
    }

    // Update stock quantity
    public boolean updateStockQuantity(int itemId, int newQuantity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = (Transaction) session.beginTransaction();
            Query query = session.createQuery("UPDATE Merchandise SET stockQuantity = :newQuantity WHERE itemId = :itemId");
            query.setParameter("newQuantity", newQuantity);
            query.setParameter("itemId", itemId);
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    // Other methods remain the same as they're view-specific
    public Merchandise getMerchandiseByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Merchandise WHERE name = :name");
            query.setParameter("name", name);
            return (Merchandise) query.uniqueResult();
        } finally {
            session.close();
        }
    }

    public String getAvailableSizes(int itemId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("SELECT size FROM Merchandise WHERE itemId = :itemId");
            query.setParameter("itemId", itemId);
            return (String) query.uniqueResult();
        } finally {
            session.close();
        }
    }

    public boolean checkSizeAvailability(int itemId, String size) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("SELECT stockQuantity FROM Merchandise WHERE itemId = :itemId AND size LIKE :size");
            query.setParameter("itemId", itemId);
            query.setParameter("size", "%" + size + "%");
            Integer quantity = (Integer) query.uniqueResult();
            return quantity != null && quantity > 0;
        } finally {
            session.close();
        }
    }
}