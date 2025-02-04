package cdac.acts.drive.dto;

public class SharedFileResponseDTO {

    private Long fileId;
    private String fileName;
    private String ownerOfFile;  // Owner's username (the user who shared the file)
    private String permission;

    // Getters and Setters
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwnerOfFile() {
        return ownerOfFile;
    }

    public void setOwnerOfFile(String ownerOfFile) {
        this.ownerOfFile = ownerOfFile;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
