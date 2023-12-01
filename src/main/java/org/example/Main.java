package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Start to create pdf file");

        String xelatexCompiler = "xelatex";
        String texFilePath = "/home/konnov/IdeaProjects/CreatePDF/templates/main";

        ProcessBuilder processBuilder = new ProcessBuilder(xelatexCompiler, texFilePath + ".tex");
        processBuilder.directory(new File("/home/konnov/IdeaProjects/CreatePDF/templates/"));

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Compile success");
        } else {
            System.out.println("Compile failed");
        }
        deleteUselessFiles(texFilePath);

    }


    static void deleteUselessFiles(String name) {
        Set<String> set = new HashSet<>();
        set.add(".aux");
        set.add(".bcf");
        set.add(".log");
        set.add(".out");
        set.add(".run.xml");

        for (String s:set) {
            File file = new File(name + s);
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}