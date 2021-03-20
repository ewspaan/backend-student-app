package nl.spaan.student_app.service;

import nl.spaan.student_app.model.Bill;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.BillRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BillService {

    Bill createBill(long HouseId);

    ResponseEntity<?> getHouseBill(long houseId);
}
