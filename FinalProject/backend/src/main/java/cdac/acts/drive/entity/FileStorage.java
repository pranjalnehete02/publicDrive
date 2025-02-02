package cdac.acts.drive.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name="files")
@Data
public class FileStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToMany(mappedBy = "file")
    private Set<SharedFile> sharedFiles;


    public FileStorage() {
    }


    public FileStorage(Set<SharedFile> sharedFiles, Long id, Folder folder, String filePath, String fileName) {
        this.sharedFiles = sharedFiles;
        this.id = id;
        this.folder = folder;
        this.filePath = filePath;
        this.fileName = fileName;
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

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SharedFile> getSharedFiles() {
        return sharedFiles;
    }

    public void setSharedFiles(Set<SharedFile> sharedFiles) {
        this.sharedFiles = sharedFiles;
    }
}
