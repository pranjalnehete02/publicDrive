package cdac.acts.drive.repository;

import cdac.acts.drive.entity.SharedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedFileRepository extends JpaRepository<SharedFile, Long> {
    List<SharedFile> findBySharedWithUserId(Long userId);
}