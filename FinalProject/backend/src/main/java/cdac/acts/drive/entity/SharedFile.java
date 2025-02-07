package cdac.acts.drive.entity;


import cdac.acts.drive.enum1.Permission;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "shared_files")
public class SharedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileStorage file;

    @ManyToOne
    @JoinColumn(name = "shared_with_user_id")
    private User sharedWithUser;

    @ManyToOne
    @JoinColumn(name = "shared_by_user_id")
    private User sharedByUser;

    @Enumerated(EnumType.STRING)
    private Permission permission;


    public SharedFile() {
    }

    public SharedFile(FileStorage file, Long id, Permission permission, User sharedByUser, User sharedWithUser) {
        this.file = file;
        this.id = id;
        this.permission = permission;
        this.sharedByUser = sharedByUser;
        this.sharedWithUser = sharedWithUser;
    }

    public FileStorage getFile() {
        return file;
    }

    public void setFile(FileStorage file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public User getSharedByUser() {
        return sharedByUser;
    }

    public void setSharedByUser(User sharedByUser) {
        this.sharedByUser = sharedByUser;
    }

    public User getSharedWithUser() {
        return sharedWithUser;
    }

    public void setSharedWithUser(User sharedWithUser) {
        this.sharedWithUser = sharedWithUser;
    }
}