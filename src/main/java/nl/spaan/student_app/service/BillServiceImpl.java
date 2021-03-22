package nl.spaan.student_app.service;

import nl.spaan.student_app.model.*;
import nl.spaan.student_app.payload.response.BillResponse;
import nl.spaan.student_app.payload.response.BillResponseUser;
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
    public void createBill(long houseId, int month, int year) {

        Bill bill = new Bill();
        List<User> users = new ArrayList<>(houseRepository.getOne(houseId).getUserList());
        if (!checkIfBillMonthExist(houseId, month, year)) {
            bill.setHouse(houseRepository.getOne(houseId));
            bill.setTotalUtilities(houseRepository.getOne(houseId).getAccount().getTotalAmountUtilities());
            bill.setMonth(month);
            bill.setYear(year);
            bill.setTotalDeclarations(0);
            bill.setTotalAmount(0);
            billRepository.save(bill);
            for( User user : users){
                BillUser billUser = new BillUser();
                billUser.setBill(bill);
                billUser.setUser(user);
                billUser.setTotalDeclarations(0);
                billUser.setAmountToPay(0);
                billUserRepository.save(billUser);
            }
        }
    }

    @Override
    public void updateBill(Declaration declaration){

        int month = declaration.getMonth();
        int year = declaration.getYear();
        long id = declaration.getHouse().getId();
        if (!checkIfBillMonthExist(id, month, year)){
            createBill(id,month,year);
        }
        Bill bill = billRepository.findByHouseIdAndMonthAndYear(id, month, year);
        bill.setTotalDeclarations(bill.getTotalDeclarations()+declaration.getGroceriesAmount());

        BillUser billUser = billUserRepository.findByUserIdAndBillId(declaration.getUser().getId(), bill.getId());
        billUser.setTotalDeclarations(billUser.getTotalDeclarations()+declaration.getGroceriesAmount());

        billRepository.save(bill);
        billUserRepository.save(billUser);

    }

    @Override
    public void updateBillHouse(long id, int month, int year){

        if (!checkIfBillMonthExist(id, month, year)){
            createBill(id,month,year);
        }
        Bill bill = billRepository.findByHouseIdAndMonthAndYear(id,month,year);
        bill.setTotalUtilities(accountRepository.findByHouseId(id).getTotalAmountUtilities());
        billRepository.save(bill);
    }


    @Override
    public ResponseEntity<?> getHouseBill(long houseId){

        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        //Vul lijst met alle rekeningen van een huis
        if (!checkIfBillMonthExist(houseId, month, year)){
            createBill(houseId,month,year);
        }
        List<Bill> billHouses = new ArrayList<>(billRepository.findByHouseId(houseId));
        //maak lijst voor de response
        List<BillResponse> billResponses = new ArrayList<>();
        for (Bill billHouse : billHouses) {
            //voeg eerst de algemen huisrekening toe
            BillResponse billResponse = new BillResponse();
            billResponse.setTotalAmountUtilities(billHouse.getTotalUtilities());
            billResponse.setTotalAmountDeclarations(billHouse.getTotalDeclarations());
            billResponse.setMonth(billHouse.getMonth());
            billResponse.setYear(billHouse.getYear());
            billResponse.setPayed(billHouse.isPayed());
            billResponse.setTotalAmountMonth(billHouse.getTotalUtilities() + billHouse.getTotalDeclarations());
            List<BillUser> billUsers = new ArrayList<>(billUserRepository.findAll());
            List<BillResponseUser> billResponseUsers = new ArrayList<>();
            //voeg daarna de individuele rekeningen toe
            for (BillUser billUser : billUsers) {
                if (billUser.getBill().getId() == billHouse.getId()) {
                    BillResponseUser billResponseUser = new BillResponseUser();
                    billResponseUser.setMonth(billUser.getBill().getMonth());
                    billResponseUser.setYear(billUser.getBill().getYear());
                    billResponseUser.setId(billUser.getId());
                    billResponseUser.setFirstName(billUser.getUser().getFirstName());
                    billResponseUser.setLastName(billUser.getUser().getLastName());
                    billResponseUser.setToPayMonth(billUser.getAmountToPay());
                    billResponseUsers.add(billResponseUser);
                }
            }
            billResponse.setBillResponseUsers(billResponseUsers);
            billResponses.add(billResponse);
        }
        return ResponseEntity.ok(billResponses);
    }

    @Override
    public boolean checkIfBillMonthExist(long houseId, int month, int year){

        List<Bill> bills = new ArrayList<>(billRepository.findByHouseId(houseId));
        for( Bill bill : bills) {
            if(bill.getYear() == year && bill.getMonth() == month){
               return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<?> togglePayed(long userBillId){

        BillUser billUser = billUserRepository.getOne(userBillId);
        billUser.setPayed(true);
        billUserRepository.save(billUser);
        toggleHouseBillPayed(billUser.getBill().getId());
        return ResponseEntity.ok("Rekening is betaalt");
    }

    @Override
    public ResponseEntity<?> getHouseBillUser(String token, boolean payed){

        List<BillUser> billUsers = userService.findUserNameFromToken(token).getUserBill();
        System.out.println("billuserpersonal--> " + billUsers.size());
        List<BillResponseUser> bills = new ArrayList<>();
        for (BillUser billUser : billUsers) {
            if (billUser.isPayed() == payed) {
                BillResponseUser billResponseUser = new BillResponseUser();
                billResponseUser.setMonth(billUser.getBill().getMonth());
                billResponseUser.setYear(billUser.getBill().getYear());
                billResponseUser.setToPayMonth(billUser.getAmountToPay());
                billResponseUser.setPayed(billUser.isPayed());
                bills.add(billResponseUser);
            }
        }
        return ResponseEntity.ok(bills);
    }

    private void toggleHouseBillPayed(long billId){

        boolean check = true;
        Bill bill = billRepository.getOne(billId);
        List<BillUser> billUsers = billUserRepository.findAllByBillId(billId);
        for (BillUser billUser : billUsers){
            if(!billUser.isPayed()){
                check =false;
            }
        }
        if(check){
            bill.setPayed(true);
            billRepository.save(bill);
        }
    }

    private double getAmountDeclarationsMonthUser (User user, int month, int year){

        double amount = 0;
        List<Declaration> declarations = new ArrayList<>(declarationRepository.findAllByUserIdAndMonthAndYear(user.getId(), month, year));
        for (Declaration declaration : declarations) {
            if (declaration.getYear() == year && declaration.getMonth() == month) {
                amount += declaration.getGroceriesAmount();
                System.out.println("bla amount user" + amount);
            }
        }
        return amount;
    }

    private double getAmountDeclarationsMonthHouse (long houseId, int month, int year){

        double amount = 0;
        List<Declaration> declarations = new ArrayList<>(declarationRepository.findAllByHouseIdAndMonthAndYear(houseId, month, year));
        declarations.removeIf(declaration -> declaration.getYear() != year && declaration.getMonth() != month);
        for (Declaration declaration : declarations){
            amount += declaration.getGroceriesAmount();
            System.out.println("bla amount house" + amount);
        }

        return amount;
    }

}
