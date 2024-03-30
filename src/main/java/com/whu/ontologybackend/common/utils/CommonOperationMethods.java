package com.whu.ontologybackend.common.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonOperationMethods {
    public static boolean isDecimal(String str){
        return str.matches("^0*(\\d+\\.\\d*|\\.\\d+)$");
    }

    public static void parseXSDElements(Element element, int parentId){
        NodeList children = element.getChildNodes();
        for(int i = 0; i < children.getLength(); i++){
            if(children.item(i) instanceof Element){
                Element child = (Element) children.item(i);
                int id = generateIntUUID();
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
                // TODO: insert element into the element table

                // TODO: insert relation into the relation table

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
}
