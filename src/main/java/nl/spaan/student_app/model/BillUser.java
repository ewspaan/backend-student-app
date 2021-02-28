package nl.spaan.student_app.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bills_user")
public class BillUser extends Bill{



    private double totalDeclarations;
    private double amountToPay;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public double getTotalDeclarations() {
        return totalDeclarations;
    }

    public void setTotalDeclarations(double totalDeclarations) {
        this.totalDeclarations = totalDeclarations;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
