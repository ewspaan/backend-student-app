package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.ERole;
import nl.spaan.student_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

        Optional<Role> findByName(ERole name);

}

