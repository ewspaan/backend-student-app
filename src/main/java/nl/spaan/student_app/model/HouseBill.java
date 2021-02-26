package nl.spaan.student_app.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bills")
public class HouseBill {


        @Id
        @GeneratedValue(
                strategy= GenerationType.AUTO,
                generator="native"
        )
        @GenericGenerator(
                name = "native",
                strategy = "native"
        )
        @Column(columnDefinition = "serial")
        private long id;

        private String dateBill;
        private double totalAmount;

        @ManyToOne(fetch=FetchType.EAGER)
        @JoinColumn(name = "house_id", nullable = false)
        private House house;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateBill() {
        return dateBill;
    }

    public void setDateBill(String dateBill) {
        this.dateBill = dateBill;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
