package nl.spaan.student_app.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private String accountNumber;
    private double waterUtility;
    private double gasUtility;
    private double elektraUtility;
    private double internetUtility;
    private double totalAmountUtilities;

    @OneToOne
    @MapsId
    @JoinColumn(name = "house_id")
    private House house;

    public Account() {
    }

    public Account(String accountNumber, double waterUtility, double gasUtility, double elektraUtility, double internetUtility, House house) {
        this.accountNumber = accountNumber;
        this.waterUtility = waterUtility;
        this.gasUtility = gasUtility;
        this.elektraUtility = elektraUtility;
        this.internetUtility = internetUtility;
        this.house = house;
    }

    public Account(String accountNumber, double waterUtility, double gasUtility, double elektraUtility, double internetUtility) {
        this.accountNumber = accountNumber;
        this.waterUtility = waterUtility;
        this.gasUtility = gasUtility;
        this.elektraUtility = elektraUtility;
        this.internetUtility = internetUtility;
    }



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

    public double getTotalAmountUtilities() {
        return totalAmountUtilities;
    }

    public void setTotalAmountUtilities(double totalAmountUtilities) {
        this.totalAmountUtilities = totalAmountUtilities;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

}
