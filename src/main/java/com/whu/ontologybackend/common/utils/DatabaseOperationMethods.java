package com.whu.ontologybackend.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import org.w3c.dom.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class DatabaseOperationMethods {

    private static final String URL = "jdbc:mysql://localhost:3306/myontology";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static int cqtsum = 0;
    private static int relsum = 0;

    private static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void DBInit(String path){
        System.out.println("Begin to initialize DB.");
        File folder = new File(path);
        traverseFiles(folder);
        System.out.println("DB has been initialized.");
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
//        System.out.println("CQTList: " + CQTList.toString());
//        System.out.println("query: " + query);
//        System.out.println();
        Set<String> CQTSet = new HashSet<>(CQTList);
        // write into MySQL
        // step 1: connect
        Connection connection = null;
        try{
            connection = DatabaseOperationMethods.getDBConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
        // step 2: insert SPARQL template
        UUID ID = UUID.randomUUID();
        try{
            String sql = "INSERT INTO SPARQLTEMPLATE (ID, SPARQLTemplate) " +
                    "VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ID.toString());
            preparedStatement.setString(2, query);
            preparedStatement.executeUpdate();
//            int rowAffected = preparedStatement.executeUpdate();
//            System.out.println(rowAffected + " row(s) has been inserted.");
        } catch (SQLException e){
            e.printStackTrace();
        }
        // step 3: insert related CQ templates and update relation table
        UUID CQT_ID;
        UUID REL_ID;
        try{
            String sql = "INSERT INTO CQTEMPLATES (ID, CQTemplate) " +
                    "VALUES (?, ?)";
            for(String cqt : CQTSet){
                CQT_ID = UUID.randomUUID();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, CQT_ID.toString());
                preparedStatement.setString(2, cqt);
                preparedStatement.executeUpdate();
//                int rowAffected = preparedStatement.executeUpdate();
//                System.out.println(rowAffected + " CQT has been inserted");
                // for each cqt update the relation table
                cqtsum++;
                REL_ID = UUID.randomUUID();
                String rel_sql = "INSERT INTO CQT_SPARQLT_REL (ID, CQTEMPLATEID, SPARQLTEMPLATEID) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement preparedStatement4rel = connection.prepareStatement(rel_sql);
                preparedStatement4rel.setString(1, REL_ID.toString());
                preparedStatement4rel.setString(2, CQT_ID.toString());
                preparedStatement4rel.setString(3, ID.toString());
                preparedStatement4rel.executeUpdate();
                relsum++;
//                rowAffected = preparedStatement4rel.executeUpdate();
//                System.out.println(rowAffected + " relation has been inserted.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        if(cqtsum != relsum){
            System.out.println("Error: they should equal.");
        } else {
            cqtsum = 0;
            relsum = 0;
        }
        // step 4: disconnect
        try{
            // autocommit = true
            // connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public static boolean checkCQTemplate(String CQT){
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT * FROM CQTEMPLATES WHERE CQTemplate = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, CQT);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkSPARQLTemplate(String SPARQLT){
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT * FROM SPARQLTEMPLATE WHERE SPARQLTemplate = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, SPARQLT);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkXSDRootID(String rootId){
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT * FROM element WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, rootId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void insertXSDElement(String id, String name, String type, int minOccurs, int maxOccurs, String parentID){
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "INSERT INTO element (id, name, type, minOccurs, maxOccurs, parent_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, type);
            preparedStatement.setInt(4, minOccurs);
            preparedStatement.setInt(5, maxOccurs);
            preparedStatement.setString(6, parentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertXSDElementRelation(String id, String parentId, String childId){
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "INSERT INTO relation (id, parent_id, child_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, parentId);
            preparedStatement.setString(3, childId);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
