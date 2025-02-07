package com.example.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${file.storage.location}")
    private String storageLocation;

    private final FileMetadataRepository fileRepository;
    private final SharedFilesRepository sharedFilesRepository;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    public FileService(FileMetadataRepository fileRepository, SharedFilesRepository sharedFilesRepository, UserRepository userRepository, JavaMailSender emailSender) {
        this.fileRepository = fileRepository;
        this.sharedFilesRepository = sharedFilesRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    // 🔥 Upload file
    public FileMetadata uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(storageLocation, fileName);
        file.transferTo(filePath);

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(fileName);
        fileMetadata.setFilePath(filePath.toString());
        fileMetadata.setFileSize(file.getSize());

        // Generate Base62 token for file sharing
        String token = generateBase62Token();
        fileMetadata.setSharingToken(token);

        // Set expiry timestamp (valid for 24 hours)
        fileMetadata.setExpiryTimestamp(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000));  // 7 days


        return fileRepository.save(fileMetadata);
    }

    // 🔥 Generate Base62 token
    private String generateBase62Token() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return base62Encode(bytes);
    }

    // 🔥 Base62 encoding
    private String base62Encode(byte[] input) {
        final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        long value = 0;
        for (byte b : input) {
            value = (value << 8) | (b & 0xFF);
            result.append(chars.charAt((int) (value % 62)));
            value /= 62;
        }
        return result.toString();
    }
    public Path downloadFile(String sharingToken) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findBySharingToken(sharingToken);
        if (fileMetadataOpt.isPresent()) {
            return Paths.get(fileMetadataOpt.get().getFilePath());
        } else {
            throw new IllegalStateException("File not found");
        }
    }

    // 🔥 Convert file to HTML for viewing
    public String convertFileToHtml(String sharingToken) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findBySharingToken(sharingToken);
        if (fileMetadataOpt.isPresent()) {
            FileMetadata fileMetadata = fileMetadataOpt.get();

            if (fileMetadata.getExpiryTimestamp() < System.currentTimeMillis()) {
                throw new IllegalStateException("Token expired");
            }

            Path filePath = Paths.get(fileMetadata.getFilePath());
            try {
                String content = new String(Files.readAllBytes(filePath));

                return "<html><body><h2>File Content</h2><pre>" + content + "</pre></body></html>";
            } catch (IOException e) {
                throw new IllegalStateException("Error reading file", e);
            }
        } else {
            throw new IllegalStateException("File not found");
        }
    }

    // 🔥 Get file content for editing
    public String getFileContent(String sharingToken) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findBySharingToken(sharingToken);
        if (fileMetadataOpt.isPresent()) {
            Path filePath = Paths.get(fileMetadataOpt.get().getFilePath());
            try {
                return new String(Files.readAllBytes(filePath));
            } catch (IOException e) {
                throw new IllegalStateException("Error reading file", e);
            }
        } else {
            throw new IllegalStateException("File not found");
        }
    }

    // 🔥 Update file content
    public void updateFile(String sharingToken, String newContent) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findBySharingToken(sharingToken);
        if (fileMetadataOpt.isPresent()) {
            Path filePath = Paths.get(fileMetadataOpt.get().getFilePath());
            try {
                Files.write(filePath, newContent.getBytes());
            } catch (IOException e) {
                throw new IllegalStateException("Error updating file", e);
            }
        } else {
            throw new IllegalStateException("File not found");
        }
    }

    // 🔥 Share file via email
    public void shareFile(String sharingToken, String email, boolean editable) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findBySharingToken(sharingToken);
        if (fileMetadataOpt.isPresent()) {
            FileMetadata fileMetadata = fileMetadataOpt.get();
//            if (fileMetadata.getExpiryTimestamp() < System.currentTimeMillis()) {
//                throw new IllegalStateException("Token expired");
//            }

            String fileUrl = editable 
                ? "http://localhost:8081/api/files/edit/" + sharingToken  
                : "http://localhost:8081/api/files/view/" + sharingToken; 

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rashirptl@gmail.com");
            message.setTo(email);
            message.setSubject("Shared File Link");
            message.setText("Click the link below to view the file:\n\n" + fileUrl);
            emailSender.send(message);
        } else {
            throw new IllegalStateException("File not found");
        }
    }


//    public void shareFileWithUser(Long fileId, Long sharedWithUserId, String accessLevel) {
//        Optional<FileMetadata> fileMetadataOpt = fileRepository.findById(fileId);
//        Optional<User> userOpt = userRepository.findById(sharedWithUserId);
//
//        if (fileMetadataOpt.isPresent() && userOpt.isPresent()) {
//            // Validate access level
//            if (!accessLevel.equalsIgnoreCase("READ") && !accessLevel.equalsIgnoreCase("WRITE")) {
//                throw new IllegalArgumentException("Invalid access level. Must be 'READ' or 'WRITE'.");
//            }
//
//            // Share the file with the user
//            SharedFile sharedFile = new SharedFile();
//            sharedFile.setFileId(fileId);
//            sharedFile.setSharedWithUserId(sharedWithUserId);
//            sharedFile.setPermission(accessLevel.toUpperCase()); // Ensure it's in uppercase
//            sharedFilesRepository.save(sharedFile);
//        } else {
//            throw new IllegalStateException("File or User not found");
//        }
//    }
 // 🔥 Share file with another user
    public void shareFileWithUser(Long fileId, Long sharedWithUserId, String accessLevel) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findById(fileId);
        Optional<User> userOpt = userRepository.findById(sharedWithUserId);

        if (fileMetadataOpt.isPresent() && userOpt.isPresent()) {
            // Validate access level
            if (!accessLevel.equalsIgnoreCase("READ") && !accessLevel.equalsIgnoreCase("WRITE")) {
                throw new IllegalArgumentException("Invalid access level. Must be 'READ' or 'WRITE'.");
            }

            // Create and save the shared file record
            SharedFile sharedFile = new SharedFile();
            sharedFile.setFileId(fileId);
            sharedFile.setSharedWithUserId(sharedWithUserId);
            sharedFile.setPermission(AccessLevel.valueOf(accessLevel.toUpperCase()));
            sharedFilesRepository.save(sharedFile);
        } else {
            throw new IllegalStateException("File or User not found");
        }
    }


    // 🔥 Get "Shared with Me" files
    public List<FileMetadata> getSharedFilesForUser(Long userId) {
        List<Long> sharedFileIds = sharedFilesRepository.findFileIdsByUserId(userId);
        return fileRepository.findAllById(sharedFileIds);
    }

    // 🔥 Get all files in required format
    public List<Map<String, Object>> getAllFiles() {
        List<FileMetadata> files = fileRepository.findAll();
        return files.stream().map(file -> {
            Map<String, Object> fileMap = new HashMap<>();
            fileMap.put("id", file.getId());
            fileMap.put("name", file.getFileName());
            fileMap.put("type", getFileExtension(file.getFileName()));
            fileMap.put("size", formatSize(file.getFileSize()));
            fileMap.put("owner", "Me");
            fileMap.put("sharing_token", file.getSharingToken());
            return fileMap;
        }).collect(Collectors.toList());
    }
    public String convertFileToHtml(Long id) {
        Optional<FileMetadata> fileMetadataOpt = fileRepository.findById(id);
        if (fileMetadataOpt.isPresent()) {
            FileMetadata fileMetadata = fileMetadataOpt.get();

            // Check if the file has expired
            if (fileMetadata.getExpiryTimestamp() < System.currentTimeMillis()) {
                throw new IllegalStateException("Token expired");
            }

            Path filePath = Paths.get(fileMetadata.getFilePath());
            try {
                // Read file content and convert to HTML
                String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

                // Return the content inside an HTML structure
                return "<html><body><h2>File Content</h2><pre>" + content + "</pre></body></html>";
            } catch (IOException e) {
                throw new IllegalStateException("Error reading file", e);
            }
        } else {
            throw new IllegalStateException("File not found");
        }
    }


    // 🔥 Extract file type
    private String getFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "unknown";
    }

    // 🔥 Format file size
    private String formatSize(long sizeInBytes) {
        double sizeInMB = sizeInBytes / (1024.0 * 1024.0);
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(sizeInMB) + "MB";
    }
}
