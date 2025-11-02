package Service;

import Model.Merchandise;
import Dao.MerchandiseDAO;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import Util.HibernateUtil;

public class MerchandiseService {
    private MerchandiseDAO merchandiseDAO;

    public MerchandiseService() {
        this.merchandiseDAO = new MerchandiseDAO();
    }

    // Save merchandise using Hibernate
    public void saveMerchandise(Merchandise merchandise) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.save(merchandise);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    // Update merchandise using Hibernate
    public void updateMerchandise(Merchandise merchandise) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.update(merchandise);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    // Delete merchandise using Hibernate
    public void deleteMerchandise(int itemId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            Merchandise merchandise = (Merchandise) session.get(Merchandise.class, itemId);
            if (merchandise != null) {
                session.delete(merchandise);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    // Get all merchandise - using DAO since it has custom logic
    public List<Merchandise> getAllMerchandise() {
        return merchandiseDAO.getMerchandiseList();
    }

    // Get merchandise by category - using DAO
    public List<Merchandise> getMerchandiseByCategory(String category) {
        return merchandiseDAO.getMerchandiseByCategory(category);
    }

    // Get merchandise by ID - using Hibernate
    public Merchandise getMerchandiseById(int itemId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (Merchandise) session.get(Merchandise.class, itemId);
        } finally {
            session.close();
        }
    }

    // Update stock quantity - using DAO as it's a partial update
    public boolean updateStockQuantity(int itemId, int newQuantity) {
        return merchandiseDAO.updateStockQuantity(itemId, newQuantity);
    }
}