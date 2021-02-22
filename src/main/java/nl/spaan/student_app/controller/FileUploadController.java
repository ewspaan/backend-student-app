package nl.spaan.student_app.controller;


import nl.spaan.student_app.payload.response.UploadResponseMessage;
import nl.spaan.student_app.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/files")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<UploadResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("File--> " + file);
            storageService.store(file);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new UploadResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new UploadResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.getFile(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" +
                file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public void delete() {
        storageService.deleteAll();
    }
}
