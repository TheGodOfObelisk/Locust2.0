package com.whu.ontologybackend.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperationMethods {

    private static final String URL = "jdbc:mysql://localhost:3306/myontology";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void DBInit(String path){
        File folder = new File(path);
        traverseFiles(folder);
    }

    private static void traverseFiles(File folder){
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files != null){
                for(File file: files){
                    if(file.isDirectory()){
                        traverseFiles(file);
                    } else {
                        extractCQMapping(file);
                    }
                }
            }
        } else {
            System.out.println("Error: no more folders in the path.");
        }
    }

    private static void extractCQMapping(File file){
        System.out.println(file.getName());
        try(InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            JSONReader jr = new JSONReader(br)) {
            JSONObject object = new JSONObject();
            jr.startObject();
            while(jr.hasNext()){
                String key = jr.readString();
                if(key.equals("cqs")){
                    JSONArray value = (JSONArray) jr.readObject();
                    object.put(key, value);
                } else if (key.equals("query")) {
                    String value = (String) jr.readObject();
                    object.put(key, value);
                } else {
                    System.out.println("Error: wrong field.");
                }
            }
            jr.endObject();
            // extract "cqs" and "query" fields and process them, respectively
            writeCQMappings(object);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeCQMappings(JSONObject inputCQTs){
        // TODO: extract two fields and write them into MySQL
        // assign ID to each CQ template and corresponding SPARQL template using UUID
        List<String> CQTList = new ArrayList<>();
        String query = "";
        if(inputCQTs.containsKey("cqs")){
            CQTList = (List<String>) inputCQTs.get("cqs");
        } else {
            System.out.println("Error: no cqs field!");
        }
        if(inputCQTs.containsKey("query")){
            query = inputCQTs.getString("query");
        } else {
            System.out.println("Error: no query field!");
        }
        System.out.println("CQTList: " + CQTList.toString());
        System.out.println("query: " + query);
        System.out.println();

        // write into MySQL

    }

    public static void DBQuery(){
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT * FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Email: " + email);
                System.out.println();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
