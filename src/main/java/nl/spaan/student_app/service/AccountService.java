package nl.spaan.student_app.service;

import nl.spaan.student_app.payload.request.UpdateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    ResponseEntity<?> updateAccount(String authorization, UpdateAccountRequest updateAccountRequest);
    ResponseEntity<?> getAccount(String authorization);
}
