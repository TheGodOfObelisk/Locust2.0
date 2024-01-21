package com.whu.ontologybackend.common.structured;


import com.whu.ontologybackend.common.utils.OntologyOperationMethods;
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
import org.apache.jena.util.iterator.ExtendedIterator;


import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


public class OntMultiwayTree implements Serializable {
    private OntMultiwayTreeNode root;

    // modular ontology identifiers
    // module name should be set during initialization or update process
    private String moduleName = "default module";

    private String moduleId = "default id";

    private String rootConcept = "thing"; // at level 1

    private String rootNodeID = "root"; // at level 0

    private boolean isImportedOntology = false;

    // URI string for OWL DL
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getRootConcept() {
        return rootConcept;
    }

    public void setRootConcept(String rootConcept) {
        this.rootConcept = rootConcept;
    }

    public String getRootNodeID() {
        return rootNodeID;
    }

    public void setRootNodeID(String rootNodeID) {
        this.rootNodeID = rootNodeID;
    }

    public boolean isImportedOntology() {
        return isImportedOntology;
    }

    public void setImportedOntology(boolean importedOntology) {
        isImportedOntology = importedOntology;
    }

    public String getOurURI() {
        return ourURI;
    }

    public void setOurURI(String ourURI) {
        this.ourURI = ourURI;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getNS() {
        return NS;
    }

    public void setNS(String NS) {
        this.NS = NS;
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
//        for(int i = 3, j = 1; i < classPath.length; i++, j++){
//            combinedClassPath.add(j, classPath[i]);
//        }
        for(int i = 0; i < classPath.length; i++){
            combinedClassPath.add(classPath[i]);
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
        // TODO:
        // utilize addChild() method
        // initialize new node and insert it into the ontology multi-way tree
        OntMultiwayTreeNode tmpOntMultiwayTreeNode = this.getRoot();
        if(tmpOntMultiwayTreeNode.getChildList().size() == 0){
            // has not been initialized or error
            // generate UUID of thing node, only once
            OntTreeNode thingOntTreeNode = new OntTreeNode(UUID.randomUUID().toString(), rootNodeID);
            NodeData thingNodeData = new NodeData();
            thingNodeData.setConcept("thing"); // actually, depth=1, the level under root
            thingOntTreeNode.setNodeData(thingNodeData);
            OntMultiwayTreeNode thingOntMultiwayTreeNode = new OntMultiwayTreeNode(thingOntTreeNode);
            List<OntMultiwayTreeNode> initChildList = new ArrayList<OntMultiwayTreeNode>();
            initChildList.add(thingOntMultiwayTreeNode);
            tmpOntMultiwayTreeNode.setChildList(initChildList);
//            this.addChild(tmpOntMultiwayTreeNode, thingOntTreeNode);
            // add child failed! please check
            System.out.println(this.toString());
        } else if(tmpOntMultiwayTreeNode.getChildList().size() > 1){
            System.out.println("ERROR: duplicated \"thing\" node.");
        }
        tmpOntMultiwayTreeNode = tmpOntMultiwayTreeNode.getChildList().get(0);
        // depth 0: root
        // depth 1: thing equals combinedClassPath[0]
        // depth 2: combinedClassPath[1]
        int depth = 1;
        // obtain ID of the "thing" concept
        String parentID = tmpOntMultiwayTreeNode.getData().getNodeId();
        for(int i = 0; i < combinedClassPath.size(); i++){
            if(combinedClassPath.get(i).equals("thing")){
                // already handled it
                continue;
            }
            if(i == (combinedClassPath.size() - 1)){
                // reach the leaf concept/jargon
                // last cycle of the for loop, handle multiple attributes besides the "concept" field
                String currentNodeID = UUID.randomUUID().toString();
                OntTreeNode curOntTreeNode = new OntTreeNode(currentNodeID, parentID);
//                parentID = curOntTreeNode.getNodeId();
                NodeData curNodeData = new NodeData();
                // set multiple attributes of the concept, use reflection
                // **start**
                String classFullPath = OntologyOperationMethods.obtainFullClassPath(combinedClassPath);
                // TODO: DELETE JAVA REFLECTION OPERATIONS
                /*
                Class cl;
                try{
                    cl = Class.forName(classFullPath);
                    // Fields to dataProperties
                    Field[] fields = cl.getDeclaredFields();
                    for(Field f: fields){
                        Class type = f.getType();
                        String name = f.getName();
                        String modifiers = Modifier.toString(f.getModifiers());
                        Map<String, Object> map = new HashMap<>();
                        map.put(name, type.getName());
                        map.put("modifier", modifiers);
                        curNodeData.getDataProperties().add(map);
                        // in what circumstances...
                    }
                    // Methods to objectProperties
                    Method[] methods = cl.getDeclaredMethods();
                    for(Method m: methods){
                        Class retType = m.getReturnType();
                        String name = m.getName();
                        String modifiers = Modifier.toString(m.getModifiers());
                        Class[] parameterTypeClasses = m.getParameterTypes();
                        // object property should connect two objects
                        // in one circumstance: this.concept is the source object
                        // and retTpye concept is the target object (retType is a Class, other than basic data type)
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name); // name of the objectProperty
                        map.put("modifier", modifiers);
                        map.put("targetType", retType); // assume retType is the target type
                        List<String> parameterTypes = new ArrayList<>();
                        for(Class c: parameterTypeClasses){
                            parameterTypes.add(c.getName());
                        }
                        map.put("parameterTypes", parameterTypes);
                        curNodeData.getObjectProperties().add(map);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                */
                curNodeData.setConcept(combinedClassPath.get(i));
                if(conceptMap.containsKey(curNodeData.getConcept())){
//                    conceptMap.get(curNodeData.getConcept()).add(classFullPath);
                    // the concept already exists, there should be more than one class full path

                    if(!conceptMap.get(curNodeData.getConcept()).keySet().contains(currentNodeID)){
                        conceptMap.get(curNodeData.getConcept()).put(currentNodeID, classFullPath);
                    }
                } else{
                    List<String> tmpList = new ArrayList<>();
                    tmpList.add(classFullPath);
                    Map<String, String> tmpMap = new HashMap<>();
                    tmpMap.put(currentNodeID, classFullPath);
                    conceptMap.put(curNodeData.getConcept(), tmpMap);
//                    conceptMap.put(curNodeData.getConcept(), tmpList);
                }
                // ***end***
                curOntTreeNode.setNodeData(curNodeData);
                OntMultiwayTreeNode curOntMultiwayTreeNode = new OntMultiwayTreeNode(curOntTreeNode);
                tmpOntMultiwayTreeNode.getChildList().add(curOntMultiwayTreeNode);


                System.out.println(combinedClassPath.get(i));

                break;
            }
            // process potential duplications
            boolean duplicated = false;
            if(tmpOntMultiwayTreeNode.getChildList().size() != 0){
                for(OntMultiwayTreeNode tmp : tmpOntMultiwayTreeNode.getChildList()){
                    if(tmp.getData().getNodeData().getConcept().equals(combinedClassPath.get(i))) {
                        System.out.println("duplicated!");
                        tmpOntMultiwayTreeNode = tmp;
                        parentID = tmp.getData().getNodeId(); // update
                        duplicated = true;
                        break;
                    }
                }
                // duplicated
                if(duplicated){
                    continue;
                }
            }
            // process the directories
            OntTreeNode curOntTreeNode = new OntTreeNode(UUID.randomUUID().toString(), parentID);
            // update parentID
            parentID = curOntTreeNode.getNodeId();
            NodeData curNodeData = new NodeData();
            curNodeData.setConcept(combinedClassPath.get(i));
            curOntTreeNode.setNodeData(curNodeData);
            OntMultiwayTreeNode curOntMultiwayTreeNode = new OntMultiwayTreeNode(curOntTreeNode);
            tmpOntMultiwayTreeNode.getChildList().add(curOntMultiwayTreeNode);
            tmpOntMultiwayTreeNode = curOntMultiwayTreeNode;

            // check whether the directories actually exist
            // !!!
        }
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

    public void updateByExistingOntology(OntModel m) throws ReflectiveOperationException, IOException {
        // integrate elements into the target tree
        // 1, classes; 2, data properties; 3, object properties
        // Step 1: process classes in the first hierarchy
        ExtendedIterator<OntClass> ontClassExtendedIterator = m.listHierarchyRootClasses();
        while(ontClassExtendedIterator.hasNext()){
            OntClass presentOntClass = ontClassExtendedIterator.next();
            String presentClassName = presentOntClass.getLocalName();
//            String fullClassName = "thing." + presentClassName;
            // update the node itself and then update its subClasses
            updateTree(presentClassName);
            if(presentOntClass.getSubClass() != null){
                updateSubClasses2Tree(presentOntClass, "");
            }
        }
        // Step 2: complement all the subclasses

        // Step 3: process data properties

        // Step 4: process object properties
        // reconsider altering data structure

    }

    private void updateSubClasses2Tree(OntClass presentOntClass, String classFullName) throws ReflectiveOperationException, IOException {
        if(classFullName.equals("")){
            updateTree(presentOntClass.getLocalName());
        } else {
            classFullName = classFullName + "." + presentOntClass.getLocalName();
            updateTree(classFullName);
        }
        ExtendedIterator<OntClass> subClassesIterator = presentOntClass.listSubClasses(true);
        while(subClassesIterator.hasNext()){
            OntClass subOntClass = subClassesIterator.next();
            updateSubClasses2Tree(subOntClass, classFullName);
        }
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
