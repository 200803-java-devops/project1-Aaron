package com.Revature.Aaron.Processes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Commands {

    public static String executeCommand(String command, String directory) {
        ProcessBuilder pBuilder = new ProcessBuilder();

        pBuilder.command("cmd.exe", "/c", command);
        pBuilder.directory(new File(directory));
        Process process = null;
        int exitCode = 0;
        String output = "";

        try {
            process = pBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            exitCode = process.waitFor();

            while (reader.ready()) {
                output += reader.readLine() + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
    
}