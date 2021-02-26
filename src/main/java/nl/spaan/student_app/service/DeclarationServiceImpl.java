package nl.spaan.student_app.service;

import nl.spaan.student_app.model.Declaration;
import nl.spaan.student_app.model.FileDB;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.response.DeclarationResponse;
import nl.spaan.student_app.repository.DeclarationRepository;
import nl.spaan.student_app.repository.FileDBRepository;
import nl.spaan.student_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class DeclarationServiceImpl implements DeclarationService {

    private DeclarationRepository declarationRepository;

    private UserRepository userRepository;

    private FileDBRepository fileDBRepository;


    @Autowired
    public void setDeclarationRepository(DeclarationRepository declarationRepository) {
        this.declarationRepository = declarationRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFileDBRepository(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    @Override
    public ResponseEntity<?> getNewDeclarations(String token) {

        List<Declaration> declarations = declarationRepository.findAll();
        List<Declaration> declarationsToCheck = new ArrayList<Declaration>();
        List<DeclarationResponse> declarationResponses= new ArrayList<>();
        if (!declarations.isEmpty()) {
            for (int i = 0; i < declarations.size(); i++) {
                if (!declarations.get(i).isChecked()) {
                    declarationsToCheck.add(declarations.get(i));
                    System.out.println(declarationsToCheck.get(i).isChecked());
                }
            }
        }
        User user;
        FileDB fileDB;
        if(!declarationsToCheck.isEmpty()) {
            for (Declaration declaration : declarationsToCheck) {
                user = userRepository.findUserById(declaration.getUser().getId());
                fileDB = fileDBRepository.findFileById(declaration.getId());
                DeclarationResponse declarationResponse = new DeclarationResponse(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        declaration.getId(),
                        fileDB.getFilePath(),
                        fileDB.getId());
                declarationResponses.add(declarationResponse);
            }
        }
        return ResponseEntity.ok(declarationResponses);
    }
}
