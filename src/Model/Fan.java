package Model;

public class Fan {
    private int fanId;
    private String nationalId;
    private String name; // Full name
    private String phone;
    private String email;
    private String password;
    private String tier;

    // ✅ NEW: Role field
    private String role;

    // ✅ Updated constructor with fanId, password before tier
    public Fan(int fanId, String nationalId, String name, String phone, String email, String password, String tier) {
        this.fanId = fanId;
        this.nationalId = nationalId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        setTier(tier); // Use setter for validation
    }

    // ✅ Constructor without fanId, password before tier
    public Fan(String nationalId, String name, String phone, String email, String password, String tier) {
        this.nationalId = nationalId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        setTier(tier); // Use setter for validation
    }

    public Fan() {
    // Initialize with default values
    this.tier = "STANDARD";
    this.role = "Fan";
}

    // ✅ Getters and Setters
    public int getFanId() { return fanId; }
    public void setFanId(int fanId) { this.fanId = fanId; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTier() { return tier; }

    // ✅ Validation to avoid invalid tier values
    public void setTier(String tier) {
        if (tier == null || tier.trim().isEmpty()) {
            this.tier = "Standard"; // default fallback
        } else {
            String formattedTier = tier.trim().toUpperCase();
            switch (formattedTier) {
                case "STANDARD":
                case "VIP":
                case "PREMIUM":
                    this.tier = formattedTier;
                    break;
                default:
                    this.tier = "STANDARD"; // fallback on unknown
                    System.out.println("⚠️ Invalid tier passed. Defaulting to 'STANDARD'");
            }
        }
    }

    // ✅ NEW: Getter and Setter for Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            this.role = "Fan"; // default role
        } else {
            this.role = role.trim();
        }
    }

    // Optional
    public String getFullName() {
        return name;
    }
}
