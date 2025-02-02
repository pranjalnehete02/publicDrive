package cdac.acts.drive.repository;

import cdac.acts.drive.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    Optional<FileStorage> findByFileNameAndFolderId(String fileName, Long folderId);

    List<FileStorage> findByFolderId(Long id);
}
