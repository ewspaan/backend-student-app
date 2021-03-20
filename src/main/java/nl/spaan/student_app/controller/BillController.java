package nl.spaan.student_app.controller;

import nl.spaan.student_app.payload.request.BillRequest;
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

//    @PostMapping("/create")
//    @PreAuthorize("hasRole('MODERATOR')")
//    ResponseEntity<?> createBill(@RequestHeader Map<String, String> headers,
//                                 @RequestBody BillRequest billRequest){
//
//        return billService.createBill(headers.get("authorization"), billRequest);
//    }

    @GetMapping("/{houseId}")
    @PreAuthorize("hasRole('MODERATOR')")
    ResponseEntity<?> getHouseBill(@PathVariable("houseId") long houseId){
        return billService.getHouseBill(houseId);
    }

}
