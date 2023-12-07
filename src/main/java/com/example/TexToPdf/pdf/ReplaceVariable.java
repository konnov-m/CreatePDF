package com.example.TexToPdf.pdf;

import java.io.*;
import java.util.Map;

public class ReplaceVariable {

    static final String PREFIX = "VAR::";

    static void replaceVariableInFile(String fullPathToFile, String oldNamefile, String newFileName , String nameVariable, String value) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fullPathToFile + oldNamefile)));
        PrintWriter out = new PrintWriter(new FileWriter(fullPathToFile + newFileName), true);
        String newStr;
        while ((newStr = in.readLine()) != null) {
            if (newStr.contains(nameVariable)) {
                newStr = newStr.replace(nameVariable, value);
            }
            out.println(newStr);
        }
    }

    static void replaceVariableInFile(String fullPathToFile, String oldNamefile, String newFileName , Map<String, String> vars) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fullPathToFile + oldNamefile)));
        PrintWriter out = new PrintWriter(new FileWriter(fullPathToFile + newFileName), true);
        String newStr;
        while ((newStr = in.readLine()) != null) {
            for (String nameVariable:vars.keySet()) {
                if (newStr.contains(PREFIX + nameVariable)) {
                    newStr = newStr.replace(PREFIX + nameVariable, vars.get(nameVariable));
                }
            }
            out.println(newStr);
        }
    }

}
