package com.zc.cryptohelper.crypto_helper.storage;

import com.zc.cryptohelper.crypto_helper.storage.exceptions.UploadException;
import com.zc.cryptohelper.crypto_helper.storage.exceptions.UploadFileNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UploadService {
    private Logger logger = LoggerFactory.getLogger(UploadService.class);

    @Value("${file.upload-dir}")
    private Path rootLocation;

    @Autowired
    private UploadRepository uploadRepository;

    public List<Upload> findAll() {
        return uploadRepository.findAll();
    }

    @Transactional(rollbackOn = {UploadException.class, IOException.class})
    public Upload store(MultipartFile file) {
        String slug = UUID.randomUUID().toString();
        String filename = file.getOriginalFilename();
        String mimetype = file.getContentType();
        Long size = file.getSize();

        String extention = FilenameUtils.getExtension(filename);

        String relativePath = slug;

        if (!extention.isBlank()) {
            relativePath = slug + "." + extention;
        }

        logger.info("Storing file {} with relative path {}", filename, relativePath);

        Upload upload = new Upload();
        upload.setSlug(slug);
        upload.setFilename(filename);
        upload.setMimetype(mimetype);
        upload.setSize(size);
        upload.setRelativePath(relativePath);
        uploadRepository.save(upload);

        try {
            if (file.isEmpty()) {
                throw new UploadException("Failed to store empty file.");
            }

            if (file.getOriginalFilename().contains("..")) {
                // This is a security check
                throw new UploadException(
                        "Cannot store file with relative path outside current directory.");
            }

            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(relativePath))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new UploadException(
                        "Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UploadException("Failed to store file." + e.getMessage());
        }

        return upload;
    }

    public Resource loadAsResource(String slug) {
        Upload upload = findBySlug(slug)
                .orElseThrow(() -> new UploadException("File not found"));

        String filename = upload.getFilename();

        try {
            Path file = rootLocation.resolve(upload.getRelativePath());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new UploadFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new UploadFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public Optional<Upload> findBySlug(String slug) {
        return uploadRepository.findBySlug(slug);
    }

    public Optional<Upload> findByFileName(String filename) {
        return uploadRepository.findByFilename(filename);
    }

    public Optional<Upload> findById(Long id) {
        return uploadRepository.findById(id);
    }

    public Upload save(Upload upload) {
        return uploadRepository.save(upload);
    }

    public Upload deleteById(Long id) {
        Upload upload = uploadRepository.findById(id)
                .orElseThrow(() -> new UploadFileNotFoundException("File not found with id: " + id));

        Path file = rootLocation.resolve(upload.getRelativePath());
        try {
            Files.deleteIfExists(file);
            uploadRepository.deleteById(id);
            return upload;
        } catch (Exception e) {
            throw new UploadException("Failed to delete file: " + id);
        }
    }

    public Upload deleteBySlug(String slug) {
        Upload upload = uploadRepository.findBySlug(slug)
                .orElseThrow(() -> new UploadFileNotFoundException("File not found with slug: " + slug));

        Path file = rootLocation.resolve(upload.getRelativePath());
        try {
            Files.deleteIfExists(file);
            uploadRepository.deleteBySlug(slug);
            return upload;
        } catch (Exception e) {
            throw new UploadException("Failed to delete file: " + slug);
        }
    }

    public Upload delete(Upload upload) {
        Path file = rootLocation.resolve(upload.getRelativePath());
        try {
            Files.deleteIfExists(file);
            uploadRepository.deleteBySlug(upload.getSlug());
            return upload;
        } catch (Exception e) {
            throw new UploadException("Failed to delete file: " + upload.getSlug());
        }
    }
}
