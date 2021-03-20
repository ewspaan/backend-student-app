package nl.spaan.student_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.spaan.student_app.model.House;
import nl.spaan.student_app.model.User;
import nl.spaan.student_app.payload.request.AddRequest;
import nl.spaan.student_app.payload.request.UpdateUserRequest;
import nl.spaan.student_app.payload.response.DeclarationResponse;
import nl.spaan.student_app.payload.response.UserResponse;
import nl.spaan.student_app.payload.response.MessageResponse;
import nl.spaan.student_app.repository.HouseRepository;
import nl.spaan.student_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spaan.sec.jwtSecret}")
    private String jwtSecret;

    @Value("${spaan.sec.emailHost}")
    private String emailHost;

    @Value("${spaan.sec.emailPort}")
    private int emailPort;

    @Value("${spaan.sec.emailUserName}")
    private String emailUserName;

    @Value("${spaan.sec.emailPassword}")
    private String emailPassword;

    private static final String PREFIX = "Bearer ";

    private UserRepository userRepository;
    private HouseRepository houseRepository;
    private PasswordEncoder encoder;

    @Override
    public ResponseEntity<?> getAllRoommates(String token) {

        User user = findUserNameFromToken(token);
        System.out.println("bla1234");

        List<User> users = userRepository.findAllByHouseId(user.getHouse().getId());

        if(users.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Geen huisgenoten gevonden"));
        }
        System.out.println("Users--> " + users.get(0).getFirstName());
        List<UserResponse> roommates = new ArrayList<>();
        for ( int i = 0; i< users.size(); i++ ){
            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(users.get(i).getUsername());
            userResponse.setFirstName(users.get(i).getFirstName());
            userResponse.setLastName(users.get(i).getLastName());
            roommates.add(userResponse);
        }
        return ResponseEntity.ok(roommates);
    }

    @Override
    public ResponseEntity<?> updateUserById(String token, UpdateUserRequest userRequest) {
        if(token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
        String username =  getUsernameFromToken(token);

        System.out.println("Bla update--> " + username);
        if(userExists(username) && updateRequestIsValid(userRequest)) {
            System.out.println("Bla update-2-> " + username);
            User updatedUser = findUserByUsername(username);


            if(userRequest.getFirstName() != null && !userRequest.getFirstName().isEmpty()) {
                updatedUser.setFirstName(userRequest.getFirstName());
            }
            if(userRequest.getLastName() != null && !userRequest.getLastName().isEmpty()) {
                updatedUser.setLastName(userRequest.getLastName());
            }

            if(userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
                System.out.println("Bla update-3 2-> " + username);
                if(!userRepository.existsByEmail(userRequest.getEmail())){
                    updatedUser.setEmail(userRequest.getEmail());
                    System.out.println("Bla update-email-> ");
                }
            }
            if(userRequest.getDateOfBirth() != null && !userRequest.getDateOfBirth().isEmpty()) {
                updatedUser.setDateOfBirth(userRequest.getDateOfBirth());
            }
            if(userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                updatedUser.setPassword(encoder.encode(userRequest.getPassword()));
            }
            //voorlopig is het niet mogelijk om username te updaten ivm met jwt token
            if(userRequest.getUsername() != null && !userRequest.getUsername().isEmpty()) {
                if(!userRepository.existsByUsername(userRequest.getUsername())){
                    return ResponseEntity.badRequest().body(new MessageResponse("User cannot be updated with provided data."));
                }
            }
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

        System.out.println("profileResponse" + profileResponse.getAccountNumber());
        House house = user.getHouse();

        String houseName = house.getHouseName();

        profileResponse.setHouseName(Objects.requireNonNullElse(houseName, "Huis heeft nog geen naam"));

        return ResponseEntity.ok(profileResponse);
    }


    // Voeg huisgenoot toe aan huis en genereer email met link naar signup pagina huisgenoot
    @Override
    public ResponseEntity<?> addUserToHouse( String token, AddRequest addRequest) {
        //TODO email sturen bij al in gebruik zijnde email
        if(token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
        }
       if (Boolean.TRUE.equals(userRepository.existsByEmail(addRequest.getEmail()))) {
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

        new EmailServiceImpl(
                emailHost,
                emailPort,
                emailUserName,
                emailPassword,
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail(),
                house.getId()
        );
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @Override
    public ResponseEntity<?> deleteUser(String username){

        if (userExists(username)){
            User user = findUserByUsername(username);
            userRepository.deleteById(user.getId());
            System.out.println("delete--> " + username);
        }
        return ResponseEntity.ok("Huisgenoot verwijdert");
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
            System.out.println("User-->  " + user.getUsername());
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
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }
}
