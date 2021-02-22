package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

}
