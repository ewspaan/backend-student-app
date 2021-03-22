package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Bill;
import nl.spaan.student_app.model.BillUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {


    Collection<? extends Bill> findByHouseId(long houseId);

    Bill findByHouseIdAndMonthAndYear(long id, int month, int year);
}
