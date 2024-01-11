package com.whu.ontologybackend.common.structured;


import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.base.Sys;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;


import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class OntMultiwayTree implements Serializable {
    private OntMultiwayTreeNode root;

    private String rootConcept = "thing"; // at level 1

    private String rootNodeID = "root"; // at level 0

    private String ourURI = "http://www.w3.org/TR/owl-features/#term_OWLDL";
    private String SOURCE = "http://www.whu.edu/cybersecurity/ontology";
    private String NS = SOURCE + "#";

    // record the quantity of duplicated concepts when initialing the multiway tree
    // Map<concept, Map<nodeId, classFullPath>>
    private Map<String, Map<String, String>> conceptMap = new HashMap<>();


    public Map<String, Map<String, String>> getConceptMap() {
        return conceptMap;
    }

    public void setConceptMap(Map<String, Map<String, String>> conceptMap) {
        this.conceptMap = conceptMap;
    }

    public OntMultiwayTree() {
        // perhaps not nodeId
        root = new OntMultiwayTreeNode(new OntTreeNode("root"));
    }

    // generate a multi-way tree of ontology architecture
    //
    public OntMultiwayTree createTree(List<OntTreeNode> treeNodes) {
        if (treeNodes == null || treeNodes.size() < 0)
            return null;

        OntMultiwayTree ontMultiwayTree = new OntMultiwayTree();

        for (OntTreeNode treeNode : treeNodes) {
            if (treeNode.getParentId().equals("root")) {
                // add a node to the root
                ontMultiwayTree.getRoot().getChildList().add(new OntMultiwayTreeNode(treeNode));
            } else {
                addChild(ontMultiwayTree.getRoot(), treeNode);
            }
        }
        return ontMultiwayTree;
    }

    public void addChildDirectly(OntMultiwayTreeNode multiwayTreeNode, OntTreeNode child){
//        multiwayTreeNode.getChildList().;
        OntMultiwayTreeNode tmpNode = new OntMultiwayTreeNode(child);
        multiwayTreeNode.getChildList().add(tmpNode);
    }

    // precondition:
    public void addChild(OntMultiwayTreeNode multiwayTreeNode, OntTreeNode child) {
        for (OntMultiwayTreeNode item : multiwayTreeNode.getChildList()) {
            if (item.getData().getNodeId().equals(child.getParentId())) {
                // finds its parent node
                item.getChildList().add(new OntMultiwayTreeNode(child));
                break;
            } else {
                if (item.getChildList() != null && item.getChildList().size() > 0) {
                    addChild(item, child);
                }
            }
        }
    }

    public String traverseTree(OntMultiwayTreeNode ontMultiwayTreeNode) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");

        if (ontMultiwayTreeNode != null) {
            for (OntMultiwayTreeNode index : ontMultiwayTreeNode.getChildList()) {
                buffer.append(index.getData().getNodeId() + ",");

                if (index.getChildList() != null && index.getChildList().size() > 0) {
                    buffer.append(traverseTree(index));
                }
            }
        }
        buffer.append("\n");
        return buffer.toString();
    }

    public OntMultiwayTreeNode getRoot() {
        return root;
    }

    public void setRoot(OntMultiwayTreeNode root) {
        this.root = root;
    }

    // parameter: full class name
    // for example, com.whu.ontology.cee.xxx
    // dot symbol in class name indicates hierarchy relation
    // parse class using reflection and store exploitable information into the multi-way tree
    public void updateTree(String className) throws ReflectiveOperationException, IOException {
        // split className into a string array
        String[] classPath = className.split("\\.");
        // combine "com.whu.ontology" to "thing"
        ArrayList<String> combinedClassPath = new ArrayList<String>();
        combinedClassPath.add(0, rootConcept);
        for(int i = 3, j = 1; i < classPath.length; i++, j++){
            combinedClassPath.add(j, classPath[i]);
        }

        if(checkIsExist(combinedClassPath)){ // test
            System.out.println("existed!");
            // do nothing or update the fields of the node
        } else {
            System.out.println("not existed!");
            // use converted class path to update the tree
            // both leaf concept/jargon and directories
            updateTree(combinedClassPath);
        }
//        readClass(className);
    }

    public void updateTree(ArrayList<String> combinedClassPath){
        // deleted
    }

    public boolean checkIsExist(ArrayList<String> combinedClassPath){
        // ensure items in combinedClassPath are in right order
        // index from 0 to n-1: directory
        // index n: specific concept
        for(int i = 0; i < combinedClassPath.size() - 1; i++){
            System.out.println("directory path:");
            System.out.println(combinedClassPath.get(i));
        }
        System.out.println("concept item:");
        System.out.println(combinedClassPath.get(combinedClassPath.size()-1));

        OntMultiwayTreeNode tmpOntMultiwayTreeNode = this.getRoot();
        int depth = 0; // hierarchy depth, index from 0
        while(depth < combinedClassPath.size()){
            for(int i = 0; i < tmpOntMultiwayTreeNode.getChildList().size(); i++){
                if(combinedClassPath.get(depth).equals(tmpOntMultiwayTreeNode.getChildList().get(i).getData().getNodeData().getConcept())){
                    System.out.println("match! continue.");
                    if(i == (tmpOntMultiwayTreeNode.getChildList().size()-1) && (depth == combinedClassPath.size()-1)){
                        System.out.println("full match! already exists!");
                        return true;
                    }
                } else {
                    System.out.println("mismatch, break.");
                    return false;
                }
            }
            depth++;
        }
        return false;
    }


    public void ontologyEnrichment(Map<String, Set<String>> resConceptsWithContext){
        System.out.println("enrich ontology multi-way tree from unstructured data source");

    }

    public void ontologyEnrichSoloConcept(String soloConcept){
        System.out.println("enrich ontology multi-way tree using solo concepts, the solo concept is " + soloConcept);
        if(soloConcept.contains("#")){ // illegal character
            soloConcept.replace("#", "");
        }
        OntTreeNode soloConceptNode = traverseTreeByConcept(this.root, soloConcept);
        if(soloConceptNode != null){
            System.out.println("combine solo concept and existing concepts");
            return;
        }
        // add this solo concept as a child of the root node (actually the thing node)
        OntTreeNode soloConceptTreeNodeLayer3 = traverseTreeByConcept(this.root, "soloConcept");
        if(soloConceptTreeNodeLayer3 == null){
            // add a node called "solo concept" under "thing" node
            OntTreeNode soloConceptTreeNode = new OntTreeNode(UUID.randomUUID().toString(), this.root.getChildList().get(0).getData().getNodeId()); // parent id = "thing" id
            NodeData soloConceptNodedata = new NodeData();
            soloConceptNodedata.setConcept("soloConcept");
            soloConceptTreeNode.setNodeData(soloConceptNodedata);
            OntMultiwayTreeNode soloConceptOntMultiwayTreeNode = new OntMultiwayTreeNode(soloConceptTreeNode);
            this.root.getChildList().get(0).getChildList().add(soloConceptOntMultiwayTreeNode);
        }
        soloConceptTreeNodeLayer3 = traverseTreeByConcept(this.root, "soloConcept");
        // then add child list
        OntTreeNode soloConceptTreeNode = new OntTreeNode(UUID.randomUUID().toString(), soloConceptTreeNodeLayer3.getNodeId()); // parent id = "solo concepts" id
        NodeData soloConceptNodedata = new NodeData();
        soloConceptNodedata.setConcept(soloConcept);
        soloConceptTreeNode.setNodeData(soloConceptNodedata);
//        traverseTreeByNodeId(this.root, soloConceptTreeNodeLayer3.getNodeId())
        addChildDirectly(traverseTreeByNodeIdV2(this.root, soloConceptTreeNodeLayer3.getNodeId()), soloConceptTreeNode);
    }

    // store the concept in OntTreeNode
    // traverse and find the specific node
    public OntTreeNode getConceptByNodeID(String nodeID){
        OntTreeNode ontTreeNode = new OntTreeNode(nodeID);
        // ...
        return ontTreeNode;
    }

    // the same implementation as traversing tree by node id
    public OntTreeNode traverseTreeByConcept(OntMultiwayTreeNode ontMultiwayTreeNode, String concept){
        if(ontMultiwayTreeNode == null){
            return null;
        }
        if(ontMultiwayTreeNode.getData().getNodeData() != null && ontMultiwayTreeNode.getData().getNodeData().getConcept().equals(concept)){
            return ontMultiwayTreeNode.getData();
        }

        for(OntMultiwayTreeNode index : ontMultiwayTreeNode.getChildList()){
            if(index.getData().getNodeData().getConcept().equals(concept)){
                return index.getData();
            }
            if(index.getChildList() != null && index.getChildList().size() > 0){
                OntTreeNode tmp = traverseTreeByConcept(index, concept);
                if(tmp != null){
                    return tmp;
                }
            }
        }
        return null;
    }

    public OntTreeNode traverseTreeByNodeId(OntMultiwayTreeNode ontMultiwayTreeNode, String nodeID){
        if(ontMultiwayTreeNode == null){
            return null;
        }

        if(ontMultiwayTreeNode.getData().getNodeId().equals(nodeID)){
            return ontMultiwayTreeNode.getData();
        }

        for(OntMultiwayTreeNode index : ontMultiwayTreeNode.getChildList()){
            if(index.getData().getNodeId().equals(nodeID)){
                return index.getData();
            }
            if(index.getChildList() != null && index.getChildList().size() > 0){
//                if(traverseTreeByNodeId(index, nodeID) != null)
//                return traverseTreeByNodeId(index, nodeID) == null ?
                OntTreeNode tmp = traverseTreeByNodeId(index, nodeID);
                if(tmp != null){
                    return tmp;
                } // else continue
            }
        }
        // no item matches
        return null;
    }

    // overload
    public OntMultiwayTreeNode traverseTreeByNodeIdV2(OntMultiwayTreeNode ontMultiwayTreeNode, String nodeID){
        if(ontMultiwayTreeNode == null){
            return null;
        }

        if(ontMultiwayTreeNode.getData().getNodeId().equals(nodeID)){
            return ontMultiwayTreeNode;
        }

        for(OntMultiwayTreeNode index : ontMultiwayTreeNode.getChildList()){
            if(index.getData().getNodeId().equals(nodeID)){
                return index;
            }
            if(index.getChildList() != null && index.getChildList().size() > 0){
                OntMultiwayTreeNode tmp = traverseTreeByNodeIdV2(index, nodeID);
                if(tmp != null){
                    return tmp;
                } // else continue
            }
        }
        // no item matches
        return null;
    }

    // traverse and delete the specific node
//    public boolean deleteConceptByNodeID(OntMultiwayTreeNode ontMultiwayTreeNode, String nodeID){
//        if(ontMultiwayTreeNode != null){
//            if(ontMultiwayTreeNode.getData().getNodeId().equals(nodeID)){
//
//                return true;
//            }
//            for(OntMultiwayTreeNode index : ontMultiwayTreeNode.getChildList()){
//                if(index.getData().getNodeId().equals(nodeID)){
//                    ontMultiwayTreeNode.getChildList().remove(index); // works?
//                    return true;
//                }
//                if(index.getChildList() != null && index.getChildList().size() > 0){
//                    return deleteConceptByNodeID(index, nodeID);
//                }
//            }
//        }
//        return false;
//    }

    public boolean deleteConceptByNodeID(OntMultiwayTreeNode ontMultiwayTreeNode, String nodeID){
        OntMultiwayTreeNode targetTreeNode = traverseTreeByNodeIdV2(ontMultiwayTreeNode, nodeID);
        OntMultiwayTreeNode parentTreeNode = traverseTreeByNodeIdV2(ontMultiwayTreeNode, targetTreeNode.getData().getParentId());
        for(OntMultiwayTreeNode index : parentTreeNode.getChildList()){
            if(index.getData().getNodeId().equals(nodeID)){
                parentTreeNode.getChildList().remove(index);
                // a test
                if(traverseTreeByNodeIdV2(ontMultiwayTreeNode, nodeID) != null){
                    System.out.println("failed to delete... not reference parameter");
                }
                return true;
            }
        }
        return false;
    }

    public OntModel exportOntologyRDF(){
//        String resRDF = new String();
        // to create an ontology model for a particular language, but leaving all other values as defaults
        // pass the URI of the ontology language to the model factory

        // create an empty model
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // create the resource, convey and process the resource object
        Resource targetOntology = model.createResource(ourURI);

        traverseTreeWithResource(this.root, model);
        // after processing, model has been enriched

        // fill in resRDF with Ontology structure
        return model;
    }

    public Resource mapTree2Resource(Resource resource){

        return resource;
    }

    public void traverseTreeWithResource(OntMultiwayTreeNode ontMultiwayTreeNode, OntModel model){
        if(ontMultiwayTreeNode == null){
            return ;
        } else if(ontMultiwayTreeNode.getChildList().size() == 0){ // just test leaf node
            String nodeId = ontMultiwayTreeNode.getData().getNodeId();
            String concept = ontMultiwayTreeNode.getData().getNodeData().getConcept();
            List<Map<String, Object>> dpList = ontMultiwayTreeNode.getData().getNodeData().getDataProperties();
            List<Map<String, Object>> opList = ontMultiwayTreeNode.getData().getNodeData().getObjectProperties();
            Map<String, Object> axioms = ontMultiwayTreeNode.getData().getNodeData().getAxioms();
            // first, process nodeId and concept

            OntClass tmpClass = model.createClass(NS + concept);
            DatatypeProperty nodeIdProperty = model.createDatatypeProperty(NS + "nodeId");
            DatatypeProperty parentNodeIdProperty = model.createDatatypeProperty(NS + "parentNodeId");
//            nodeIdProperty.addDomain();
            tmpClass.addProperty(nodeIdProperty, nodeId);
            if(!nodeId.equals("root")){
                tmpClass.addProperty(parentNodeIdProperty, ontMultiwayTreeNode.getData().getParentId());
                // get parent's concept
                OntTreeNode tmpNode = traverseTreeByNodeId(this.root, ontMultiwayTreeNode.getData().getParentId());
                if(tmpNode.getNodeData() != null){
                    tmpClass.addSuperClass(model.getOntClass(NS + tmpNode.getNodeData().getConcept()));
                } else {
                    tmpClass.addSuperClass(model.getOntClass(NS + "root"));
                }
                // establish hierarchical relationship between class in the target ontology
            }
            if(dpList.size() != 0){
                for(Map<String, Object> dp : dpList){
                    Set<String> keySet = dp.keySet();
                    for(String key : keySet){
                        DatatypeProperty keyProperty = model.createDatatypeProperty(NS + key);
                        // let alone domain and range
                        tmpClass.addProperty(keyProperty, dp.get(key).toString());
                    }
                }
            }
            if(opList.size() != 0){
                for(Map<String, Object> op : opList){
                    Set<String> keySet = op.keySet();
                    for(String key : keySet){
                        DatatypeProperty keyProperty = model.createDatatypeProperty(NS + key);
                        // let alone domain and range
                        tmpClass.addProperty(keyProperty, op.get(key).toString());
                    }
                }
            }
            if(axioms != null){
                Set<String> keySet = axioms.keySet();
                for(String key : keySet){
                    DatatypeProperty keyProperty = model.createDatatypeProperty(NS + key);
                    tmpClass.addProperty(keyProperty, (String) axioms.get(key));
                }
            }
            return ;
        } else {
            // refresh resource
            String nodeId = ontMultiwayTreeNode.getData().getNodeId();
            String concept = "";
            if(ontMultiwayTreeNode.getData().getNodeData() != null){
                concept = ontMultiwayTreeNode.getData().getNodeData().getConcept();
                List<Map<String, Object>> dpList = ontMultiwayTreeNode.getData().getNodeData().getDataProperties();
                List<Map<String, Object>> opList = ontMultiwayTreeNode.getData().getNodeData().getObjectProperties();
                Map<String, Object> axioms = ontMultiwayTreeNode.getData().getNodeData().getAxioms();
                // first, process nodeId and concept

                OntClass tmpClass = model.createClass(NS + concept);
                DatatypeProperty nodeIdProperty = model.createDatatypeProperty(NS + "nodeId");
                DatatypeProperty parentNodeIdProperty = model.createDatatypeProperty(NS + "parentNodeId");
//            nodeIdProperty.addDomain();
                tmpClass.addProperty(nodeIdProperty, nodeId);
                if(!nodeId.equals("root")){
                    tmpClass.addProperty(parentNodeIdProperty, ontMultiwayTreeNode.getData().getParentId());
                    // get parent's concept
                    OntTreeNode tmpNode = traverseTreeByNodeId(this.root, ontMultiwayTreeNode.getData().getParentId());
                    if(tmpNode.getNodeData() != null){
                        tmpClass.addSuperClass(model.getOntClass(NS + tmpNode.getNodeData().getConcept()));
                    } else {
                        tmpClass.addSuperClass(model.getOntClass(NS + "root"));
                    }
                    // establish hierarchical relationship between class in the target ontology
                }
                if(dpList.size() != 0){
                    for(Map<String, Object> dp : dpList){
                        Set<String> keySet = dp.keySet();
                        for(String key : keySet){
                            DatatypeProperty keyProperty = model.createDatatypeProperty(NS + key);
                            // let alone domain and range
                            tmpClass.addProperty(keyProperty, (String) dp.get(key));
                        }
                    }
                }
                if(opList.size() != 0){
                    for(Map<String, Object> op : opList){
                        Set<String> keySet = op.keySet();
                        for(String key : keySet){
                            DatatypeProperty keyProperty = model.createDatatypeProperty(NS + key);
                            // let alone domain and range
                            tmpClass.addProperty(keyProperty, (String) op.get(key));
                        }
                    }
                }
                if(axioms != null){
                    Set<String> keySet = axioms.keySet();
                    for(String key : keySet){
                        DatatypeProperty keyProperty = model.createDatatypeProperty(NS + key);
                        tmpClass.addProperty(keyProperty, (String) axioms.get(key));
                    }
                }
            } else {
                OntClass tmpClass = model.createClass(NS + "root");
                DatatypeProperty nodeIdProperty = model.createDatatypeProperty(NS + "nodeId");
                DatatypeProperty parentNodeIdProperty = model.createDatatypeProperty(NS + "parentNodeId");
                tmpClass.addProperty(nodeIdProperty, "root");
                tmpClass.addProperty(parentNodeIdProperty, "");
            }
        }

        if(ontMultiwayTreeNode.getChildList().size() == 0){ // just test leaf node
            return;
        }

        for (OntMultiwayTreeNode index : ontMultiwayTreeNode.getChildList()){
            if(index.getChildList() != null){
                traverseTreeWithResource(index, model);
//                if(tmp != null){
//                    return tmp;
//                }
            }
        }

//        return ;
    }
}
