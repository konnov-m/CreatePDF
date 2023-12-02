package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.example.ReplaceVariable.replaceVariableInFile;

public class Main {

    static final String TEX = ".tex";

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Start to create pdf file");

        String xelatexCompiler = "xelatex";
        String nameTexFile = "main";
        String copyNameTexFile = "main2";
        String workDirectory = "/home/konnov/IdeaProjects/CreatePDF/templates/";

        replaceVariableInFile(workDirectory, nameTexFile + TEX,copyNameTexFile + TEX,
                "VAR::surname", "Konnov");

        ProcessBuilder processBuilder = new ProcessBuilder(xelatexCompiler, workDirectory + copyNameTexFile + TEX);
        processBuilder.directory(new File(workDirectory));

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Compile success");
        } else {
            System.out.println("Compile failed");
        }
        Files.copy(Path.of(workDirectory + copyNameTexFile + ".pdf"), Path.of(workDirectory + nameTexFile + ".pdf"));

        deleteUselessFiles(workDirectory + copyNameTexFile);

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
}