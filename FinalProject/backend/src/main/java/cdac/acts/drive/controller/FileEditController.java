package cdac.acts.drive.controller;

import cdac.acts.drive.entity.FileStorage;
import cdac.acts.drive.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/files")
public class FileEditController {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    // Fetch file content if it's a text file
    @GetMapping("/edit/{fid}")
    public ResponseEntity<?> getFileContent(@PathVariable("fid") Long fid) {
        Optional<FileStorage> fileStorageOptional = fileStorageRepository.findById(fid);

        if (fileStorageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        FileStorage fileStorage = fileStorageOptional.get();
        String fileName = fileStorage.getFileName();
        String filePathStr = fileStorage.getFilePath();

        if (fileName == null || !fileName.endsWith(".txt")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only text files can be edited");
        }

        if (filePathStr == null || filePathStr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File path is missing");
        }

        Path filePath = Paths.get(filePathStr);

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File does not exist");
        }

        if (!Files.isReadable(filePath)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("File is not readable");
        }

        try {
            String content = Files.readString(filePath);
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file");
        }
    }

    // Save edited content and replace the original file
    @PutMapping("/edit/{fid}")
    public ResponseEntity<String> saveEditedFile(@PathVariable("fid") Long fid, @RequestBody String newContent) {
        Optional<FileStorage> fileStorageOptional = fileStorageRepository.findById(fid);

        if (fileStorageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        FileStorage fileStorage = fileStorageOptional.get();
        String fileName = fileStorage.getFileName();
        String filePathStr = fileStorage.getFilePath();

        if (fileName == null || !fileName.endsWith(".txt")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only text files can be edited");
        }

        if (filePathStr == null || filePathStr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File path is missing");
        }

        Path filePath = Paths.get(filePathStr);

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File does not exist");
        }

        if (!Files.isWritable(filePath)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("File is not writable");
        }

        try {
            Files.writeString(filePath, newContent);
            return ResponseEntity.ok("File updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving file");
        }
    }
}
