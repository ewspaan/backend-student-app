package nl.spaan.student_app.controller;


import nl.spaan.student_app.payload.request.DeclarationRequest;
import nl.spaan.student_app.service.DeclarationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all/{checked}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getDeclarations(@RequestHeader Map<String, String> headers,
                                             @PathVariable("checked") boolean checked) {
        return declarationService.getAllDeclarations(headers.get("authorization"), checked);
    }

    @GetMapping("/personal/{checked}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> getDeclarationsUser(@RequestHeader Map<String, String> headers,
                                             @PathVariable("checked") boolean checked) {
        return declarationService.getDeclarationsUser(headers.get("authorization"), checked);
    }

    @GetMapping("/personal/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> getDeclaration(@RequestHeader Map<String, String> headers,
                                                 @PathVariable("id") long id) {
        return declarationService.getDeclaration(headers.get("authorization"), id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> uploadDeclaration(@RequestHeader Map<String, String> headers,
                                               @RequestBody DeclarationRequest declarationRequest) {
        return declarationService.storeDeclaration(headers.get("authorization"),declarationRequest);
    }
    @PutMapping("/checked")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> checkDeclaration(@RequestHeader Map<String, String> headers,
                                               @RequestBody DeclarationRequest declarationRequest){
        return declarationService.checkDeclaration(headers.get("authorization"),declarationRequest);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> editDeclaration(@RequestHeader Map<String, String> headers,
                                             @RequestBody DeclarationRequest declarationRequest){
            return declarationService.editDeclaration(headers.get("authorization"),declarationRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> deleteDeclaration(@PathVariable("id") long id){
        return declarationService.deleteDeclaration(id);
    }



}
