package nl.spaan.student_app.payload.response;

import java.util.List;

public class JwtInlogResponse {

    private long id;
    private String username;
    private String firstName;
    private String email;
    private String roles;

    public JwtInlogResponse(long id, String username, String firstName, String email, String roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.email = email;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

}
