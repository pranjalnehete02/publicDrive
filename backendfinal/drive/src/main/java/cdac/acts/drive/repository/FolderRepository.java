package cdac.acts.drive.repository;

import cdac.acts.drive.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    //will give root folder
    Optional<Folder> findByUserIdAndParentFolderIsNull(Long userId);

    List<Folder> findByParentFolderId(Long id);
}
