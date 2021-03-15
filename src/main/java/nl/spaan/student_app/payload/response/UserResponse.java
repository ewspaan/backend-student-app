package nl.spaan.student_app.payload.response;

public class UserResponse {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String roles;
    private String houseName;
    private String accountNumber;
    private double waterUtility;
    private double gasUtility;
    private double elektraUtility;
    private double internetUtility;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
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
}
