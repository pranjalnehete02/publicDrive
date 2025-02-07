package cdac.acts.drive.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name="folders")
public class Folder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String folderName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "folder")
    private Set<FileStorage> files;

    @OneToMany(mappedBy = "folder")
    private Set<SharedFolder> sharedFolders;

    public Folder() {
    }

    public Folder(Set<FileStorage> files, String folderName, Long id, Folder parentFolder, Set<SharedFolder> sharedFolders, User user) {
        this.files = files;
        this.folderName = folderName;
        this.id = id;
        this.parentFolder = parentFolder;
        this.sharedFolders = sharedFolders;
        this.user = user;
    }

    public Set<FileStorage> getFiles() {
        return files;
    }

    public void setFiles(Set<FileStorage> files) {
        this.files = files;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public Set<SharedFolder> getSharedFolders() {
        return sharedFolders;
    }

    public void setSharedFolders(Set<SharedFolder> sharedFolders) {
        this.sharedFolders = sharedFolders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}




