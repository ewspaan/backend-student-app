package nl.spaan.student_app.service;

import nl.spaan.student_app.model.User;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendMail(User user);
}
