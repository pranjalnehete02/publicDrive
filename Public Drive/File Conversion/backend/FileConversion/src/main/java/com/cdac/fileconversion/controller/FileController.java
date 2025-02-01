//package com.cdac.fileconversion.controller;
//
//import com.cdac.fileconversion.entity.UploadedFile;
//import com.cdac.fileconversion.repository.FileRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.*;
//import java.nio.file.StandardOpenOption;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//@CrossOrigin(origins = "http://localhost:3001")
//@RestController
//@RequestMapping("/files")
//public class FileController {
//
//    private static final String UPLOAD_DIR = "uploads/";
//
//    @Autowired
//    private FileRepository fileRepository;
//
//    /**
//     * Upload File & Store Path in Database
//     */
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest().body("File is empty!");
//            }
//
//            String filename = StringUtils.cleanPath(file.getOriginalFilename());
//            Path filePath = Paths.get(UPLOAD_DIR, filename);
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, file.getBytes());
//
//            UploadedFile uploadedFile = new UploadedFile(filename, filePath.toString());
//            fileRepository.save(uploadedFile);
//
//            return ResponseEntity.ok("File uploaded successfully: " + filename);
//        } catch (IOException e) {
//            e.printStackTrace();  // Log the exception to help debug
//            return ResponseEntity.status(500).body("File upload failed.");
//        }
//    }
//
//    /**
//     * List All Files in Database
//     */
//    @GetMapping("/list")
//    public ResponseEntity<List<String>> listFiles() {
//        List<String> filenames = fileRepository.findAll()
//                .stream()
//                .map(UploadedFile::getFilename)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(filenames);
//    }
//
//    /**
//     * View File (If HTML, return as plain text)
//     */
//    @GetMapping("/view/{filename}")
//    public ResponseEntity<String> viewFile(@PathVariable String filename) {
//        try {
//            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
//                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));
//
//            Path filePath = Paths.get(uploadedFile.getFilepath());
//            String content = Files.readString(filePath);
//
//            // If file is HTML, convert to text
//            if (filename.endsWith(".html") || filename.endsWith(".htm")) {
//                content = stripHtml(content);
//            }
//
//            return ResponseEntity.ok(content);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Error reading file.");
//        }
//    }
//
//    /**
//     * Edit File (Convert back to HTML if necessary)
//     */
//    @PostMapping("/edit/{filename}")
//    public ResponseEntity<String> editFile(@PathVariable String filename, @RequestBody String newContent) {
//        try {
//            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
//                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));
//
//            Path filePath = Paths.get(uploadedFile.getFilepath());
//
//            // If it's an HTML file, convert text back to HTML
//            if (filename.endsWith(".html") || filename.endsWith(".htm")) {
//                newContent = wrapHtml(newContent);
//            }
//
//            // For text files, save the plain text content directly
//            Files.writeString(filePath, newContent, StandardOpenOption.TRUNCATE_EXISTING);
//
//            return ResponseEntity.ok("File updated successfully: " + filename);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Error updating file.");
//        }
//    }
//
//    /**
//     * Download File (Returns as it is)
//     */
//    @GetMapping("/download/{filename}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//        try {
//            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
//                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));
//
//            Path filePath = Paths.get(uploadedFile.getFilepath());
//            Resource resource = new UrlResource(filePath.toUri());
//
//            MediaType contentType;
//            try {
//                contentType = MediaType.parseMediaType(Files.probeContentType(filePath));
//            } catch (IOException e) {
//                contentType = MediaType.APPLICATION_OCTET_STREAM;
//            }
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
//                    .contentType(contentType)
//                    .body(resource);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    /**
//     * Delete File
//     */
//    @DeleteMapping("/delete/{filename}")
//    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
//        try {
//            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
//                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));
//
//            Path filePath = Paths.get(uploadedFile.getFilepath());
//            Files.deleteIfExists(filePath);
//            fileRepository.delete(uploadedFile);
//
//            return ResponseEntity.ok("File deleted successfully.");
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Error deleting file.");
//        }
//    }
//
//    /**
//     * Utility: Strip HTML tags (Convert HTML → Plain Text)
//     */
//    private String stripHtml(String html) {
//        return html.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
//    }
//
//    /**
//     * Utility: Wrap text in HTML (Convert Plain Text → HTML)
//     */
//    private String wrapHtml(String text) {
//        return "<html><body>" + text.replace("\n", "<br>") + "</body></html>";
//    }
//}
package com.cdac.fileconversion.controller;

import com.cdac.fileconversion.entity.UploadedFile;
import com.cdac.fileconversion.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/files")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private FileRepository fileRepository;

    /**
     * Upload File & Store Path in Database
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty!");
            }

            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            UploadedFile uploadedFile = new UploadedFile(filename, filePath.toString());
            fileRepository.save(uploadedFile);

            return ResponseEntity.ok("File uploaded successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed.");
        }
    }

    /**
     * List All Files in Database
     */
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        List<String> filenames = fileRepository.findAll()
                .stream()
                .map(UploadedFile::getFilename)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filenames);
    }

    /**
     * View File (If HTML, return as plain text)
     */
    @GetMapping("/view/{filename}")
    public ResponseEntity<String> viewFile(@PathVariable String filename) {
        try {
            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));

            Path filePath = Paths.get(uploadedFile.getFilepath());
            String content = Files.readString(filePath);

            // Convert HTML files to plain text for editing
            if (filename.endsWith(".html") || filename.endsWith(".htm")) {
                content = stripHtml(content);
            }

            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading file.");
        }
    }

    /**
     * Edit File (Convert back to HTML if necessary)
     */
    /**
     * Edit File (Replace the original file with new content)
     */
    @PostMapping("/edit/{filename}")
    public ResponseEntity<String> editFile(@PathVariable String filename, @RequestBody String newContent) {
        try {
            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));

            Path filePath = Paths.get(uploadedFile.getFilepath());

            // If it's an HTML file, convert text back to HTML
            if (filename.endsWith(".html") || filename.endsWith(".htm")) {
                newContent = wrapHtml(newContent);
            }

            // Overwrite the existing file
            Files.write(filePath, newContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

            return ResponseEntity.ok("File updated successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error updating file.");
        }
    }

    /**
     * Download File (Convert .txt to structured .html)
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));

            Path filePath = Paths.get(uploadedFile.getFilepath());
            String content = Files.readString(filePath);
            String downloadFilename = filename;
            MediaType contentType;

            // If it's a .txt file, convert it to a well-structured .html before downloading
            if (filename.endsWith(".txt")) {
                content = wrapHtml(content);
                downloadFilename = filename.replace(".txt", ".html");
                filePath = Paths.get(UPLOAD_DIR, downloadFilename);
                Files.write(filePath, content.getBytes());
            }

            Resource resource = new UrlResource(filePath.toUri());

            try {
                contentType = MediaType.parseMediaType(Files.probeContentType(filePath));
            } catch (IOException e) {
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFilename + "\"")
                    .contentType(contentType)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Delete File
     */
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        try {
            UploadedFile uploadedFile = fileRepository.findByFilename(filename)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));

            Path filePath = Paths.get(uploadedFile.getFilepath());
            Files.deleteIfExists(filePath);
            fileRepository.delete(uploadedFile);

            return ResponseEntity.ok("File deleted successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deleting file.");
        }
    }

    /**
     * Utility: Strip HTML tags (Convert HTML → Plain Text)
     */
    private String stripHtml(String html) {
        return html.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
    }

    /**
     * Utility: Wrap text in a complete HTML structure (Convert Plain Text → HTML)
     */
    private String wrapHtml(String text) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Downloaded File</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 20px; padding: 20px; }\n" +
                "        pre { white-space: pre-wrap; word-wrap: break-word; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <pre>" + text + "</pre>\n" +
                "</body>\n" +
                "</html>";
    }
}
