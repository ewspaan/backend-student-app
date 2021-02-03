package nl.spaan.student_app.controller;

import nl.spaan.student_app.model.Client;
import nl.spaan.student_app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "")
    public ResponseEntity<Object> getClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping(value = "/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable("id") long id) {
        Client client = clientService.getClientById(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping(value = "/clients")
    public ResponseEntity<Object> saveClient(@RequestBody Client client){
        long newID = clientService.saveClient(client);
        return new ResponseEntity<>(newID, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/clients/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable("id") long id){
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/clients/email/{email}")
    public ResponseEntity<Object> getClientByEmail(@PathVariable("email") String email) {
        Client client = clientService.getClientByEmail(email);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

}