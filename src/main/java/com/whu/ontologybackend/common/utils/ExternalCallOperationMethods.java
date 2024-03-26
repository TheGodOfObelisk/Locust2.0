package com.whu.ontologybackend.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExternalCallOperationMethods {
    public static void callPythonScript(String path) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", path);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null){
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        System.out.println("Exit code: " + exitCode);
        System.out.println("Output: \n" + output.toString());

        return;
    }

    public static void callPyateScript(String path, String article) throws IOException, InterruptedException {
        // \n may exist in the article String
        ProcessBuilder processBuilder = new ProcessBuilder("python", path, article);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null){
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        System.out.println("Exit code: " + exitCode);
        System.out.println("Output: \n" + output.toString());

        return;
    }
}
