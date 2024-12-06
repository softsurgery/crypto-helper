package com.zc.cryptohelper.crypto_helper.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {
    Optional<Upload> findBySlug(String slug);
    Optional<Upload> findByFilename(String filename);
    void deleteBySlug(String slug);
}