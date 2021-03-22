package nl.spaan.student_app.payload.response;

import java.util.List;

public class BillResponse {

    private double totalAmountMonth;
    private double totalAmountDeclarations;
    private double totalAmountUtilities;
    private int month;
    private int year;
    private boolean payed;
    private List<BillResponseUser> billResponseUsers;


    public double getTotalAmountMonth() {
        return totalAmountMonth;
    }

    public void setTotalAmountMonth(double totalAmountMonth) {
        this.totalAmountMonth = totalAmountMonth;
    }

    public double getTotalAmountDeclarations() {
        return totalAmountDeclarations;
    }

    public void setTotalAmountDeclarations(double totalAmountDeclarations) {
        this.totalAmountDeclarations = totalAmountDeclarations;
    }

    public double getTotalAmountUtilities() {
        return totalAmountUtilities;
    }

    public void setTotalAmountUtilities(double totalAmountUtilities) {
        this.totalAmountUtilities = totalAmountUtilities;
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

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public List<BillResponseUser> getBillResponseUsers() {
        return billResponseUsers;
    }

    public void setBillResponseUsers(List<BillResponseUser> billResponseUsers) {
        this.billResponseUsers = billResponseUsers;
    }
}
