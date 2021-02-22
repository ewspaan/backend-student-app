package nl.spaan.student_app.controller;


import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.UpdateUserRequest;
import nl.spaan.student_app.payload.response.MessageResponse;
import nl.spaan.student_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/users")
public class UserController {


    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> updateUser(@RequestHeader Map<String, String> headers,
                                        @RequestBody UpdateUserRequest updateRequest) {
        return userService.updateUserById(headers.get("authorization"), updateRequest);
    }


    @GetMapping("/jwtlogin")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> findUserByToken(@RequestHeader Map<String, String> headers) {
       System.out.println("UserController--> "+ headers);
       return userService.findUserByToken(headers.get("authorization"));
    }


    @PostMapping( "/roommate")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> findUserByToken(@RequestHeader Map<String, String> headers,
                                            @RequestBody AddRequest addRequest) {
        return userService.addUserToHouse(headers.get("authorization"), addRequest);
    }
}
