package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
