package nl.spaan.student_app.payload.response;

public class BillResponseUser {

    private int month;
    private int year;
    private long id;
    private String firstName;
    private String lastName;
    private double declarationsUser;
    private double toPayMonth;
    private boolean payed;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
