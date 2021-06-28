package nl.spaan.student_app.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int month;
    private int year;
    private double totalUtilities;
    private double totalDeclarations;
    private double totalAmount;
    private boolean payed;
    private boolean payAble;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @OneToMany(
            mappedBy = "bill",
            fetch = FetchType.EAGER)
    private List<BillUser> BillsUser;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotalUtilities() {
        return totalUtilities;
    }

    public void setTotalUtilities(double totalUtilities) {
        this.totalUtilities = totalUtilities;
    }

    public double getTotalDeclarations() {
        return totalDeclarations;
    }

    public void setTotalDeclarations(double totalDeclarations) {
        this.totalDeclarations = totalDeclarations;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public List<BillUser> getBillsUser() {
        return BillsUser;
    }

    public void setBillsUser(List<BillUser> userBills) {
        this.BillsUser = userBills;
    }

    public boolean isPayAble() {
        return payAble;
    }

    public void setPayAble(boolean payAble) {
        this.payAble = payAble;
    }
}
