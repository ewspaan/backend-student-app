package nl.spaan.student_app.service;

import nl.spaan.student_app.payload.request.DeclarationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public interface DeclarationService {



    ResponseEntity<?> getAllDeclarations(String authorization);

    ResponseEntity<?> storeDeclaration(String token, DeclarationRequest declarationRequest);
}
