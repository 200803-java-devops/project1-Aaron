package com.Revature.Aaron.Utils;

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
        String output = "";

        try {
            process = pBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();

            while (reader.ready()) {
                output += reader.readLine() + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
    
}