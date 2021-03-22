package nl.spaan.student_app.service;

import nl.spaan.student_app.model.Account;
import nl.spaan.student_app.payload.request.UpdateAccountRequest;
import nl.spaan.student_app.payload.response.AccountResponse;
import nl.spaan.student_app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountServiceImpl implements AccountService {

    private UserService userService;

    private BillService billService;

    private AccountRepository accountRepository;

    @Override
    public ResponseEntity<?> updateAccount(String token , UpdateAccountRequest updateAccountRequest){

        long id = userService.findUserNameFromToken(token).getHouse().getId();
        if (accountRepository.existsByHouseId(id)) {
            Account account = accountRepository.findByHouseId(id);

            if (updateAccountRequest.getAccountNumber() != null && !updateAccountRequest.getAccountNumber().isEmpty()) {
                account.setAccountNumber(updateAccountRequest.getAccountNumber());
            }
            if (updateAccountRequest.getElektraUtility() > 0 ) {
                account.setElektraUtility(updateAccountRequest.getElektraUtility());
            }
            if (updateAccountRequest.getGasUtility() > 0 ) {
                account.setGasUtility(updateAccountRequest.getGasUtility());
            }
            if (updateAccountRequest.getWaterUtility() > 0 ) {
                account.setWaterUtility(updateAccountRequest.getWaterUtility());
            }
            if (updateAccountRequest.getInternetUtility() > 0 ) {
                account.setInternetUtility(updateAccountRequest.getInternetUtility());
            }
            account.setTotalAmountUtilities(addUtilities(account));
            accountRepository.save(account);
            LocalDate date = LocalDate.now();
            int month = date.getMonthValue();
            int year = date.getYear();
            billService.updateBillHouse(id,month,year);
            return ResponseEntity.ok("Account succesvol geüpdatet");
        }
        Account newAccount = new Account(updateAccountRequest.getAccountNumber(),
                updateAccountRequest.getWaterUtility(),
                updateAccountRequest.getGasUtility(),
                updateAccountRequest.getElektraUtility(),
                updateAccountRequest.getInternetUtility(),
                userService.findUserNameFromToken(token).getHouse());
        newAccount.setTotalAmountUtilities(addUtilities(newAccount));

        accountRepository.save(newAccount);

        return ResponseEntity.ok("Account succesvol aan gemaakt");
    }

    @Override
    public ResponseEntity<?> getAccount(String token) {

        Long id = userService.findUserNameFromToken(token).getHouse().getId();
        if (accountRepository.existsByHouseId(id)) {
            Account newAccount = userService.findUserNameFromToken(token).getHouse().getAccount();
            AccountResponse accountResponse = new AccountResponse(newAccount.getAccountNumber(),
                    newAccount.getWaterUtility(),
                    newAccount.getGasUtility(),
                    newAccount.getElektraUtility(),
                    newAccount.getInternetUtility());
            return ResponseEntity.ok(accountResponse);
        }

        return ResponseEntity.ok("Huisrekening bestaat nog niet");
    }
    private double addUtilities(Account account){
        return account.getElektraUtility()+
                            account.getGasUtility()+
                            account.getWaterUtility()+
                            account.getInternetUtility();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setBillService(BillService billService) {
        this.billService = billService;
    }
}
