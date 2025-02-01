package com.cdac.fileconversion.repository;

import com.cdac.fileconversion.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UploadedFile, Long> {
    Optional<UploadedFile> findByFilename(String filename);
}
