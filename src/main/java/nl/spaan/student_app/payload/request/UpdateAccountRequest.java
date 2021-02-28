package nl.spaan.student_app.payload.request;

public class UpdateAccountRequest {

    private String accountNumber;
    private double waterUtility;
    private double gasUtility;
    private double elektraUtility;
    private double internetUtility;

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
