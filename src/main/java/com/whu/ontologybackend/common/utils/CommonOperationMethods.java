package com.whu.ontologybackend.common.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonOperationMethods {
    public static boolean isDecimal(String str){
        return str.matches("^0*(\\d+\\.\\d*|\\.\\d+)$");
    }

    public static void parseXSDRootElement(Element element, String rootId){
        NamedNodeMap namedNodeMap = element.getAttributes();
        List<String> nodeNameList = new ArrayList<>();
        for(int j = 0; j < namedNodeMap.getLength(); j++){
            Node node = namedNodeMap.item(j);
            System.out.println("node name in namedNodeMap: " + node.getNodeName());
            nodeNameList.add(node.getNodeName());
        }
        String name="", type="";
        int minOccurs=0, maxOccurs=-2; // -2 means uninitialized, -1 means unbounded
        if(nodeNameList.contains("name")){
            name = element.getAttribute("name");
        }
        if(nodeNameList.contains("type")){
            type = element.getAttribute("type");
        }
        if(nodeNameList.contains("minOccurs")){
            try{
                minOccurs = Integer.parseInt(element.getAttribute("minOccurs"));
            } catch (Exception e){
                e.printStackTrace();
                minOccurs = 0;
            }
        }
        if(nodeNameList.contains("maxOccurs")){
            if(element.getAttribute("maxOccurs").equals("unbounded")){
                System.out.println("maxOccurs = unbounded");
            } else {
                try{
                    maxOccurs = Integer.parseInt(element.getAttribute("maxOccurs"));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        DatabaseOperationMethods.insertXSDElement(rootId, name, type, minOccurs, maxOccurs, rootId); // root has no parent
    }

    public static void parseXSDElements(Element element, String parentId){
        NodeList children = element.getChildNodes();
        for(int i = 0; i < children.getLength(); i++){
            if(children.item(i) instanceof Element){
                Element child = (Element) children.item(i);
//                int id = generateIntUUID();
                String id = UUID.randomUUID().toString();
                NamedNodeMap namedNodeMap = child.getAttributes();
                List<String> nodeNameList = new ArrayList<>();
                for(int j = 0; j < namedNodeMap.getLength(); j++){
                    Node node = namedNodeMap.item(j);
                    System.out.println("node name in namedNodeMap: " + node.getNodeName());
                    nodeNameList.add(node.getNodeName());
                }
                String name="", type="";
                int minOccurs=0, maxOccurs=-2; // -2 means uninitialized, -1 means unbounded
                if(nodeNameList.contains("name")){
                    name = child.getAttribute("name");
                }
                if(nodeNameList.contains("type")){
                    type = child.getAttribute("type");
                }
                if(nodeNameList.contains("minOccurs")){
                    try{
                        minOccurs = Integer.parseInt(child.getAttribute("minOccurs"));
                    } catch (Exception e){
                        e.printStackTrace();
                        minOccurs = 0;
                    }
                }
                if(nodeNameList.contains("maxOccurs")){
                    if(child.getAttribute("maxOccurs").equals("unbounded")){
                        System.out.println("maxOccurs = unbounded");
                    } else {
                        try{
                            maxOccurs = Integer.parseInt(child.getAttribute("maxOccurs"));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                // element with empty name or type should be filtered
                // TODO: insert element into the element table
                DatabaseOperationMethods.insertXSDElement(id, name, type, minOccurs, maxOccurs, parentId);
                // TODO: insert relation into the relation table
//                int relId = CommonOperationMethods.generateIntUUID();
                String relId = UUID.randomUUID().toString();
                DatabaseOperationMethods.insertXSDElementRelation(relId, parentId, id);

                parseXSDElements(child, id);
            }
        }
    }

    public static int generateIntUUID(){
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        int intUUID = uuidStr.hashCode();
        return intUUID;
    }

    public static List<File> fetchFilesInDir(File modulePath){
        List<File> fileList = new ArrayList<>();
        if(modulePath.isFile()){
            fileList.add(modulePath);
        } else if(modulePath.isDirectory()){
            File[] files = modulePath.listFiles();
            if(files != null){
                for(File file: files){
                    if(file.getName().contains(".xsd")){
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }

    public static List<File> fetchOntologiesInDir(File path){
        List<File> fileList = new ArrayList<>();
        if(path.isFile()){
            fileList.add(path);
        } else if(path.isDirectory()){
            File[] files = path.listFiles();
            if(files != null){
                for(File file: files){
                    if(file.getName().contains(".owl")){
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }
}
