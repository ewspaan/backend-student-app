package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Account;
import nl.spaan.student_app.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account , Long> {

    

    boolean existsByHouseId(Long id);

    Account findByHouseId(long id);

}
