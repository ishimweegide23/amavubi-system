package controller;

import Dao.FanDAO;
import Model.Fan;
import Service.FanService;
import Service.OTPService;
import java.util.List;

public class FanController {
    private final FanService fanService;
    private final FanDAO fanDAO;
    private Fan currentFan;
    private final OTPService otpService;

    public FanController(FanService fanService, FanDAO fanDAO) {
        this.fanService = fanService;
        this.fanDAO = fanDAO;
        this.otpService = new OTPService();
    }

    // Simplified constructor for convenience
    public FanController() {
        this(new FanService(), new FanDAO());
    }

    
      // Register a new fan with OTP verification
    public boolean registerFan(Fan fan, String otp) {
        if (!validateFan(fan)) {
            return false;
        }
        
        if (!otpService.verifyOTP(fan.getEmail(), otp)) {
            return false;
        }
        
        return fanService.registerFan(fan);
    }

    // Authenticate a fan
   public Fan authenticateFan(String email, String password) {
        Fan fan = fanService.authenticateFan(email, password);
        this.currentFan = fan; // Store authenticated fan
        return fan;
    }
    
     // OTP-related methods
    public String generateOTP(String email) {
        return otpService.generateAndSendOTP(email);
    }

    public boolean verifyOTP(String email, String otp) {
        return otpService.verifyOTP(email, otp);
    }
      public List<Fan> searchFans(String searchTerm) {
        return fanService.searchFans(searchTerm);
    }

    public Fan getCurrentFan() {
        return currentFan;
    }

    public void logout() {
        this.currentFan = null;
    }

    // Get fan by ID
    public Fan getFanById(int fanId) {
        if (fanId <= 0) {
            return null;
        }
        return fanService.getFanById(fanId);
    }

    // Update fan information
    public boolean updateFan(Fan fan) {
        if (fan == null || fan.getFanId() <= 0) {
            return false;
        }
        return fanService.updateFan(fan);
    }

    // Delete a fan
    public boolean deleteFan(int fanId) {
        if (fanId <= 0) {
            return false;
        }
        return fanService.deleteFan(fanId);
    }

    // Get all fans
    public List<Fan> getAllFans() {
        return fanService.getAllFans();
    }

  

    // Check if email exists
    public boolean isEmailRegistered(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return fanService.isEmailRegistered(email);
    }

    // Check if national ID exists
    public boolean isNationalIdRegistered(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            return false;
        }
        return fanService.isNationalIdRegistered(nationalId);
    }

    // Update fan tier
    public boolean updateFanTier(int fanId, String newTier) {
        if (fanId <= 0 || newTier == null || newTier.trim().isEmpty()) {
            return false;
        }
        return fanService.updateFanTier(fanId, newTier);
    }
    
      // Helper validation method
    private boolean validateFan(Fan fan) {
        if (fan == null) return false;
        
        return fan.getName() != null && !fan.getName().isEmpty()
            && fan.getEmail() != null && fan.getEmail().contains("@")
            && fan.getPassword() != null && fan.getPassword().length() >= 6
            && fan.getPhone() != null && !fan.getPhone().isEmpty();
    }

   

  
}