package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findBySharingToken(String sharingToken);
    Optional<FileMetadata> findByPublicToken(String publicToken);
}

