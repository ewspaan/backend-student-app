package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

}
