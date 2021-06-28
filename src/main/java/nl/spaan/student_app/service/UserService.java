package nl.spaan.student_app.service;


import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.UpdateUserRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;


@Service
@Validated
public interface UserService {

    ResponseEntity<?> getAllRoommates(String token);
    ResponseEntity<?> updateUserById(String token,  @Valid UpdateUserRequest userRequest);
    ResponseEntity<?> getUserByToken(String token);
    ResponseEntity<?> addUserToHouse(String token, AddRequest addRequest);
    ResponseEntity<?> getUser(String authorization);

    User findUserNameFromToken(String token);


    ResponseEntity<?> deleteUser(String username);

    ResponseEntity<?> promoteUser(String username);
}