package nl.spaan.student_app.controller;


import nl.spaan.student_app.payload.request.DeclarationRequest;
import nl.spaan.student_app.payload.response.UploadResponseMessage;
import nl.spaan.student_app.service.DeclarationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/declarations")
public class DeclarationController {


    private DeclarationService declarationService;

    @Autowired
    public void setDeclarationService(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getDeclarations(@RequestHeader Map<String, String> headers) {
        return declarationService.getAllDeclarations(headers.get("authorization"));
    }
    @PostMapping("/upload")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> uploadDeclaration(@RequestParam("file") MultipartFile file,
                                        @RequestParam ("amount") String amount,
                                        @RequestHeader Map<String, String> headers) {
        return declarationService.storeDeclaration(headers.get("authorization"),amount,file);
    }

}
