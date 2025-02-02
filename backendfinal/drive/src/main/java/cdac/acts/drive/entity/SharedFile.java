package cdac.acts.drive.entity;


import cdac.acts.drive.enum1.Permission;
import jakarta.persistence.*;
import lombok.Data;


@Data
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

    @Enumerated(EnumType.STRING)
    private Permission permission;
}
