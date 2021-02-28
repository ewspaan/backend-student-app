package nl.spaan.student_app.service;

import nl.spaan.student_app.payload.request.BillRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BillService {

    ResponseEntity<?> createBill(String authorization, BillRequest billRequest);
}
