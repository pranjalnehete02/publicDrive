package com.cdac.fileconversion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String filepath; // Store only file path

    public UploadedFile() {}

    public UploadedFile(String filename, String filepath) {
        this.filename = filename;
        this.filepath = filepath;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getFilepath() { return filepath; }
}
