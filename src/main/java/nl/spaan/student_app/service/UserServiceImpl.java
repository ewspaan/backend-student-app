package nl.spaan.student_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.spaan.student_app.model.House;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.UpdateUserRequest;
import nl.spaan.student_app.payload.response.UserResponse;
import nl.spaan.student_app.payload.response.MessageResponse;
import nl.spaan.student_app.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private static final String PREFIX = "Bearer ";

    @Value("${spaan.sec.jwtSecret}")
    private String jwtSecret;

    private EmailService emailService;

    private UserRepository userRepository;

    private PasswordEncoder encoder;

    @Override
    public ResponseEntity<?> getAllRoommates(String token) {

        User user = findUserNameFromToken(token);
        List<User> users = userRepository.findAllByHouseId(user.getHouse().getId());

        if(users.size() == 1) {
            return ResponseEntity.ok().body(new MessageResponse("Geen huisgenoten gevonden"));
        }
        List<UserResponse> roommates = new ArrayList<>();
        for (User value : users) {
            if(!value.getUsername().equals(user.getUsername())) {
                UserResponse userResponse = new UserResponse();
                userResponse.setUsername(value.getUsername());
                userResponse.setFirstName(value.getFirstName());
                userResponse.setLastName(value.getLastName());
                roommates.add(userResponse);
            }
        }
        return ResponseEntity.ok(roommates);
    }

    @Override
    public ResponseEntity<?> updateUserById(String token, UpdateUserRequest userRequest) {
        if(token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
        String username =  getUsernameFromToken(token);

        if(userExists(username) && updateRequestIsValid(userRequest)) {

            User updatedUser = findUserByUsername(username);

            if(userRequest.getFirstName() != null && !userRequest.getFirstName().isEmpty()) {
                updatedUser.setFirstName(userRequest.getFirstName());
            }
            if(userRequest.getLastName() != null && !userRequest.getLastName().isEmpty()) {
                updatedUser.setLastName(userRequest.getLastName());
            }

            if(userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
                if(userRepository.existsByEmail(userRequest.getEmail())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is al in gebruik");
                }
                updatedUser.setEmail(userRequest.getEmail());
            }
            if(userRequest.getDateOfBirth() != null && !userRequest.getDateOfBirth().isEmpty()) {
                updatedUser.setDateOfBirth(userRequest.getDateOfBirth());
            }
            if(userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                updatedUser.setPassword(encoder.encode(userRequest.getPassword()));
            }
            //voorlopig is het niet mogelijk om username te updaten ivm met jwt token
//            if(userRequest.getUsername() != null && !userRequest.getUsername().isEmpty()) {
//                if(!userRepository.existsByUsername(userRequest.getUsername())){
//                    return ResponseEntity.badRequest().body(new MessageResponse("User cannot be updated with provided data."));
//                }
//            }
            userRepository.save(updatedUser);
            return ResponseEntity.ok().body("User succesvol geupdate");
        }
        return ResponseEntity.badRequest().body(new MessageResponse("User cannot be updated with provided data."));
    }

    //autoriseer User met behulp van token
    @Override
    public ResponseEntity<?> getUserByToken(String token) {
        String username = getUsernameFromToken(token);

        if(userExists(username)) {
            User user = findUserByUsername(username);
            UserResponse jwtInlogResponse = createCommonResponse(user);
            return ResponseEntity.ok(jwtInlogResponse);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
    }

    @Override
    public ResponseEntity<?> getUser(String token){
        User user = findUserNameFromToken(token);

        UserResponse profileResponse = createCommonResponse(user);

        House house = user.getHouse();

        String houseName = house.getHouseName();

        profileResponse.setHouseName(Objects.requireNonNullElse(houseName, "Huis heeft nog geen naam"));

        return ResponseEntity.ok(profileResponse);
    }


    // Voeg huisgenoot toe aan huis en genereer email met link naar signup pagina huisgenoot
    @Override
    public ResponseEntity<?> addUserToHouse( String token, AddRequest addRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByHouseIdAndEmail(findUserNameFromToken(token).getHouse().getId(),addRequest.getEmail()))) {
            //return ResponseEntity.badRequest().body(new MessageResponse("Email is al toegevoegd aan huis"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is al in gebruik");
        }

        //maak nieuwe user
        User newUser = new User();
        if(addRequest.getFirstName().isEmpty()){
            newUser.setFirstName("Nieuwe huisgenoot");
        }else {
            newUser.setFirstName(addRequest.getFirstName());
        }
        if (addRequest.getLastName().isEmpty()){
            newUser.setLastName(addRequest.getEmail());
        }else {
            newUser.setLastName(addRequest.getLastName());
        }
        newUser.setUsername(createRandomUsername(addRequest.getEmail()));
        newUser.setEmail(addRequest.getEmail());
        newUser.setHouse(findUserNameFromToken(token).getHouse());
        userRepository.save(newUser);

        emailService.sendMail(newUser);

        return ResponseEntity.ok(new MessageResponse("Huisgenoot succesvol toegevoegd"));
    }
    @Override
    public ResponseEntity<?> deleteUser(String username){

        if (userExists(username)){
            User user = findUserByUsername(username);
            userRepository.deleteById(user.getId());
            return ResponseEntity.ok("Huisgenoot verwijdert");
        }
        return ResponseEntity.badRequest().body("Huisgenoot niet gevonden");
    }
    private String createRandomUsername(String email){


        email = email.replace("@","");
        if (Boolean.FALSE.equals(userRepository.existsByUsername(email))) {
            return email;
        }else {
            int length = 5;
            String generatedString = RandomStringUtils.random(length, false, true);
            email += generatedString;
        }
        return email;
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
        //als beide password velden leeg zijn hoeft het password niet veranderd te worden.
        if(updateUserRequest.getPasswordRepeat() == null && updateUserRequest.getPassword() == null) {
            return true;
        }
        if (updateUserRequest.getPassword().equals(updateUserRequest.getPasswordRepeat())) {
            return true;
        }
        return false;
    }
    @Override
    public User findUserNameFromToken(String token) {
        String username = getUsernameFromToken(token);
        User user = new User();
        if(userExists(username)) {
            user = findUserByUsername(username);
        }
        return(user);
    }


    private User findUserByUsername(String username) {

        return userRepository.findByUsername(username).get();
    }

    private UserResponse createCommonResponse(User user){

            List<String> roles = user.getRoles().stream()
                    .map(role -> (role.getName().name()))
                    .collect(Collectors.toList());
            String role = roles.get(0);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setEmail(user.getEmail());
            userResponse.setDateOfBirth(user.getDateOfBirth());
            userResponse.setRoles(role);
            userResponse.setHouseId(user.getHouse().getId());
            userResponse.setHouseName(user.getHouse().getHouseName());
            userResponse.setAccountNumber(user.getHouse().getAccount().getAccountNumber());
            userResponse.setWaterUtility(user.getHouse().getAccount().getWaterUtility());
            userResponse.setGasUtility(user.getHouse().getAccount().getGasUtility());
            userResponse.setElektraUtility(user.getHouse().getAccount().getElektraUtility());
            userResponse.setInternetUtility(user.getHouse().getAccount().getInternetUtility());
            return userResponse;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
