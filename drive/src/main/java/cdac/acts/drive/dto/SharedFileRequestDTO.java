package cdac.acts.drive.dto;

import cdac.acts.drive.enum1.Permission;

public class SharedFileRequestDTO {
    private Long fileId;
    private String sharedByUsername;  // Changed to username of the sender
    private String sharedToUser;  // This will be the username of the user with whom the file is shared
    private Permission permission;

    // Getters and Setters
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getSharedByUsername() {
        return sharedByUsername;
    }

    public void setSharedByUsername(String sharedByUsername) {
        this.sharedByUsername = sharedByUsername;
    }

    public String getSharedToUser() {
        return sharedToUser;
    }

    public void setSharedToUser(String sharedToUser) {
        this.sharedToUser = sharedToUser;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
