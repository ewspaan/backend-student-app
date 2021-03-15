package nl.spaan.student_app.storage;

import nl.spaan.student_app.model.Declaration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Service
public interface FileStorageService {

    void init();

    ResponseEntity<?> store(MultipartFile file, String token);

    ResponseEntity<?> store(String file, String token, Declaration declaration);
}
