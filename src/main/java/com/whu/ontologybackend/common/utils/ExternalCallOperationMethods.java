package com.whu.ontologybackend.common.utils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    public static Map<String, Double> callPyateScript(String path, String article) throws IOException, InterruptedException {
        // \n may exist in the article String
        ProcessBuilder processBuilder = new ProcessBuilder("python", path, article);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        String regex = "\\s{2,}"; // means more than one spaces
        Map<String, Double> extractedTerms = new HashMap<>();
        while((line = reader.readLine()) != null){
            String[] parts = line.split(regex);
            if(parts.length != 2){
                System.out.println("Error occurred or meets the end!");
            } else {
                String key = parts[0];
                Double value = Double.parseDouble(parts[1]);
                extractedTerms.put(key, value);
            }
            output.append(line).append("\n");
//            System.out.println(parts);
        }

        int exitCode = process.waitFor();
        System.out.println(extractedTerms.toString());
//        System.out.println("Exit code: " + exitCode);
//        System.out.println("Output: \n" + output.toString());

        return extractedTerms;
    }
}
