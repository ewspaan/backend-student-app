package nl.spaan.student_app.service;

import nl.spaan.student_app.model.*;
import nl.spaan.student_app.payload.request.DeclarationRequest;
import nl.spaan.student_app.payload.response.DeclarationResponse;
import nl.spaan.student_app.repository.BillRepository;
import nl.spaan.student_app.repository.DeclarationRepository;
import nl.spaan.student_app.repository.FileDBRepository;
import nl.spaan.student_app.repository.UserRepository;
import nl.spaan.student_app.storage.FileStorageService;
import nl.spaan.student_app.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Transactional
@Service
public class DeclarationServiceImpl implements DeclarationService {

    private DeclarationRepository declarationRepository;

    private UserRepository userRepository;

    private FileDBRepository fileDBRepository;

    private BillRepository billRepository;

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

    @Override
    public ResponseEntity<?> storeDeclaration(String token, DeclarationRequest declarationRequest) {

        /*Bij het binnenkomen van een declaratie wordt eerst gekeken of er voor die maand al een huisrekening is gemaakt.
          Is die er niet dan wordt die aangemaakt. Dan wordt er gekeken of de user al een huisrekening boor die maand heeft.
          Is die er niet wordt die aangemaakt.
          Declaratie wordt toegevoegd aan huisrekening algemeen en huisrekening user.
        */
        System.out.println("bla-->" + declarationRequest.getAmount());
        System.out.println("bla-->" + declarationRequest.getYear());
        System.out.println("bla-->" + declarationRequest.getMonth());
        System.out.println("bla-->" + declarationRequest.getFileName());

        User user = userService.findUserNameFromToken(token);
        BillHouse billHouse = new BillHouse();
        BillUser billUser = new BillUser();
        Declaration declaration = new Declaration();
        List<BillHouse> houseBills = user.getHouse().getBillHouse();
        List<BillUser> userBills = user.getUserBill();

        for(int i = 0; i < houseBills.size(); i++){
            if(houseBills.get(i).getMonth() == declarationRequest.getMonth() && houseBills.get(i).getYear() == declarationRequest.getYear()){
                billHouse = houseBills.get(i);
            }
        }
        System.out.println("bla" + billHouse.getYear());
        if (billHouse.getYear() == 0)
        {
            billHouse.setHouse(user.getHouse());
            billHouse.setMonth(declarationRequest.getMonth());
            billHouse.setYear(declarationRequest.getYear());
            billHouse.setTotalUtilities(user.getHouse().getAccount().getTotalAmountUtilities());

        }
        System.out.println("bla");
        for(int i = 0; i < userBills.size(); i++){
            if(userBills.get(i).getMonth() == declarationRequest.getMonth() && userBills.get(i).getYear() == declarationRequest.getYear() ){
                billUser = userBills.get(i);
            }
        }

        if (billUser.getYear() == 0){
            billUser.setUser(user);
            billUser.setMonth(declarationRequest.getMonth());
            billUser.setYear(declarationRequest.getYear());
            billUser.setTotalAmount(billHouse.getTotalAmount());

        }
        //verhoog het totaal van de gedeclareerde boodschappen
        billHouse.setTotalDeclarations(billHouse.getTotalDeclarations() + declarationRequest.getAmount());
        billUser.setTotalDeclarations(billUser.getTotalDeclarations() + declarationRequest.getAmount());

        declaration.setHouse(user.getHouse());
        declaration.setUser(user);
        declaration.setMonth(declarationRequest.getMonth());
        declaration.setYear(declarationRequest.getYear());
        declaration.setGroceriesAmount(declarationRequest.getAmount());
        declaration.setCorrect(false);
        declaration.setChecked(false);

        billRepository.save(billUser);
        billRepository.save(billHouse);
        declarationRepository.save(declaration);

        return ResponseEntity.ok(declaration.getId());

    }

    @Override
    public ResponseEntity<?> getAllDeclarations(String token) {

        System.out.println("blabla");
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
                        declaration.getGroceriesAmount());
                declarationResponses.add(declarationResponse);
            }
        }
        return ResponseEntity.ok(declarationResponses);
    }
}
