package nl.spaan.student_app;

import nl.spaan.student_app.payload.request.SignupRequest;
import nl.spaan.student_app.service.AuthorizationService;
import nl.spaan.student_app.service.BillServiceImpl;
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
        erwin.setFirstName("Bla");
        erwin.setLastName("Blatenstein");
        erwin.setUsername("ews");
        erwin.setEmail("kansloos@gmail.com");
        erwin.setDateOfBirth("01-02-2000");
        erwin.setPassword("password");
        authorizationService.registerUser(erwin);


        SignupRequest ralph = new SignupRequest();
        ralph.setFirstName("Bladibla");
        ralph.setLastName("van Bla");
        ralph.setUsername("bla");
        ralph.setEmail("kansarm@gmail.com");
        ralph.setDateOfBirth("03-04-2001");
        ralph.setPassword("password");
        authorizationService.registerUser(ralph);


    }
}
