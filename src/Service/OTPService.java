package Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, Long> otpExpiry = new HashMap<>();
    
    public String generateAndSendOTP(String email) {
        String otp = generateOTP();
        otpStore.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + 300000); // 5 minute expiry
        
        // In a real application, implement actual email/SMS sending here
        System.out.println("OTP for " + email + ": " + otp);
        return otp;
    }
    
    public boolean verifyOTP(String email, String otp) {
        String storedOTP = otpStore.get(email);
        Long expiryTime = otpExpiry.get(email);
        
        if (storedOTP == null || expiryTime == null) {
            return false;
        }
        
        if (System.currentTimeMillis() > expiryTime) {
            otpStore.remove(email);
            otpExpiry.remove(email);
            return false;
        }
        
        return storedOTP.equals(otp);
    }
    
    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}