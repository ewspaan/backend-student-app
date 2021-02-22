package nl.spaan.student_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.spaan.student_app.model.House;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.UpdateUserRequest;
import nl.spaan.student_app.payload.response.JwtInlogResponse;
import nl.spaan.student_app.payload.response.MessageResponse;
import nl.spaan.student_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spaan.sec.jwtSecret}")
    private String jwtSecret;

    private static final String PREFIX = "Bearer ";

    private UserRepository userRepository;
    private PasswordEncoder encoder;

    @Override
    public ResponseEntity<?> getAllUsers() {

        List<User> users = userRepository.findAll();

        if(users.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("No Users found!"));
        }
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<?> updateUserById(String token, UpdateUserRequest userRequest) {
        if(token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
        String username =  getUsernameFromToken(token);

        if(userExists(username) && updateRequestIsValid(userRequest)) {
            User updatedUser = findUserByUsername(username);
            if(!userRequest.getPassword().isEmpty() && !userRequest.getRepeatedPassword().isEmpty()) {
                updatedUser.setPassword(encoder.encode(userRequest.getPassword()));
            }
            if(userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
                updatedUser.setEmail(userRequest.getEmail());
            }
            return ResponseEntity.ok().body(userRepository.save(updatedUser));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("User cannot be updated with provided data."));
    }

    @Override
    public ResponseEntity<?> findUserByToken(String token) {
        String username = getUsernameFromToken(token);

        if(userExists(username)) {
            System.out.println("USI FUBT 3--> " + ResponseEntity.ok(findUserByUsername(username)));
            User user = findUserByUsername(username);
            List<String> roles = user.getRoles().stream()
                    .map(role -> (role.getName().name()))
                    .collect(Collectors.toList());
            String role = roles.get(0);
            JwtInlogResponse jwtInlogResponse = new JwtInlogResponse(
                                                user.getId(),
                                                user.getUsername(),
                                                user.getFirstName(),
                                                user.getEmail(),
                                                role);
            return ResponseEntity.ok(jwtInlogResponse);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
    }


    // Voeg huisgenoot toe aan huis en genereer email met link naar signup pagine huisgenoot
    @Override
    public ResponseEntity<?> addUserToHouse( String token, AddRequest addRequest) {
        //TODO email sturen bij al in gebruik zijnde email
        if(token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
       if (Boolean.TRUE.equals(userRepository.existsByEmail(addRequest.getEmail()))) {
            System.out.println(addRequest.getEmail());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username of Email is al in gebruik"));
        }
        //maak nieuwe user
        User newUser = new User();
        newUser.setFirstName(addRequest.getFirstName());
        newUser.setLastName(addRequest.getLastName());
        newUser.setEmail(addRequest.getEmail());

        String username = getUsernameFromToken(token);
        User user = findUserByUsername(username);

        House house = user.getHouse();

        newUser.setHouse(house);
        userRepository.save(newUser);

        new EmailService("smtp.mailtrap.io",
                2525, "3bfcf4b63b722e",
                "4ded572b74701c",
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail(),
                house.getId());


        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private String getUsernameFromToken(String token) {
        String tokenWithoutBearer = removePrefix(token);

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                .parseClaimsJws(tokenWithoutBearer).getBody();

        return claims.getSubject();
    }

    private String removePrefix(String token) {
        return token.replace(PREFIX, "");
    }

    private boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean updateRequestIsValid(UpdateUserRequest updateUserRequest) {
        if(updateUserRequest.getPassword().equals(updateUserRequest.getRepeatedPassword())) {
            return true;
        }
        return false;
    }

    private User findUserByUsername(String username) {

        System.out.println("USI fubun--> " + userRepository.findByUsername(username).get().getUsername());
        return userRepository.findByUsername(username).get();
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

}
