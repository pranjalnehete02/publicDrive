package cdac.acts.drive.controller;
import cdac.acts.drive.dto.FileFolderDTO;
import cdac.acts.drive.entity.FileStorage;
import cdac.acts.drive.entity.Folder;
import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.FileStorageRepository;
import cdac.acts.drive.repository.FolderRepository;
import cdac.acts.drive.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Value("${file.storage.path}")
    private String storagePath;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(storagePath);
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
            }

            // Step 1: Find the user by username
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            User user = userOpt.get();

            // Step 2: Find the user's root folder (where parentFolder is null)
            Optional<Folder> rootFolderOpt = folderRepository.findByUserIdAndParentFolderIsNull(user.getId());
            if (rootFolderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Root folder not found for user");
            }
            Folder rootFolder = rootFolderOpt.get();

            // Step 3: Save the file to the system (using user's folder)
            String fileName = file.getOriginalFilename();
            Path filePath = rootLocation.resolve(user.getId().toString()).resolve(fileName);
            Files.createDirectories(filePath.getParent());  // Create directories if they don't exist
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Step 4: Save the file metadata in the database (FileStorage)
            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(fileName);
            fileStorage.setFilePath(filePath.toString());
            fileStorage.setFolder(rootFolder); // Link to the root folder
            fileStorageRepository.save(fileStorage);

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
        }
    }


    @PostMapping("/files")
    public ResponseEntity<List<FileFolderDTO>> listFiles(@RequestParam("username") String username) {
        try {
            // Step 1: Find the user by username
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            User user = userOpt.get();

            // Step 2: Find the user's root folder (where parentFolder is null)
            Optional<Folder> rootFolderOpt = folderRepository.findByUserIdAndParentFolderIsNull(user.getId());
            if (rootFolderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            Folder rootFolder = rootFolderOpt.get();

            // Step 3: List files and folders in the root folder
            List<FileFolderDTO> fileFolderDTOs = listFilesInFolder(rootFolder);

            return ResponseEntity.ok(fileFolderDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    private List<FileFolderDTO> listFilesInFolder(Folder rootFolder) {
        List<FileFolderDTO> fileFolderDTOs = new ArrayList<>();

        // Step 4: Find all files that belong to the rootFolder
        List<FileStorage> filesInFolder = fileStorageRepository.findByFolderId(rootFolder.getId());
        if (filesInFolder != null && !filesInFolder.isEmpty()) {
            for (FileStorage file : filesInFolder) {
                // Determine if the item is a file or folder
                boolean isFolder = false;  // We only deal with files for now
                Long parentFolderId = (rootFolder.getParentFolder() != null) ? rootFolder.getParentFolder().getId() : null;

                // Create a DTO for each file in the folder
                fileFolderDTOs.add(new FileFolderDTO(file.getFileName(), parentFolderId, isFolder, rootFolder.getUser().getUsername(), file.getId()));
            }
        }

        // Step 5: Find all subfolders that belong to the rootFolder
        List<Folder> subFolders = folderRepository.findByParentFolderId(rootFolder.getId());
        if (subFolders != null && !subFolders.isEmpty()) {
            for (Folder subFolder : subFolders) {
                Long parentFolderId = rootFolder.getId();

                // Add the subfolder details to the response list
                fileFolderDTOs.add(new FileFolderDTO(subFolder.getFolderName(), parentFolderId, true, rootFolder.getUser().getUsername(), subFolder.getId()));
            }
        }

        return fileFolderDTOs;
    }


//        return fileFolderDTOs;
//    }

    @GetMapping("/download/{fid}")
    @Operation(summary = "Get files by ID", description = "Fetch a files using its ID")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fid") Long fid) {
        // Step 1: Find the file in the database by fid
        Optional<FileStorage> fileStorageOptional = fileStorageRepository.findById(fid);

        if (fileStorageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // If the file does not exist
        }

        FileStorage fileStorage = fileStorageOptional.get();
        // Step 2: Get the file path from the database (Assuming filePath is stored in DB)
        Path filePath = Paths.get(fileStorage.getFilePath());

        // Step 3: Check if the file exists
        File file = filePath.toFile();
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // If the file doesn't exist on disk
        }

        try {
            // Step 4: Read the file content into a byte array
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Step 5: Set the content type (for example, application/pdf for PDF files)
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback if unable to determine the file type
            }

            // Step 6: Set the content-disposition header for download with the filename
            String fileName = fileStorage.getFileName(); // Get file name from the database

            // Return the file as byte array, along with headers to indicate download
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Error reading file
        }
    }




}
