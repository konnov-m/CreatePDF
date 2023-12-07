package com.example.TexToPdf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;

public class TexFiles {

    static final Logger log = LoggerFactory.getLogger(TexFiles.class);

    public static void saveFileToLocalDisk(File file, String filePath) throws IOException {
        // Create a directory (if not exists) for saving the file
        File directory = new java.io.File(filePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to disk
        Files.copy(file.getAbsoluteFile().toPath(), new File(filePath).toPath());
    }

    public static void deleteFiles(String folderPath, String wildcard) {
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] mathingFiles = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.matches(wildcard);
                }
            });

            if (mathingFiles != null) {
                for (File file : mathingFiles) {
                    if (file.delete()) {
                        log.info("Delete file " + file.getName());
                    } else {
                        log.info("Can't delete file " + file.getName());
                    }
                }
            }
        } else {
            log.info("There is no directory");
        }
    }
}
