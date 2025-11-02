package Model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fans")
public class Fan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fan_id")
    private int fanId;

    @Column(name = "national_id", unique = true, nullable = false)
    private String nationalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "tier")
    private String tier;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "role")
    private String role;

    @Column(name = "otp")
    private String otp;

    // ✅ Default constructor
    public Fan() {
        this.registrationDate = new Date(); // Default registration time
        this.role = "Fan"; // Default role
        this.tier = "STANDARD"; // Default membership
    }

    // ✅ 6-param constructor (used during registration without fanId)
    public Fan(String nationalId, String name, String phone,
               String email, String password, String tier) {
        this();
        this.nationalId = nationalId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.tier = tier;
    }

    // ✅ 7-param constructor (used during registration with fanId)
    public Fan(int fanId, String nationalId, String name, String phone,
               String email, String password, String tier) {
        this(nationalId, name, phone, email, password, tier);
        this.fanId = fanId;
    }

    // ✅ Full constructor (used in DAO or loading from DB)
    public Fan(int fanId, String nationalId, String name, String phone,
               String email, String password, String tier, String role,
               Date registrationDate, String otp) {
        this.fanId = fanId;
        this.nationalId = nationalId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.tier = tier;
        this.role = role;
        this.registrationDate = registrationDate;
        this.otp = otp;
    }

    // ✅ Getters and Setters
    public int getFanId() {
        return fanId;
    }

    public void setFanId(int fanId) {
        this.fanId = fanId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    // ✅ Role/membership helpers
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(this.role);
    }

    public boolean hasStandardMembership() {
        return "STANDARD".equalsIgnoreCase(this.tier);
    }

    public boolean hasPremiumMembership() {
        return "PREMIUM".equalsIgnoreCase(this.tier);
    }

    public boolean hasVIPMembership() {
        return "VIP".equalsIgnoreCase(this.tier);
    }

    @Override
    public String toString() {
        return "Fan{" +
                "fanId=" + fanId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", tier='" + tier + '\'' +
                '}';
    }
}
