package nl.spaan.student_app.payload.response;

public class BillResponse {

    private double totalAmountMonth;
    private double totalAmountDeclarations;
    private double totalAmountUtilities;
    private int month;
    private int year;
    private String firstName;
    private String lastName;
    private double declarationsUser;
    private double toPayMonth;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getDeclarationsUser() {
        return declarationsUser;
    }

    public void setDeclarationsUser(double declarationsUser) {
        this.declarationsUser = declarationsUser;
    }

    public double getToPayMonth() {
        return toPayMonth;
    }

    public void setToPayMonth(double toPayMonth) {
        this.toPayMonth = toPayMonth;
    }
}
