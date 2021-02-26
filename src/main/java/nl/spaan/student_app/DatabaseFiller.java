package nl.spaan.student_app;

import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.SignupRequest;
import nl.spaan.student_app.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFiller implements CommandLineRunner {

    private final AuthorizationService authorizationService;

    @Autowired
    public DatabaseFiller(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void run(String... args){

        SignupRequest erwin = new SignupRequest();
        erwin.setFirstName("Erwin");
        erwin.setLastName("Spaan");
        erwin.setUsername("ews");
        erwin.setEmail("ewspaan@gmail.com");
        erwin.setDateOfBirth("11-11-1977");
        erwin.setPassword("password");
        authorizationService.registerUser(erwin);

        SignupRequest ralph = new SignupRequest();
        ralph.setFirstName("Ralph");
        ralph.setLastName("Spaan");
        ralph.setUsername("rjs");
        ralph.setEmail("rjspaan@gmail.com");
        ralph.setDateOfBirth("04-10-1978");
        ralph.setPassword("password");
        authorizationService.registerUser(ralph);

    }
}
