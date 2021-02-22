package nl.spaan.student_app.service;


import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.UpdateUserRequest;

import nl.spaan.student_app.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;


@Service
@Validated
public interface UserService {
    ResponseEntity<?> getAllUsers();
    ResponseEntity<?> updateUserById(String token,  @Valid UpdateUserRequest userRequest);
    ResponseEntity<?> findUserByToken(String token);
    ResponseEntity<?> addUserToHouse(String token, AddRequest addRequest);
}