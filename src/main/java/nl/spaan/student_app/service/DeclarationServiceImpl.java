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
import java.time.LocalDate;
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
    public ResponseEntity<?> storeDeclaration(String token, String amount, MultipartFile file) {

        /*Bij het binnenkomen van een declaratie wordt eerst gekeken of er voor die maand al een huisrekening is gemaakt.
          Is die er niet dan wordt die aangemaakt. Dan wordt er gekeken of de user al een huisrekening voor die maand heeft.
          Is die er niet wordt die aangemaakt.
          Declaratie wordt toegevoegd aan huisrekening algemeen en huisrekening user.
        */

        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        //Todo check voor juiste string
        double amountDouble = Double.parseDouble(amount);
        System.out.println("bla " + amountDouble + "  " + amount);

        User user = userService.findUserNameFromToken(token);
        BillHouse billHouse = new BillHouse();
        BillUser billUser = new BillUser();
        Declaration declaration = new Declaration();
        List<BillHouse> houseBills = user.getHouse().getBillHouse();
        List<BillUser> userBills = user.getUserBill();

        for(int i = 0; i < houseBills.size(); i++){
            if(houseBills.get(i).getMonth() == month && houseBills.get(i).getYear() == year){
                billHouse = houseBills.get(i);
            }
        }
        System.out.println("bla " + billHouse.getYear());
        if (billHouse.getYear() == 0)
        {
            billHouse.setHouse(user.getHouse());
            billHouse.setMonth(month);
            billHouse.setYear(year);
            billHouse.setTotalUtilities(user.getHouse().getAccount().getTotalAmountUtilities());

        }
        System.out.println("bla decla");
        for(int i = 0; i < userBills.size(); i++){
            if(userBills.get(i).getMonth() == month && userBills.get(i).getYear() == year ){
                billUser = userBills.get(i);
            }
        }

        if (billUser.getYear() == 0){
            billUser.setUser(user);
            billUser.setMonth(month);
            billUser.setYear(year);
            billUser.setTotalAmount(billHouse.getTotalAmount());

        }
        //verhoog het totaal van de gedeclareerde boodschappen
        billHouse.setTotalDeclarations(billHouse.getTotalDeclarations() + amountDouble);
        billUser.setTotalDeclarations(billUser.getTotalDeclarations() + amountDouble);

        declaration.setHouse(user.getHouse());
        declaration.setUser(user);
        declaration.setMonth(month);
        declaration.setYear(year);
        declaration.setGroceriesAmount(amountDouble);
        declaration.setCorrect(false);
        declaration.setChecked(false);
        fileStorageService.store(file,token,declaration);

        billRepository.save(billUser);
        billRepository.save(billHouse);
        declarationRepository.save(declaration);

        return ResponseEntity.ok(declaration.getId());

    }

    @Override
    public ResponseEntity<?> getAllDeclarations(String token) {

        List<Declaration> declarations = declarationRepository.findAllByHouseId(userService.findUserNameFromToken(token).getHouse().getId());
        List<Declaration> declarationsToCheck = new ArrayList<Declaration>();
        List<DeclarationResponse> declarationResponses= new ArrayList<>();
        if (!declarations.isEmpty()) {
            for (int i = 0; i < declarations.size(); i++) {
                if (!declarations.get(i).isChecked()) {
                    declarationsToCheck.add(declarations.get(i));
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
                        declaration.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        declaration.getGroceriesAmount(),
                        fileDB.getFilePath());
                declarationResponses.add(declarationResponse);
            }
        }
        return ResponseEntity.ok(declarationResponses);
    }
}
