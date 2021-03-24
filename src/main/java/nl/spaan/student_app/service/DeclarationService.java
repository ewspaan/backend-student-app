package nl.spaan.student_app.service;

import nl.spaan.student_app.payload.request.DeclarationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface DeclarationService {



    ResponseEntity<?> getAllDeclarations(String token, boolean checked);

    ResponseEntity<?> storeDeclaration(String token, DeclarationRequest declarationRequest);

    ResponseEntity<?> updateDeclaration(String token, DeclarationRequest declarationRequest);

    ResponseEntity<?> getDeclarationsUser(String authorization, boolean checked);

    ResponseEntity<?> getDeclaration(String authorization, long id);

    ResponseEntity<?> editDeclaration(String authorization, DeclarationRequest declarationRequest);
}
