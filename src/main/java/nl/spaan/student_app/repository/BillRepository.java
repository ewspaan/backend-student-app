package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Bill;
import nl.spaan.student_app.model.BillHouse;
import nl.spaan.student_app.model.House;
import nl.spaan.student_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

}
