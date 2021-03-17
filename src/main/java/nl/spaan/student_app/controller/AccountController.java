package nl.spaan.student_app.controller;

import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.UpdateAccountRequest;
import nl.spaan.student_app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('MODERATOR')")
    ResponseEntity<?> updateAccount(@RequestHeader Map<String, String> headers,
                                    @RequestBody UpdateAccountRequest updateAccountRequest){
        return accountService.updateAccount(headers.get("authorization"), updateAccountRequest);
    }

    @GetMapping("/download")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    ResponseEntity<?> getAccount(@RequestHeader Map<String, String> headers){
        return accountService.getAccount(headers.get("authorization"));
    }

}
