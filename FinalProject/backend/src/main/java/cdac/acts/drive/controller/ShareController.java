package cdac.acts.drive.controller;

import cdac.acts.drive.dto.SharedFileRequestDTO;
import cdac.acts.drive.dto.SharedFileResponseDTO;
import cdac.acts.drive.entity.FileStorage;
import cdac.acts.drive.entity.SharedFile;
import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.FileStorageRepository;
import cdac.acts.drive.repository.SharedFileRepository;
import cdac.acts.drive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/share/files")
public class ShareController {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SharedFileRepository sharedFileRepository;

    @PostMapping("/share")
    public ResponseEntity<String> shareFile(@RequestBody SharedFileRequestDTO requestDTO) {
        // Step 1: Find the file by fileId
        FileStorage file = fileStorageRepository.findById(requestDTO.getFileId())
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Step 2: Find the user who is sharing the file by username (sharedByUsername)
        User sharedByUser = userRepository.findByUsername(requestDTO.getSharedByUsername())
                .orElseThrow(() -> new RuntimeException("User who shared file not found"));

        // Step 3: Find the user to whom the file is being shared (sharedToUser)
        User sharedWithUser = userRepository.findByUsername(requestDTO.getSharedToUser())
                .orElseThrow(() -> new RuntimeException("User to share with not found"));

        // Step 4: Create a new SharedFile entry
        SharedFile sharedFile = new SharedFile();
        sharedFile.setFile(file);
        sharedFile.setSharedByUser(sharedByUser);
        sharedFile.setSharedWithUser(sharedWithUser);
        sharedFile.setPermission(requestDTO.getPermission());

        // Step 5: Save the new SharedFile entry to the database
        sharedFileRepository.save(sharedFile);

        return ResponseEntity.ok("File shared successfully!");
    }


    @GetMapping("/shared-files")
    public ResponseEntity<List<SharedFileResponseDTO>> getSharedFiles(@RequestParam String username) {
        // Step 1: Find the user by username
        User sharedWithUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Find all shared files where the user is the sharedToUser
        List<SharedFile> sharedFiles = sharedFileRepository.findBySharedWithUser(sharedWithUser);

        // Step 3: Convert the list of SharedFile entities into SharedFileResponseDTO
        List<SharedFileResponseDTO> response = sharedFiles.stream()
                .map(sharedFile -> {
                    // Get the file details
                    FileStorage file = sharedFile.getFile();
                    User sharedByUser = sharedFile.getSharedByUser();

                    // Create and populate the response DTO
                    SharedFileResponseDTO dto = new SharedFileResponseDTO();
                    dto.setFileId(file.getId());
                    dto.setFileName(file.getFileName()); // Assuming the file has a fileName field
                    dto.setOwnerOfFile(sharedByUser.getUsername()); // Assuming owner is the user who shared the file
                    dto.setPermission(sharedFile.getPermission().toString()); // Convert the Permission enum to string

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}