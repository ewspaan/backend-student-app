package nl.spaan.student_app.service;

import nl.spaan.student_app.model.*;
import nl.spaan.student_app.payload.response.BillResponse;
import nl.spaan.student_app.payload.response.BillResponseUser;
import nl.spaan.student_app.payload.response.MessageResponse;
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
            bill.setPayAble(false);
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
    public void updateBillWhenDeclarationsChange(long id, int month, int year){

        if (!checkIfBillMonthExist(id, month, year)){
            createBill(id,month,year);
        }
        Bill bill = billRepository.findByHouseIdAndMonthAndYear(id, month, year);
        if(bill.isPayed()){
            return;
        }
        // calculeer totaal aan declaraties voor de maand/jaar van dit huis
        bill.setTotalDeclarations(getAmountDeclarationsMonthHouse(id,month,year));

        List<User> roommates = new ArrayList<>(bill.getHouse().getUserList());
        for (User user : roommates) {
            BillUser billUser = billUserRepository.findByUserIdAndBillId(user.getId(),bill.getId());
            billUser.setTotalDeclarations(getAmountDeclarationsMonthUser(user,month,year));
            billUser.setAmountToPay( ( (bill.getTotalUtilities()+bill.getTotalDeclarations()) /roommates.size() ) - billUser.getTotalDeclarations() );
            billUserRepository.save(billUser);
        }
        billRepository.save(bill);
    }
    private double getAmountDeclarationsMonthUser (User user, int month, int year){

        double amount = 0;
        List<Declaration> declarations = new ArrayList<>(declarationRepository.findAllByUserIdAndMonthAndYear(user.getId(), month, year));
        for (Declaration declaration : declarations) {
            if (declaration.getYear() == year && declaration.getMonth() == month) {
                amount += declaration.getGroceriesAmount();
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
        }
        return amount;
    }

    @Override
    public void updateBillWhenAccountChange(long id, int month, int year){

        if (!checkIfBillMonthExist(id, month, year)){
            createBill(id,month,year);
        }
        Bill bill = billRepository.findByHouseIdAndMonthAndYear(id,month,year);
        if(bill.isPayed()){
            return;
        }
        bill.setTotalUtilities(accountRepository.findByHouseId(id).getTotalAmountUtilities());
        List<User> roommates = new ArrayList<>(bill.getHouse().getUserList());
        for (User user : roommates) {
            BillUser billUser = billUserRepository.findByUserIdAndBillId(user.getId(), bill.getId());
            billUser.setAmountToPay(((
                    bill.getTotalDeclarations() + bill.getTotalUtilities())
                    / bill.getHouse().getUserList().size())
                    - billUser.getTotalDeclarations());
            billUserRepository.save(billUser);
        }
        billRepository.save(bill);
    }


    @Override
    public ResponseEntity<?> getHouseBill(long houseId){

        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();

        if (!checkIfBillMonthExist(houseId, month, year)){
            createBill(houseId,month,year);
        }
        makeBillHousePayAble(houseId);
        return ResponseEntity.ok(createHouseBillResponse(houseId));
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
        return ResponseEntity.ok(new MessageResponse("Rekening is betaalt"));
    }

    @Override
    public ResponseEntity<?> getHouseBillUser(String token, boolean payed){

        List<BillUser> billUsers = userService.findUserNameFromToken(token).getUserBill();
        List<BillResponseUser> bills = new ArrayList<>();
        for (BillUser billUser : billUsers) {
            if (billUser.isPayed() == payed) {
                BillResponseUser billResponseUser = new BillResponseUser();
                billResponseUser.setMonth(billUser.getBill().getMonth());
                billResponseUser.setYear(billUser.getBill().getYear());
                billResponseUser.setToPayMonth(billUser.getAmountToPay());
                billResponseUser.setPayed(billUser.isPayed());
                billResponseUser.setDeclarationsUser(billUser.getTotalDeclarations());
                bills.add(billResponseUser);
            }
        }
        return ResponseEntity.ok(bills);
    }

    private void makeBillHousePayAble(long houseId){

        LocalDate date = LocalDate.now();
        int monthNow = date.getMonthValue();
        int yearNow = date.getYear();
        List<Bill> bills = billRepository.findAllByPayed(false);
        for (Bill bill: bills){
            if( !(bill.getYear() == yearNow && bill.getMonth() == monthNow)){
                if (checkIfAllDeclarationAreCorrect(houseId,bill.getMonth(),bill.getYear())){
                    bill.setPayAble(true);
                    billRepository.save(bill);
                }
            }
        }
    }

    private boolean checkIfAllDeclarationAreCorrect(long houseId, int month, int year){

        List<Declaration> declarations = new ArrayList<>(declarationRepository.findAllByHouseIdAndMonthAndYear(houseId,month,year));
        boolean correct = true;
        if(declarations.isEmpty()){
            return true;
        }
        for(Declaration declaration : declarations){
            if(!declaration.isCorrect() || !declaration.isChecked()){
                correct = false;
            }
        }
        return correct;
    }

    private List<BillResponse> createHouseBillResponse(long houseId){

        //Vul lijst met alle rekeningen van een huis
        List<Bill> billHouses = new ArrayList<>(billRepository.findByHouseId(houseId));
        //verwijder de rekeningen die nog niet betaald mogen worden ivm met de declaraties
        Iterator itr = billHouses.iterator();
        while (itr.hasNext())
        {
            Bill bill = (Bill)itr.next();
            if(!bill.isPayAble()) {
                itr.remove();
            }
        }
        //maak lijst voor de response
        List<BillResponse> billResponses = new ArrayList<>();
        for (Bill billHouse : billHouses) {
            //voeg eerst de algemene huisrekening toe
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
                    billResponseUser.setDeclarationsUser(billUser.getTotalDeclarations());
                    billResponseUser.setToPayMonth(billUser.getAmountToPay());
                    billResponseUser.setPayed(billUser.isPayed());
                    billResponseUsers.add(billResponseUser);
                }
            }
            billResponse.setBillResponseUsers(billResponseUsers);
            billResponses.add(billResponse);
        }
        return (billResponses);

    }

    private void toggleHouseBillPayed(long billId){

        boolean check = true;
        Bill bill = billRepository.getOne(billId);
        List<BillUser> billUsers = billUserRepository.findAllByBillId(billId);
        for (BillUser billUser : billUsers){
            if (!billUser.isPayed()) {
                check = false;
                break;
            }
        }
        if(check){
            bill.setPayed(true);
            billRepository.save(bill);
        }
    }

    @Override
    public ResponseEntity<?> createBillByRandomYear(long houseId){

        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int year = date.getYear();
        if (!checkIfBillMonthExist(houseId, month, year)){
            return ResponseEntity.ok(new MessageResponse("Er is nog geen rekening voor deze maand"));
        }
        if (!checkIfAllDeclarationAreCorrect(houseId,month,year)){
            return ResponseEntity.ok(new MessageResponse("Niet alle declaraties zijn gekeurd"));
        }
        Bill bill = billRepository.findByHouseIdAndMonthAndYear(houseId,month,year);
        List<Declaration> declarations = new ArrayList<>(declarationRepository.findAllByHouseIdAndMonthAndYear(houseId,month,year));
        if (bill != null) {
            int random = (int) (Math.random() * 9999 + 1);
            if (!declarations.isEmpty()) {
                for (Declaration declaration : declarations) {
                    declaration.setYear(random);
                    declarationRepository.save(declaration);
                }
                bill.setYear(random);
                billRepository.save(bill);
                return ResponseEntity.ok(new MessageResponse("Jaar verandert"));
            }
        }
        return ResponseEntity.ok(new MessageResponse("Er is nog geen rekening voor deze maand"));
    }
}
