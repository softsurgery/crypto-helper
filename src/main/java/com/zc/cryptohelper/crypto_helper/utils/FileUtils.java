package com.zc.cryptohelper.crypto_helper.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static File downloadFile(String fileURL, String filename, String ext) throws IOException {
        URL url = new URL(fileURL);
        File file = new File(filename + ext);
        try (InputStream in = url.openStream()) {
            logger.info("Downloading file to: " + file.getAbsolutePath());
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        scheduleFileDeletion(file, 5, TimeUnit.SECONDS);
        return file;
    }

    private static void scheduleFileDeletion(File file, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            if (file.exists() && !file.delete()) {
                logger.warn("Failed to delete file: " + file.getAbsolutePath());
            } else {
                logger.info("File deleted: " + file.getAbsolutePath());
            }
        }, delay, unit);
    }
}
