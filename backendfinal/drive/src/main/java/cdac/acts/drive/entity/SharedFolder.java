package cdac.acts.drive.entity;

import cdac.acts.drive.enum1.Permission;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shared_folders")
public class SharedFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "shared_with_user_id")
    private User sharedWithUser;

    @Enumerated(EnumType.STRING)
    private Permission permission;
}
