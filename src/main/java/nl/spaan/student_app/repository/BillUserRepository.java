package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.BillUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BillUserRepository extends JpaRepository<BillUser, Long> {


    BillUser findByUserIdAndBillId(long id, long id1);

    List<BillUser> findAllByBillId(long billId);
}
