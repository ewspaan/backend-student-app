package nl.spaan.student_app.service;

import nl.spaan.student_app.model.*;
import nl.spaan.student_app.payload.request.DeclarationRequest;
import nl.spaan.student_app.payload.response.DeclarationResponse;
import nl.spaan.student_app.payload.response.MessageResponse;
import nl.spaan.student_app.repository.BillRepository;
import nl.spaan.student_app.repository.DeclarationRepository;
import nl.spaan.student_app.repository.FileDBRepository;
import nl.spaan.student_app.repository.UserRepository;
import nl.spaan.student_app.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Transactional
@Service
public class DeclarationServiceImpl implements DeclarationService {

    private DeclarationRepository declarationRepository;

    private UserRepository userRepository;

    private FileDBRepository fileDBRepository;

    private BillService billService;

    private UserService userService;

    private FileStorageService fileStorageService;

    private BillRepository billRepository;


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


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Autowired
    public void setBillService(BillService billService) {
        this.billService = billService;
    }

    @Autowired
    public void setBillRepository(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public ResponseEntity<?> storeDeclaration(String token, DeclarationRequest declarationRequest) {


        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        //Todo check voor juiste string
        double amountDouble = Double.parseDouble(declarationRequest.getAmount());

        User user = userService.findUserNameFromToken(token);
        if (user.getUserBill().isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Je bent nieuw. Je kunt vanaf volgende maand declaraties invoeren"));
        }
        Declaration declaration = new Declaration();
        declaration.setHouse(user.getHouse());
        declaration.setUser(user);
        declaration.setMonth(month);
        declaration.setYear(year);
        declaration.setGroceriesAmount(amountDouble);
        declaration.setCorrect(false);
        declaration.setChecked(false);
        fileStorageService.store(declarationRequest.getFileName(),token,declaration);
        declarationRepository.save(declaration);
        billService.updateBillWhenDeclarationsChange(declaration.getHouse().getId(), month, year);

        return ResponseEntity.ok(new MessageResponse("Declaratie succesvol opgeslagen"));

    }

    @Override
    public ResponseEntity<?> getAllDeclarations(String token, boolean correct) {

        List<Declaration> declarations = declarationRepository.findAllByHouseId(userService.findUserNameFromToken(token).getHouse().getId());
        List<Declaration> declarationsToCheck = new ArrayList<>();
        // originele declaratie en verkeerde declaraties
        if (!declarations.isEmpty() && !correct) {
            for (Declaration declaration : declarations) {
                    declarationsToCheck.add(declaration);
            }
        // correcte declaraties
        }else {
            for (Declaration declaration : declarations) {
                if (declaration.isCorrect()) {
                    declarationsToCheck.add(declaration);
                }
            }
        }
        return ResponseEntity.ok(createDeclarationResponse(declarationsToCheck));
    }

    @Override
    public ResponseEntity<?> getDeclarationsUser(String token, boolean correct){

        List<Declaration> declarations = declarationRepository.findAllByUserIdAndAndCheckedAndCorrect(userService.findUserNameFromToken(token).getId(), true, correct);

        return ResponseEntity.ok(createDeclarationResponse(declarations));
    }

    @Override
    public ResponseEntity<?> getDeclaration(String authorization, long id){
        Declaration declaration = declarationRepository.getOne(id);
        DeclarationResponse declarationResponse = new DeclarationResponse(id,
                declaration.getUser().getFirstName()
                ,declaration.getUser().getLastName()
                ,declaration.getGroceriesAmount()
                ,declaration.getFileDB().getNameFile());

        return ResponseEntity.ok(declarationResponse);
    }

    @Override
    public ResponseEntity<?> editDeclaration(String token, DeclarationRequest declarationRequest) {

        Declaration declaration = declarationRepository.getOne(declarationRequest.getId());
        double amountDouble = Double.parseDouble(declarationRequest.getAmount());
        declaration.setChecked(false);
        declaration.setGroceriesAmount(amountDouble);
        declarationRepository.save(declaration);
        FileDB fileDB = fileDBRepository.getOne(declaration.getId());
        fileDB.setNameFile(declarationRequest.getFileName());
        fileDBRepository.save(fileDB);
        billService.updateBillWhenDeclarationsChange(declaration.getHouse().getId(), declaration.getMonth(), declaration.getYear());
        return ResponseEntity.ok(new MessageResponse("Declaratie succesvol verandert"));
    }

    @Override
    public ResponseEntity<?> checkDeclaration(String token, DeclarationRequest declarationRequest){

        Declaration declaration;
        if(declarationExist(declarationRequest.getId())) {
            declaration = declarationRepository.getOne(declarationRequest.getId());
        }else {
            return ResponseEntity.ok(new MessageResponse("declaratie bestaat niet"));
        }
        if(!declarationRequest.getCorrect()){
            declaration.setChecked(true);
            declaration.setCorrect(false);
            declarationRepository.save(declaration);
        }else {
            declaration.setChecked(true);
            declaration.setCorrect(true);
            declarationRepository.save(declaration);
        }
        return ResponseEntity.ok(new MessageResponse("declaratie verandert"));
    }

    @Override
    public ResponseEntity<?> deleteDeclaration(long id){

        Declaration declaration =declarationRepository.getOne(id);
        declarationRepository.deleteById(id);
        billService.updateBillWhenDeclarationsChange(declaration.getHouse().getId(), declaration.getMonth(), declaration.getYear());
        return ResponseEntity.ok(new MessageResponse("Declaratie succesvol verwijdert"));
    }

    private boolean declarationExist(long id){
        return declarationRepository.existsById(id);
    }

    private List<DeclarationResponse> createDeclarationResponse(List<Declaration> declarations){

        List<DeclarationResponse> declarationResponses= new ArrayList<>();
        User user;
        FileDB fileDB;
        if(!declarations.isEmpty()) {
            for (Declaration declaration : declarations) {
                user = userRepository.findUserById(declaration.getUser().getId());
                fileDB = fileDBRepository.findFileById(declaration.getId());
                DeclarationResponse declarationResponse = new DeclarationResponse(
                        declaration.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        declaration.getGroceriesAmount(),
                        fileDB.getNameFile());
                declarationResponse.setChecked(declaration.isChecked());
                declarationResponse.setCorrect(declaration.isCorrect());
                declarationResponse.setMonth(declaration.getMonth());
                declarationResponse.setYear(declaration.getYear());
                declarationResponses.add(declarationResponse);
            }
        }
        return (declarationResponses);
    }
}
