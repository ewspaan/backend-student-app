package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Declaration;
import nl.spaan.student_app.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {


    List<Declaration> findAllByHouseId(long id);
}
