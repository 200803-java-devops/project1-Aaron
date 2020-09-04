package com.Revature.Aaron.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Commands {

    public static String executeCommand(String command, String directory) {
        ProcessBuilder pBuilder = new ProcessBuilder();
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            pBuilder.command("cmd.exe", "/c", command);
        } else {
            pBuilder.command("sh", "-c", command);
        }
        pBuilder.directory(new File(directory));
        Process process = null;
        String output = "";

        try {
            process = pBuilder.start();

            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream())); BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                Boolean noTimeout = process.waitFor(60, TimeUnit.SECONDS);
                if(!noTimeout) {
                }
                String line;
                while (inputReader.ready()) {
                    line = inputReader.readLine() + "\n";
                    output += line;
                }
                while (errorReader.ready()) {
                    line = errorReader.readLine() + "\n";
                    output += line;
                }
            }  catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return output;
    }
    
}