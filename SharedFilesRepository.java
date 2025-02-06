package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SharedFilesRepository extends JpaRepository<SharedFile, Long> {

    @Query("SELECT sf.fileId FROM SharedFile sf WHERE sf.sharedWithUserId = :userId")
    List<Long> findFileIdsByUserId(Long userId);
}
