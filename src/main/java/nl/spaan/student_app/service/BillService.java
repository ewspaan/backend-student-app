package nl.spaan.student_app.service;

import nl.spaan.student_app.model.Bill;
import nl.spaan.student_app.model.Declaration;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.BillRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BillService {


    void createBill(long houseId, int month, int year);

    void updateBillWhenDeclarationsChange(long id, int month, int year);

    void updateBillWhenAccountChange(long houseId, int month, int year);

    ResponseEntity<?> getHouseBill(long houseId);

    boolean checkIfBillMonthExist(long houseId, int month, int year);

    ResponseEntity<?> togglePayed(long billId);

    ResponseEntity<?> getHouseBillUser(String token, boolean payed);

    ResponseEntity<?> createBillByRandomYear(long houseId);
}
