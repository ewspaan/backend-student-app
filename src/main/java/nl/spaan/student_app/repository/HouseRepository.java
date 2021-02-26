package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    House findHouseById(House house);
}
