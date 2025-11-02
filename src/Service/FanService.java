package Service;

import Dao.FanDAO;
import Model.Fan;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import Util.HibernateUtil;
import java.rmi.Remote;
import java.util.Random;

public class FanService implements Remote {
    private FanDAO fanDao;

    public FanService() {
        this.fanDao = new FanDAO();
    }
    
    public boolean registerFan(Fan fan) {
        return fanDao.registerFan(fan);
    }
       
     public boolean saveFan(Fan fan) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
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


    public boolean updateFan(Fan fan) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(fan);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public boolean deleteFan(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Fan fan = (Fan) session.get(Fan.class, id);
            if (fan != null) {
                session.delete(fan);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public Fan getFan(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (Fan) session.get(Fan.class, id);
        } finally {
            session.close();
        }
    }

    public List<Fan> getAllFans() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Using the exact syntax that works with Hibernate 4.3.x
            return session.createCriteria(Fan.class).list();
        } finally {
            session.close();
        }
    }

  public Fan authenticateFan(String email, String password) {
        return fanDao.authenticateFan(email, password);
    }

    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    
    public String generateAndSendOTP(String email) {
        return fanDao.generateAndStoreOTP(email);
    }
    
    public List<Fan> searchFans(String searchTerm) {
        return fanDao.searchFans(searchTerm);
    }

    public Fan getFanById(int fanId) {
        return getFan(fanId);
    }

    public boolean isNationalIdRegistered(String nationalId) {
        return fanDao.isNationalIdRegistered(nationalId);
    }

    public boolean isEmailRegistered(String email) {
        return fanDao.isEmailRegistered(email);
    }

    public boolean updateFanTier(int fanId, String newTier) {
        Fan fan = getFan(fanId);
        if (fan != null) {
            fan.setTier(newTier);
            return updateFan(fan);
        }
        return false;
    }

  
}