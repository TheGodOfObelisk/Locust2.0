package com.whu.ontologybackend.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.whu.ontologybackend.common.structured.XSDElement;
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

    public static List<String> getCQTemplates(){
        List<String> cqTemplateList = new ArrayList<>();
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT CQTemplate FROM cqtemplates";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String cqt = resultSet.getString("CQTemplate");
                cqTemplateList.add(cqt);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return cqTemplateList;
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

    // rootId contains Root substring, others are all in UUID form
    public static List<String> extractXSDRootIdList(){
        List<String> rootIdList = new ArrayList<>();
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT id FROM element WHERE id LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%Root");
            try(ResultSet rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    System.out.println("Id: " + rs.getString("id"));
                    rootIdList.add(rs.getString("id"));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rootIdList;
    }

    public static XSDElement extractXSDElementById(String nodeId){
        XSDElement xsdElement = new XSDElement();
        try(Connection connection = DatabaseOperationMethods.getDBConnection()){
            String sql = "SELECT * FROM element WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nodeId);
            try(ResultSet rs = preparedStatement.executeQuery()){
                int size = 0;
                while(rs.next()){
                    xsdElement.setId(rs.getString("id"));
                    xsdElement.setName(rs.getString("name"));
                    xsdElement.setType(rs.getString("type"));
                    xsdElement.setMinOccurs(rs.getInt("minOccurs"));
                    xsdElement.setMaxOccurs(rs.getInt("maxOccurs"));
                    xsdElement.setParentId(rs.getString("parent_id"));
                    size++;
                    if(size > 1){
                        System.out.println("Error: more than one elements match nodeId: " + nodeId);
                    }
                } // there should be only one
                return xsdElement;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> extractSubXSDElementIdsByParentId(String parentId){
        List<String> subElementIds = new ArrayList<>();
        try(Connection connection = getDBConnection()){
            String sql = "SELECT child_id FROM relation WHERE parent_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, parentId);
            try(ResultSet rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    String subElementId = rs.getString("child_id");
                    subElementIds.add(subElementId);
                }
                return subElementIds;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return subElementIds;
    }

    // true: not duplicated; false: duplicated
    public static boolean checkDuplicatedMaterializeCQ(String CQContent){
        try(Connection connection = getDBConnection()){
            String sql = "SELECT * FROM materializedcq WHERE MaterializedCQ = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CQContent);
            try(ResultSet rs = preparedStatement.executeQuery()){
                if (!rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void storeInputCQs(String CQContent){
        try(Connection connection = getDBConnection()){
            String sql = "INSERT INTO materializedcq (ID, MaterializedCQ) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, CQContent);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
