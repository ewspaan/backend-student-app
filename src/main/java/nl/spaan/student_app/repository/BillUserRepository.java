package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.BillUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillUserRepository extends JpaRepository<BillUser, Long> {
}
