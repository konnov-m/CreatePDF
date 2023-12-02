package org.example;

import java.io.*;

public class ReplaceVariable {

    static void replaceVariableInFile(String fullPathToFile, String oldNamefile, String newFileName , String nameVariable, String value) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fullPathToFile + oldNamefile)));
        PrintWriter out = new PrintWriter(new FileWriter(fullPathToFile + newFileName), true);
        String newStr;
        while ((newStr = in.readLine()) != null) {
            if (newStr.contains(nameVariable)) {
                System.out.println("String before" + newStr);
                newStr = newStr.replace(nameVariable, value);
                System.out.println("New String" + newStr);
            }
            out.println(newStr);
        }
    }

}
