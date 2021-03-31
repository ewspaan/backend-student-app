package nl.spaan.student_app.storage;

import nl.spaan.student_app.model.Declaration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FileStorageService {


    ResponseEntity<?> store(String file, String token, Declaration declaration);
}
