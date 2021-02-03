package nl.spaan.student_app.service;

import nl.spaan.student_app.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> getAllClients();
    long saveClient(Client client);
    void deleteClient(long id);
    Client getClientById(long id);
    Client getClientByEmail(String email);
    Optional<Client> getClient(String email);
}
