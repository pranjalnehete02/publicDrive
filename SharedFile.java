package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "shared_files")
public class SharedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fileId;

    @Column(nullable = false)
    private Long sharedWithUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevel permission;  // Access level (READ/WRITE)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getSharedWithUserId() {
        return sharedWithUserId;
    }

    public void setSharedWithUserId(Long sharedWithUserId) {
        this.sharedWithUserId = sharedWithUserId;
    }

    public AccessLevel getPermission() {
        return permission;
    }

    public void setPermission(AccessLevel permission) {
        this.permission = permission;
    }
}
