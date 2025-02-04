package cdac.acts.drive.controller;

import cdac.acts.drive.entity.Folder;
import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.FolderRepository;
import cdac.acts.drive.repository.UserRepository;
import cdac.acts.drive.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/ap/user")
public class UserController {

    private final Path rootLocation = Paths.get("C:/Users/adity/OneDrive/Desktop/Project CDAC/FIleDataBase");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private RegisterService userService;

    @PostMapping("/add-user")
    public String addUser(@RequestBody User user) {
        userService.saveUser(user);
        return "User added successfully!";
    }
    // POST method to register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {

            user.setPassword(user.getPassword());
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

            return ResponseEntity.status(201).body("User registered successfully, folder created, and root folder added.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while registering the user.");
        }
    }
}
