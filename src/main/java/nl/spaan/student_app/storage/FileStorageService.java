package nl.spaan.student_app.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Transactional
@Service
public interface FileStorageService {


    ResponseEntity<?> getFile(long id , HttpServletRequest request);

    void init();

    void store(MultipartFile file, String amount, String date, String token) throws IOException;
}
