package com.whu.ontologybackend.common.utils;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


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

    public static boolean callQuickTypeScript(String targetPath, File jsonFile, String moduleSource){
        // the converted file name: XX-schema.json
        // check write privilege
        File file = new File(targetPath);
        if(file.exists()){
            if(file.canWrite()){
                System.out.println("can write to this directory");
            } else {
                System.out.println("cannot write to this directory");
            }
        } else {
            System.out.println("Directory does not exist.");
        }
        try{
            // 1, cannot output
            String command = "quicktype " + jsonFile.getAbsolutePath() + " -o " + targetPath + "\\" + jsonFile.getName().replace(".json", "-schema.json") + " -l schema";
            Process process = Runtime.getRuntime().exec("runas /user:Administrator cmd /c " + command);
            // 2, no privilege to run
//            targetPath = targetPath + "\\" + jsonFile.getName().replace(".json", "-schema.json");
//            ProcessBuilder processBuilder = new ProcessBuilder("quicktype", jsonFile.getAbsolutePath(), "-o", targetPath, "-l", "schema");
//            processBuilder.redirectErrorStream(true);
//            processBuilder.environment().put("hasAdministrativeRights", "true");
//            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean callTrangScript(String jarPath, File xmlFile, String moduleSource) throws IOException, InterruptedException {
        // check directory
        String path2Check = jarPath + moduleSource;
        Path path = Paths.get(path2Check);
        if(!moduleSource.equals("")){
            boolean exist = Files.exists(path);
            if(!exist){
                System.out.println("XML from this module is first inputted. Ready to create its directory.");
                File moduleDir = new File(path2Check);
                if(!moduleDir.mkdir()){
                    System.out.println("Failed to create the module directory. Exit.");
                    return false;
                } else {
                    System.out.println("Succeeded to create the module directory. Continue.");
                }
            } else {
                System.out.println("XML from this module has been inputted.");
            }
        } else {
            System.out.println("No module source has been specified");
            // check defaultModule
            path2Check = jarPath + "defaultModule";
            path = Paths.get(path2Check);
            boolean exist = Files.exists(path);
            if(!exist){
                System.out.println("Ready to create the default module directory.");
                File moduleDir = new File(path2Check);
                if(!moduleDir.mkdir()){
                    System.out.println("Failed to create the module directory. Exit.");
                    return false;
                } else {
                    System.out.println("Succeeded to create the module directory. Continue.");
                }
            } else {
                System.out.println("The default module directory has been created.");
            }
        }

        String jarFile = jarPath + "trang-20091111.jar"; // fixed
        String xmlAbsoluatePath = xmlFile.getAbsolutePath();
        String targetFile = path2Check + "\\" + xmlFile.getName().replace(".xml", "") + ".xsd";
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFile, xmlAbsoluatePath, targetFile);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            output.append(line).append("\n");
        }
        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
        return true;
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
                if(!CommonOperationMethods.isDecimal(parts[1])){
                    continue;
                }
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
