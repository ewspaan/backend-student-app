package nl.spaan.student_app.payload.response;

public class DeclarationResponse {

    private String firstName;
    private String lastName;
    private String username;
    private long declarationId;
    private String fileURL;
    private long fileId;

    public DeclarationResponse(String firstName, String lastName, String username, long declarationId, String fileURL, long fileId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.declarationId = declarationId;
        this.fileURL = fileURL;
        this.fileId = fileId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(long declarationId) {
        this.declarationId = declarationId;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }
}
