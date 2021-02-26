package nl.spaan.student_app.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

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

    private String accountNumber;
    private double waterUtility;
    private double gasUtility;
    private double elektraUtility;
    private double internetUtility;

    @OneToOne
    @MapsId
    @JoinColumn(name = "house_id")
    private House house;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getWaterUtility() {
        return waterUtility;
    }

    public void setWaterUtility(double waterUtility) {
        this.waterUtility = waterUtility;
    }

    public double getGasUtility() {
        return gasUtility;
    }

    public void setGasUtility(double gasUtility) {
        this.gasUtility = gasUtility;
    }

    public double getElektraUtility() {
        return elektraUtility;
    }

    public void setElektraUtility(double elektraUtility) {
        this.elektraUtility = elektraUtility;
    }

    public double getInternetUtility() {
        return internetUtility;
    }

    public void setInternetUtility(double internetUtility) {
        this.internetUtility = internetUtility;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

}
