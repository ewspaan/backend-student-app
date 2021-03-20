package nl.spaan.student_app.service;

import nl.spaan.student_app.model.*;
import nl.spaan.student_app.payload.request.BillRequest;
import nl.spaan.student_app.payload.response.BillResponse;
import nl.spaan.student_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {

    private BillRepository billRepository;

    private BillUserRepository billUserRepository;

    private UserService userService;

    private AccountRepository accountRepository;

    private DeclarationRepository declarationRepository;

    private HouseRepository houseRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBillRepository(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Autowired
    public void setBillUserRepository(BillUserRepository billUserRepository) {
        this.billUserRepository = billUserRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setDeclarationRepository(DeclarationRepository declarationRepository) {
        this.declarationRepository = declarationRepository;
    }

    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Override
    public Bill createBill(long houseId) {

        Bill bill = new Bill();
        List<User> users = new ArrayList<>(houseRepository.getOne(houseId).getUserList());
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        if (!checkIfBillMonthExist(houseId, month, year)) {
            bill.setHouse(houseRepository.getOne(houseId));
            bill.setTotalUtilities(houseRepository.getOne(houseId).getAccount().getTotalAmountUtilities());
            bill.setMonth(month);
            bill.setYear(year);
            bill.setTotalDeclarations(getAmountDeclarationsMonthHouse(houseId,month,year));
            bill.setTotalAmount(bill.getTotalDeclarations()+bill.getTotalUtilities());
            billRepository.save(bill);
            for( User user : users){
                BillUser billUser = new BillUser();
                billUser.setBill(bill);
                billUser.setUser(user);
                billUser.setTotalDeclarations(getAmountDeclarationsMonthUser(user,month,year));
                billUser.setAmountToPay((bill.getTotalAmount()/users.size()) - billUser.getTotalDeclarations());
                billUserRepository.save(billUser);
            }
            return bill;
        }

        return bill;
    }


    @Override
    public ResponseEntity<?> getHouseBill(long houseId){

        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        //Vul lijst met alle rekeningen van een huis
        List<Bill> billHouses = new ArrayList<>(billRepository.findByHouseId(houseId));
        //Haal alle betaalde rekeningen eruit
        billHouses.removeIf(Bill::isPayed);
        //Zijn er geen rekeningen maak er dan 1
        if (billHouses.size() == 0){
            billHouses.add(createBill(houseId));
        }
        System.out.println("bla bill" + billHouses.size());

        List<BillResponse> billResponses = new ArrayList<>();

        for (Bill billHouse : billHouses) {
            BillResponse billResponse = new BillResponse();
            billResponse.setTotalAmountUtilities(billHouse.getTotalUtilities());
            billResponse.setTotalAmountDeclarations(billHouse.getTotalDeclarations());
            billResponse.setMonth(billHouse.getMonth());
            billResponse.setYear(billHouse.getYear());
            billResponse.setTotalAmountMonth(billHouse.getTotalUtilities() + billHouse.getTotalDeclarations());
            billResponses.add(billResponse);
        }

        return ResponseEntity.ok(billResponses);
    }

    private boolean checkIfBillMonthExist(long houseId, int month, int year){

        List<Bill> bills = new ArrayList<>(billRepository.findByHouseId(houseId));
        return bills.contains(year) && bills.contains(month);
    }

    private double getAmountDeclarationsMonthUser (User user, int month, int year){

        double amount = 0;
        List<Declaration> declarations = new ArrayList<>(user.getDeclaration());
        for (Declaration declaration : declarations) {
            if (declaration.getYear() == year && declaration.getMonth() == month) {
                amount += declaration.getGroceriesAmount();
            }
        }
        return amount;
    }

    private double getAmountDeclarationsMonthHouse (long houseId, int month, int year){

        double amount = 0;
        List<Declaration> declarations = new ArrayList<>(declarationRepository.findAllByHouseId(houseId));
        System.out.println("bla size" + declarations.size());
        declarations.removeIf(declaration -> declaration.getYear() != year && declaration.getMonth() != month);
        for (Declaration declaration : declarations){
            amount += declaration.getGroceriesAmount();
            System.out.println("bla amount" + amount);
        }

        return amount;
    }

}
