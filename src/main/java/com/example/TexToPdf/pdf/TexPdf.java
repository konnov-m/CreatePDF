package com.example.TexToPdf.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.TexToPdf.pdf.ReplaceVariable.replaceVariableInFile;

public class TexPdf {

    static final Logger log = LoggerFactory.getLogger(TexPdf.class);

    public static final String TEX = ".tex";

    public static final String PDF = ".pdf";

    static final String WORK_DIRECTORY = "/var/files/TexToPdfBot/";

    static final String XELATEX_COMPILER = "xelatex";

    static public boolean createPdf (String name, Map<String, String> vars) {
        deleteIfExists(getFullPathToPdf(name));

        String newFileName = name + "_copy";
        try {
            replaceVariableInFile(WORK_DIRECTORY, name + TEX,newFileName + TEX, vars);
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(XELATEX_COMPILER, WORK_DIRECTORY + newFileName + TEX);
        processBuilder.directory(new File(WORK_DIRECTORY));

        Process process = null;
        int exitCode = -1;
        try {
            process = processBuilder.start();
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
        if (exitCode == 0) {
            try {
                Files.copy(Path.of(WORK_DIRECTORY + newFileName + PDF),
                        Path.of(WORK_DIRECTORY + name + PDF));
            } catch (IOException e) {
                log.error(e.getMessage());
                return false;
            }

            deleteUselessFiles(WORK_DIRECTORY + newFileName);
            return true;
        } else {
            deleteUselessFiles(WORK_DIRECTORY + newFileName);
            return false;
        }
    }


    static void deleteUselessFiles(String name) {
        Set<String> set = new HashSet<>();
        set.add(".aux");
        set.add(".bcf");
        set.add(".log");
        set.add(".out");
        set.add(".run.xml");
        set.add(".tex");
        set.add(".pdf");

        for (String s:set) {
            File file = new File(name + s);
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static String getFullPathToPdf(String name) {
        return WORK_DIRECTORY + name + PDF;
    }

    public static String getFullPathToTex(String name) {
        return WORK_DIRECTORY + name + TEX;
    }

    public static void deleteIfExists (String name) {
        File file = new File(name);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
