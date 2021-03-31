package nl.spaan.student_app.controller;

import nl.spaan.student_app.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/bills")
public class BillController {

    private BillService billService;

    @Autowired
    public void setBillService(BillService billService) {
        this.billService = billService;
    }


    @PutMapping("/payed/{billId}")
    @PreAuthorize("hasRole('MODERATOR')")
    ResponseEntity<?> togglePayedBill(@PathVariable("billId") long billId){
        return billService.togglePayed(billId);
    }

    @PutMapping("/create/{houseId}")
    @PreAuthorize("hasRole('MODERATOR')")
    ResponseEntity<?> createBill(@PathVariable("houseId") long houseId){
        return billService.createBillByRandomYear(houseId);
    }

    @GetMapping("/house/{houseId}")
    @PreAuthorize("hasRole('MODERATOR')")
    ResponseEntity<?> getHouseBill(@PathVariable("houseId") long houseId){
        return billService.getHouseBill(houseId);
    }

    @GetMapping("/personal/{payed}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    ResponseEntity<?> getHouseBillUser(@RequestHeader Map<String, String> headers,
                                       @PathVariable("payed") boolean payed){
        return billService.getHouseBillUser(headers.get("authorization"), payed);
    }

}
