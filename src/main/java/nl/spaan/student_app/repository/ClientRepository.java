package nl.spaan.student_app.repository;

import nl.spaan.student_app.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);


}