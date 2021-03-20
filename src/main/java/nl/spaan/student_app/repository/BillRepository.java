package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {


    Collection<? extends Bill> findByHouseId(long houseId);
}
