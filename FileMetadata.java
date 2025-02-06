package com.example.demo;

import lombok.Data;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Boolean isPublic = false;
    private Timestamp createdAt;
    private String sharingToken;
    private Long expiryTimestamp;
    private String publicToken;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getSharingToken() {
		return sharingToken;
	}
	public void setSharingToken(String sharingToken) {
		this.sharingToken = sharingToken;
	}
	public Long getExpiryTimestamp() {
		return expiryTimestamp;
	}
	public void setExpiryTimestamp(Long expiryTimestamp) {
		this.expiryTimestamp = expiryTimestamp;
	}
	public String getPublicToken() {
		return publicToken;
	}
	public void setPublicToken(String publicToken) {
		this.publicToken = publicToken;
	}
    
}

