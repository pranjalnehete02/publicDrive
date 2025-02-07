package com.example.demo;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")  // All endpoints under /api path
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Allow requests from all origins
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // 🔥 Upload file
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileMetadata fileMetadata = fileService.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileMetadata);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    // 🔥 Download file
    @GetMapping("/files/download/{sharingToken}")
    public ResponseEntity<?> downloadFile(@PathVariable String sharingToken) {
        try {
            Path filePath = fileService.downloadFile(sharingToken);
            File file = filePath.toFile();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file: " + e.getMessage());
        }
    }

//    @GetMapping("/files/view/{sharingToken}")
//    public ResponseEntity<String> viewFileAsHtml(@PathVariable String sharingToken) {
//        try {
//            // Fetch file content using the sharingToken
//            String htmlContent = fileService.convertFileToHtml(sharingToken);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
//                    .body(htmlContent);
//        } catch (IllegalStateException e) {
//            // If the file is not found or expired, return a 404 error
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>File not found or expired</h1>");
//        }
//    }
    @GetMapping("/files/view/{id}")
    public ResponseEntity<String> viewFileAsHtml(@PathVariable Long id) {
        try {
            // Fetch file content using the id
            String htmlContent = fileService.convertFileToHtml(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(htmlContent);
        } catch (IllegalStateException e) {
            // If the file is not found or expired, return a 404 error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>File not found or expired</h1>");
        }
    }



    // 🔥 Get all files in required format
    @GetMapping("/files")
    public ResponseEntity<List<Map<String, Object>>> getAllFiles() {
        List<Map<String, Object>> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    // 🔥 Editable File View Page
    @GetMapping("/files/edit/{sharingToken}")
    public ResponseEntity<String> editFile(@PathVariable String sharingToken) {
        try {
            String content = fileService.getFileContent(sharingToken);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body("<html><head><title>Edit File</title></head><body>" +
                            "<h2>Edit File</h2>" +
                            "<textarea id='fileContent' rows='10' cols='50'>" + content + "</textarea><br>" +
                            "<button onclick='saveChanges()'>Save Changes</button>" +
                            "<script>" +
                            "function saveChanges() {" +
                            "  const updatedContent = document.getElementById('fileContent').value;" +
                            "  fetch('/api/files/update/" + sharingToken + "', {" +
                            "    method: 'POST'," +
                            "    headers: { 'Content-Type': 'application/json' }," +
                            "    body: JSON.stringify({ content: updatedContent })" +
                            "  })" +
                            "  .then(response => response.text())" +
                            "  .then(data => { alert(data); window.location.reload(); })" +
                            "  .catch(error => console.error('Error:', error));" +
                            "}" +
                            "</script>" +
                            "</body></html>");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>File not found or expired</h1>");
        }
    }

    // 🔥 Save Edited File Content
    @PostMapping("/files/update/{sharingToken}")
    public ResponseEntity<String> updateFile(@PathVariable String sharingToken, @RequestBody Map<String, String> request) {
        try {
            fileService.updateFile(sharingToken, request.get("content"));
            return ResponseEntity.ok("File updated successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or expired");
        }
    }

    @PostMapping("/files/share")
    public ResponseEntity<?> shareFile(@RequestParam("sharingToken") String sharingToken,
                                       @RequestParam("email") String email,
                                       @RequestParam("editable") boolean editable) {
        try {
            fileService.shareFile(sharingToken, email, editable);
            return ResponseEntity.ok("File shared successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sharing file: " + e.getMessage());
        }
    }


// // 🔥 Share file with another user by user ID
//    @PostMapping("/files/share-with-user")
//    public ResponseEntity<?> shareFileWithUser(@RequestParam("fileId") Long fileId,
//                                               @RequestParam("sharedWithUserId") Long sharedWithUserId,
//                                               @RequestParam("accessLevel") String accessLevel) {
//        try {
//            fileService.shareFileWithUser(fileId, sharedWithUserId, accessLevel);
//            return ResponseEntity.ok("File shared successfully with user ID: " + sharedWithUserId);
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sharing file: " + e.getMessage());
//        }
//    }

    @PostMapping("/files/share-with-user")
    public ResponseEntity<?> shareFileWithUser(
            @RequestParam("fileId") Long fileId,
            @RequestParam("sharedWithUserId") Long sharedWithUserId,
            @RequestParam("accessLevel") String accessLevel) {

        try {
        	System.out.println("fileId: " + fileId);
        	System.out.println("sharedWithUserId: " + sharedWithUserId);
        	System.out.println("accessLevel: " + accessLevel);

            // Share the file with the user
            fileService.shareFileWithUser(fileId, sharedWithUserId, accessLevel);
            return ResponseEntity.ok("File shared successfully with user ID: " + sharedWithUserId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sharing file: " + e.getMessage());
        }
    }


    // 🔥 Get all files shared with a user
    @GetMapping("/files/shared-with-me/{userId}")
    public ResponseEntity<List<FileMetadata>> getSharedFilesForUser(@PathVariable Long userId) {
        try {
            List<FileMetadata> sharedFiles = fileService.getSharedFilesForUser(userId);
            return ResponseEntity.ok(sharedFiles);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
