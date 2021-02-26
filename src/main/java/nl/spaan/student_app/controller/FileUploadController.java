package nl.spaan.student_app.controller;

import nl.spaan.student_app.model.FileDB;
import nl.spaan.student_app.payload.response.UploadResponseMessage;
import nl.spaan.student_app.storage.FileStorageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/files")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<UploadResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                            @RequestParam("amount") String amount,
                                                            @RequestParam("date") String date,
                                                            @RequestHeader Map<String, String> headers) {
        String message = "";
        String token = headers.get("authorization");
        try {
            fileStorageService.store( file,amount, date, token );
            System.out.println("Bla einde");
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new UploadResponseMessage(message));
        } catch (Exception e) {
            System.out.println("Bla foutje");
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new UploadResponseMessage(message));
        }
    }


    @GetMapping(value = "/download/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> getFile(@PathVariable long id , HttpServletRequest request) {
        return fileStorageService.getFile(id,request);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public void delete() {

    }
}
