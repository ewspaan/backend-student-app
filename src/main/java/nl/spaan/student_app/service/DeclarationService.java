package nl.spaan.student_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface DeclarationService {

    ResponseEntity<?> getNewDeclarations(String authorization);
}
