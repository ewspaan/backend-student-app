package nl.spaan.student_app.service;

import nl.spaan.student_app.model.*;
import nl.spaan.student_app.payload.request.DeclarationRequest;
import nl.spaan.student_app.payload.response.DeclarationResponse;
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

    private BillRepository billRepository;

    private BillService billService;

    private UserService userService;

    private FileStorageService fileStorageService;


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
    public void setBillRepository(BillRepository billRepository) {
        this.billRepository = billRepository;
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

    @Override
    public ResponseEntity<?> storeDeclaration(String token, DeclarationRequest declarationRequest) {


        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        //Todo check voor juiste string
        double amountDouble = Double.parseDouble(declarationRequest.getAmount());

        User user = userService.findUserNameFromToken(token);
        Declaration declaration = new Declaration();

        declaration.setHouse(user.getHouse());
        declaration.setUser(user);
        declaration.setMonth(month);
        declaration.setYear(year);
        declaration.setGroceriesAmount(amountDouble);
        declaration.setCorrect(false);
        declaration.setChecked(false);
        billService.updateBill(declaration);
        fileStorageService.store(declarationRequest.getFileName(),token,declaration);
        declarationRepository.save(declaration);

        return ResponseEntity.ok(declaration.getId());

    }

    @Override
    public ResponseEntity<?> getAllDeclarations(String token, boolean checked) {

        List<Declaration> declarations = declarationRepository.findAllByHouseId(userService.findUserNameFromToken(token).getHouse().getId());
        List<Declaration> declarationsToCheck = new ArrayList<>();
        if (!declarations.isEmpty() && checked) {
            for (Declaration declaration : declarations) {
                if (!declaration.isChecked()) {
                    declarationsToCheck.add(declaration);
                }
            }
        }else {
            for (Declaration declaration : declarations) {
                if (declaration.isChecked()) {
                    declarationsToCheck.add(declaration);
                }
            }
        }

        return ResponseEntity.ok(createDeclarationResponse(declarationsToCheck));
    }

    @Override
    public ResponseEntity<?> getDeclarationsUser(String token, boolean checked){

        List<Declaration> declarations = declarationRepository.findAllByUserIdAndCorrect(userService.findUserNameFromToken(token).getId(), checked);

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
        return null;
    }

    @Override
    public ResponseEntity<?> updateDeclaration(String token, DeclarationRequest declarationRequest){

        System.out.println("bla decla--> " + declarationRequest.getId());
        Declaration declaration;
        if(declarationExist(declarationRequest.getId())) {
            declaration = declarationRepository.getOne(declarationRequest.getId());
        }else {
            return ResponseEntity.ok("declaratie bestaat niet");
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
        return ResponseEntity.ok("declaratie verandert");
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
                declarationResponses.add(declarationResponse);
            }
        }
        return (declarationResponses);
    }
}
