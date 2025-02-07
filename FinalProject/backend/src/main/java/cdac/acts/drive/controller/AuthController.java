package cdac.acts.drive.controller;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

import cdac.acts.drive.entity.AuthResponse;
import cdac.acts.drive.entity.Folder;
import cdac.acts.drive.entity.LoginRequest;
import cdac.acts.drive.entity.User;
import cdac.acts.drive.filter.CustomJWTTokenService;
import cdac.acts.drive.repository.FolderRepository;
import cdac.acts.drive.repository.UserRepository;
import cdac.acts.drive.service.RegisterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Api", description = "For Authentication of users")
public class AuthController {

    @Value("${file.storage.path}")
    String storagePath;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(storagePath);
    }

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private CustomJWTTokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // Store active tokens per username
    ConcurrentHashMap<String, String> activeTokens = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        String token = tokenService.generateToken(authentication.getName());
        logger.info("generating token");
        logger.info("logging in user");
        return ResponseEntity.ok(new AuthResponse(token));
    }
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody User user) {
//        userService.saveUser(user);
//        return ResponseEntity.ok("user added successfully");
//    }
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        try {

            user.setPassword(user.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(user.getRole()==null){
                user.setRole("ROLE_USER");
            }
            User savedUser = userRepository.save(user);
            Path userFolderPath = rootLocation.resolve(String.valueOf(savedUser.getId()));
            File userFolder = new File(userFolderPath.toString());

            if (!userFolder.exists()) {
                boolean folderCreated = userFolder.mkdir(); // Create the directory

                if (!folderCreated) {
                    return ResponseEntity.status(500).body("Failed to create user folder.");
                }
            }

            Folder rootFolder = new Folder();
            rootFolder.setFolderName("Root Folder");
            rootFolder.setUser(savedUser);
            rootFolder.setParentFolder(null);
            folderRepository.save(rootFolder);
            logger.info("signing up user");
            return ResponseEntity.status(201).body("User registered successfully, folder created, and root folder added.");
        } catch (Exception e) {
            logger.error("Error in signup {}",user.getUsername(),e);
            return ResponseEntity.status(500).body("An error occurred while registering the user.");
        }
    }
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    //     String username = loginRequest.getUsername();

    //     // Check if there is an active token for this username
    //     if (activeTokens.containsKey(username)) {
    //         String existingToken = activeTokens.get(username);
    //         // Validate existing token
    //         if (tokenService.validateToken(existingToken)) {
    //             return ResponseEntity.badRequest().body("Only one active session is allowed for this user.");
    //         }
    //     }

    //     // Authenticate user credentials
    //     UsernamePasswordAuthenticationToken authToken =
    //             new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword());
    //     Authentication authentication = authenticationManager.authenticate(authToken);

    //     // Generate new token and store it
    //     String newToken = tokenService.generateToken(username);
    //     activeTokens.put(username, newToken);

    //     return ResponseEntity.ok(new AuthResponse(newToken));
    // }

    // Optional: Method to clear token on logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest loginRequest) {
        activeTokens.remove(loginRequest.getUsername());
        logger.info("Logging of user {}",loginRequest.getUsername());
        return ResponseEntity.ok("Logged out successfully.");
    }
}


