package nl.spaan.student_app.service;

import nl.spaan.student_app.model.BillHouse;
import nl.spaan.student_app.model.Declaration;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.BillRequest;
import nl.spaan.student_app.repository.AccountRepository;
import nl.spaan.student_app.repository.BillRepository;
import nl.spaan.student_app.repository.DeclarationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class BillServiceImpl implements BillService {

    private BillRepository billRepository;

    private UserService userService;

    private AccountRepository accountRepository;

    private DeclarationRepository declarationRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBillRepository(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setDeclarationRepository(DeclarationRepository declarationRepository) {
        this.declarationRepository = declarationRepository;
    }

    @Override
    public ResponseEntity<?> createBill(String token, BillRequest billRequest) {

        return ResponseEntity.ok("Huisrekening succesvol gemaakt");
    }

}
