package nl.spaan.student_app.service;

import nl.spaan.student_app.exeption.DatabaseErrorException;
import nl.spaan.student_app.exeption.RecordNotFoundException;
import nl.spaan.student_app.model.Client;
import nl.spaan.student_app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImp implements ClientService{

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public long saveClient(Client client) {
        Client existingClient = clientRepository.findByEmail(client.getEmail());
        System.out.println("Client--> " + client.getEmail());
        if(existingClient != null && client.getEmail().equals(existingClient.getEmail())){
            System.out.println("client bestaat al");
            throw new DatabaseErrorException();
        }
        else {
            Client newClient = clientRepository.save(client);
            return newClient.getId();
        }
    }

    @Override
    public void deleteClient(long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        }
        else{
            throw new RecordNotFoundException();
        }
    }

    @Override
    public Client getClientById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client getClientByEmail(String email) {
      Client existingClient = clientRepository.findByEmail(email);
      return existingClient;
    }

    @Override
    public Optional<Client> getClient(String email) {
        return Optional.empty();
    }


}

