package com.example.TexToPdf.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TexFiles {

    public static void saveFileToLocalDisk(File file, String filePath) throws IOException {
        // Create a directory (if not exists) for saving the file
        File directory = new java.io.File(filePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to disk
        Files.copy(file.getAbsoluteFile().toPath(), new File(filePath).toPath());
    }
}
