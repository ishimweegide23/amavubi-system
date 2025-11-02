package Service;

import Model.Fan;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IFanService extends Remote {
    // Authentication
    Fan authenticateFan(String email, String password, String otp) throws RemoteException;
    String generateAndSendOTP(String email) throws RemoteException;
    
    // CRUD Operations
    boolean registerFan(Fan fan) throws RemoteException;
    boolean updateFan(Fan fan) throws RemoteException;
    boolean deleteFan(int id) throws RemoteException;
    
    // Queries
    Fan getFanById(int id) throws RemoteException;
    List<Fan> getAllFans() throws RemoteException;
    List<Fan> searchFans(String searchTerm) throws RemoteException;
    
    // Validations
    boolean isEmailRegistered(String email) throws RemoteException;
    boolean isNationalIdRegistered(String nationalId) throws RemoteException;
    
    // Tier Management
    boolean updateFanTier(int fanId, String newTier) throws RemoteException;
}