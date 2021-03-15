package nl.spaan.student_app.payload.response;

public class DeclarationResponse {

    private long id;
    private String firstName;
    private String lastName;
    private double amount;
    private String fileName;



    public DeclarationResponse(long id, String firstName, String lastName, double amount, String fileName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.amount = amount;
        this.fileName = fileName;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
