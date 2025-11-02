package Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_id", insertable = false, updatable = false)
    private Fan fan;

    @Column(name = "fan_id", nullable = false)
    private int fanId;

    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private double amount;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Transient
    private String fanName; // For display purposes only

    // Constructors
    public Transaction() {
        this.transactionDate = new Date();
        this.status = "completed";
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Fan getFan() {
        return fan;
    }

    public void setFan(Fan fan) {
        this.fan = fan;
        if (fan != null) {
            this.fanId = fan.getFanId();
            this.fanName = fan.getName();
        }
    }

    public int getFanId() {
        return fanId;
    }

    public void setFanId(int fanId) {
        this.fanId = fanId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getFanName() {
        return fanName;
    }

    public void setFanName(String fanName) {
        this.fanName = fanName;
    }

    // Helper methods
    public String getFormattedTransactionId() {
        if (matchId != null && matchId > 0) {
            return "TKT-" + transactionId;
        } else if (itemId != null && itemId > 0) {
            return "MER-" + transactionId;
        } else if ("fundraising".equalsIgnoreCase(paymentMethod)) {
            return "FUND-" + transactionId;
        }
        return "TRN-" + transactionId;
    }

    public String getTransactionType() {
        if (matchId != null && matchId > 0) {
            return "Match Ticket";
        } else if (itemId != null && itemId > 0) {
            return "Merchandise";
        } else if ("fundraising".equalsIgnoreCase(paymentMethod)) {
            return "Fundraising";
        }
        return "Other";
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", fanId=" + fanId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }

    public void rollback() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void commit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTransactionDescription() {
    if (matchId != null && matchId > 0) {
        return "Match ID: " + matchId;
    } else if (itemId != null && itemId > 0) {
        return "Item ID: " + itemId;
    } else if ("fundraising".equalsIgnoreCase(paymentMethod)) {
        return "Fundraising";
    } else {
        return "Other";
    }
}


public String getTransactionIdAsString() {
    return String.valueOf(this.transactionId);
}

}