package com.zc.cryptohelper.crypto_helper.storage;

import com.zc.cryptohelper.crypto_helper.storage.exceptions.UploadFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload-file")
    public ResponseEntity<Upload> upload(@RequestParam("file") MultipartFile file) {
        Upload upload = uploadService.store(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(upload);
    }

    @GetMapping("/files")
    public ResponseEntity<List<Upload>> getFiles() {
        List<Upload> uploads = uploadService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(uploads);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Upload> getFileByIdOrSlug(@PathVariable String id) {
        Boolean isId = false;
        Long idAsLong = null;
        try {
            idAsLong = Long.parseLong(id);
            isId = true;
        } catch (Exception e) {
            logger.info("Failed to parse path value: {} as ID", id);
        }

        if (isId) {
            Upload upload = uploadService.findById(idAsLong)
                    .orElseThrow(() -> new UploadFileNotFoundException("File not found with id: " + id));
            return ResponseEntity.status(HttpStatus.OK).body(upload);
        } else {
            Upload upload = uploadService.findBySlug(id)
                    .orElseThrow(() -> new UploadFileNotFoundException("File not found with slug: " + id));
            return ResponseEntity.status(HttpStatus.OK).body(upload);
        }
    }

    @GetMapping("/files/{slug}/download")
    public ResponseEntity<Resource> getFile(@PathVariable String slug) throws Exception {
        Upload upload = uploadService.findBySlug(slug)
                .orElseThrow(() -> new UploadFileNotFoundException("File not found with slug: " + slug));
        Resource resource = uploadService.loadAsResource(slug);

        File file = resource.getFile();
        long fileLength = file.length();
        HttpHeaders respHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType(upload.getMimetype());
        respHeaders.setContentType(mediaType);
        respHeaders.setContentLength(fileLength);
        respHeaders.setContentDispositionFormData("attachment", file.getName());
        return ResponseEntity.ok().headers(respHeaders).body(resource);
    }

    @DeleteMapping("/files/{slug}")
    public ResponseEntity<Upload> deleteFile(@PathVariable String slug) {
        Upload upload = uploadService.deleteBySlug(slug);
        return ResponseEntity.ok().body(upload);
    }
}