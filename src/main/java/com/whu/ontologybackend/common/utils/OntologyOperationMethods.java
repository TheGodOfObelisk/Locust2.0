package com.whu.ontologybackend.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OntologyOperationMethods {
    public static void ontologyForestSerialization(){
        // serialization test
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D:\\test.out"));
            objectOutputStream.writeObject(GlobalVariables.ontMultiwayForest);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<OntMultiwayTree> ontologyForestDeserialization(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:\\test.out"));
            List<OntMultiwayTree> res = (List<OntMultiwayTree>) objectInputStream.readObject();
            return res;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void localThesaurusSerialization(){
        // serialization test
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D:\\localThesaurus.out"));
            objectOutputStream.writeObject(GlobalVariables.localThesaurus);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Set<Glossary> localThesaurusDeserialization(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:\\localThesaurus.out"));
            Set<Glossary> res = (Set<Glossary>)objectInputStream.readObject();
            return res;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void outputOntInfo(OntModel m) {
        ExtendedIterator<OntClass> ontClassExtendedIterator = m.listClasses();
//            ExtendedIterator<OntClass> ontClassExtendedIterator = m.listHierarchyRootClasses();
        // close iterators manually if it doesn't iterator until hasNext() returns false
        while(ontClassExtendedIterator.hasNext()){
            OntClass presentOntClass = ontClassExtendedIterator.next();

            System.out.println("Class name: " + presentOntClass.getLocalName());
            if(presentOntClass.getSuperClass() != null){
                System.out.println("SuperClass name: " + presentOntClass.getSuperClass().getLocalName());
            } else {
                System.out.println("This class has no super class.");
            }
            if(presentOntClass.getSubClass() != null){
                System.out.println("SubClass name: " + presentOntClass.getSubClass().getLocalName());
            } else {
                System.out.println("This class has no sub class.");
            }
            ExtendedIterator<OntProperty> ontPropertyExtendedIterator = presentOntClass.listDeclaredProperties(true);
            while(ontPropertyExtendedIterator.hasNext()){
                OntProperty presentOntProperty = ontPropertyExtendedIterator.next();
                System.out.println("Properties: " + presentOntProperty.getLocalName());
                System.out.println("Property Type: " + propertyType(presentOntProperty));
            }
        }
    }

    public static String propertyType(OntProperty ontProperty){
        String propertyType = "";
        if(ontProperty.isDatatypeProperty()){
            propertyType += "Data Property.";
        } else if (ontProperty.isObjectProperty()) {
            propertyType += "Object Property.";
        } else if (ontProperty.isFunctionalProperty()) {
            propertyType += "Functional Property.";
        } else if (ontProperty.isSymmetricProperty()){
            propertyType += "Symmetric Property.";
        } else if (ontProperty.isInverseFunctionalProperty()){
            propertyType += "Inverse Functional Property.";
        } else if (ontProperty.isTransitiveProperty()){
            propertyType += "Transitive Property.";
        } else if (ontProperty.isAnnotationProperty()) {
            propertyType += "Annotation Property.";
        }
        return propertyType;
    }

    public static void ontMetadataProperties(OntModel m){
        String base = "http://idea.rpi.edu/malont";
        Ontology ont = m.getOntology(base);

        // list the ontology imports
        for(String imp : ont.getOntModel().listImportedOntologyURIs()){
            System.out.println("Ontology " + base + " imports " + imp);
        }
    }

    public static void listOntologyResources(OntModel m){
        OntModel mBase = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, m.getBaseModel());

        for(Iterator i = mBase.listOntologies(); i.hasNext();){
            Ontology ont = (Ontology) i.next();
            // m's base model has ont as an import ...
            // processing ...
        }
    }

    // module name and module id are user inputs
    public static void integrateExistingOntology(OntModel m, String moduleName, String moduleId) throws ReflectiveOperationException, IOException {
        OntMultiwayTree targetTree = new OntMultiwayTree();
        // case 1: first initialization and import an existing ontology
        // case 2: not first initialization and import an existing ontology
        // case 3: not first initialization and this existing ontology has been imported before
        // operation 1: initialize and set the only one multi-way tree's attributes
        // operation 2: add a new multi-way tree to the forest
        // operation 3: update the corresponding multi-way tree that was created before
        if(GlobalVariables.ontMultiwayForest.size() == 1 && GlobalVariables.ontMultiwayForest.get(0).getModuleName().equals("default module")){
            // initialize it.
            targetTree = GlobalVariables.ontMultiwayForest.get(0);
            targetTree.setModuleName(moduleName);
            targetTree.setModuleId(moduleId);
            targetTree.setImportedOntology(true);
        } else if(GlobalVariables.ontMultiwayForest.size() == 1 && !GlobalVariables.ontMultiwayForest.get(0).getModuleName().equals("default Module")){
            // add targetTree to the global forest
            targetTree.setModuleName(moduleName);
            targetTree.setModuleId(moduleId);
            targetTree.setImportedOntology(true);
        } else if(GlobalVariables.ontMultiwayForest.size() > 1){
            for(OntMultiwayTree tmpTree : GlobalVariables.ontMultiwayForest){
                if(tmpTree.getModuleId().equals(moduleId) && tmpTree.getModuleName().equals(moduleName)){
                    targetTree = tmpTree;
                    break;
                }
            }
            // recheck
            if(!targetTree.getModuleName().equals(moduleName) || !targetTree.getModuleId().equals(moduleId)){
                targetTree.setModuleName(moduleName);
                targetTree.setModuleId(moduleId);
                targetTree.setImportedOntology(true);
            }
        }

        // further update operations should be implemented in OntMultiwayTree's methods
        targetTree.updateByExistingOntology(m);
        // target tree in the forest has been updated, rewriting is unnecessary.
        // append targetTree to the forest
        boolean append = true;
        for(int i = 0; i < GlobalVariables.ontMultiwayForest.size(); i++){
            if(GlobalVariables.ontMultiwayForest.get(i).getModuleName().equals(targetTree.getModuleName()) && GlobalVariables.ontMultiwayForest.get(i).getModuleId().equals(targetTree.getModuleId())){
                append = false;
                // may lose data properties
                GlobalVariables.ontMultiwayForest.set(i, targetTree);
                break;
            }
        }
        if(append){
            GlobalVariables.ontMultiwayForest.add(targetTree);
        }
        return;
    }

    public static void synchronizeTermsFromArticles2localThesaurus(Map<String, Double> extractedTerms){
        Set<String> terms = extractedTerms.keySet();
        // default to be i in that i should be the most cases
        // set the classification due to the belief that terms extracted from articles tend to be instances
        for(String term :terms){
            Glossary candidateTerm = new Glossary();
            candidateTerm.setLabel("i");
            candidateTerm.setWord(term);
            candidateTerm.setDescription(term + ": from inputted articles.");
            GlobalVariables.localThesaurus.add(candidateTerm);
        }

    }

    public static void synchronizeInputGlossaries2localThesaurus(JSONObject inputGlossaries){
        // synchronize inputted glossaries to local thesaurus
        // only handle two types of thesaurus
        Set<String> glossaries = inputGlossaries.keySet();
        for(String glossary : glossaries){
            JSONObject body = inputGlossaries.getJSONObject(glossary);
            String description = "";
            if(body.containsKey("description")){
                description = body.getString("description"); // from globalknowledge-cybersecurity.json
            } else if(body.containsKey("Definition")){
                description = body.getString("Definition"); // from NICCS_3.json
            }
            // how to handle those candidate terms with the same "word" field?
            Glossary candidateTerm = new Glossary();
            candidateTerm.setWord(glossary);
            candidateTerm.setDescription(description);
            // label 'u' represents 'unknown'
            // use 'c' instead of 'u' due to the belief that the terms listed in thesaurus should be concepts
            candidateTerm.setLabel("c"); // after Term Typing, it should be set according to the result of Term Typing
            GlobalVariables.localThesaurus.add(candidateTerm);
        }
    }

    // drop Java class parser
//    public static String obtainFullClassPath(ArrayList<String> combinedClassPath){
//        if(combinedClassPath.size() == 0){
//            return "";
//        } else if(combinedClassPath.size() == 1 && combinedClassPath.get(0).equals("thing")){
//            return "com.whu.ontology";
//        }
//        String fullClassPath = new String("com.whu.ontology");
//        for(int i = 1; i < combinedClassPath.size(); i++){
//            fullClassPath += ("." + combinedClassPath.get(i));
//        }
//        return fullClassPath;
//    }

    public static String obtainFullClassPath(ArrayList<String> combinedClassPath){
        if(combinedClassPath.size() == 0){
            return "";
        }
//        else if(combinedClassPath.size() == 1 && combinedClassPath.get(0).equals("thing")){
//            return "com.whu.ontology";
//        }
        String fullClassPath = new String("thing");
        for(int i = 1; i < combinedClassPath.size(); i++){
            fullClassPath += ("." + combinedClassPath.get(i));
        }
        return fullClassPath;
    }

    public static void synchronizeTerms2LocalThesaurus(){
        // extract terms from ontology forest to local thesaurus
        Set<Glossary> termsFromOF = new HashSet<>();
        for(OntMultiwayTree tmpTree : GlobalVariables.ontMultiwayForest){
            // TODO: implement extractTerms() method
            Set<Glossary> tmpTerms = tmpTree.extractTerms();
            termsFromOF.addAll(tmpTerms);
            // Also, add instances

        }

        GlobalVariables.localThesaurus.addAll(termsFromOF);
    }

    public static boolean checkCQTandSPARQLT(String filePrefix){
        try(BufferedReader reader = new BufferedReader(new FileReader(filePrefix + "cq_templates_only.txt"))){
            String CQTemplate;
            while((CQTemplate = reader.readLine()) != null){
                System.out.println(CQTemplate);
                if(!DatabaseOperationMethods.checkCQTemplate(CQTemplate)){
                    System.out.println("Error: incomplete CQTemplate db.");
                    return false;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(filePrefix + "sparqlowl_templates_only.txt"))){
            String SPARQLTemplate;
            while((SPARQLTemplate = reader.readLine()) != null){
                System.out.println(SPARQLTemplate);
                if(!DatabaseOperationMethods.checkSPARQLTemplate(SPARQLTemplate)){
                    System.out.println("Error: incomplete SPARQLTemplate db.");
                    return false;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> extractPlaceHoldersFromSPARQLT(String SPARQLTemplate){
        System.out.println("Ready to extract placeholders from SPARQL Template: " + SPARQLTemplate);
        String regex = "op\\d|c\\d|i\\d|dt\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(SPARQLTemplate);
        List<String> matches = new ArrayList<>();
        while(matcher.find()){
            matches.add(matcher.group());
        }
        for(String match:matches){
            System.out.println(match);
        }
        return matches;
    }

    public static void placeholderSetInit(Set<String> cSet, Set<String> opSet, Set<String> dtSet, Set<String> iSet){
        for(Glossary glossary : GlobalVariables.localThesaurus){
            if(glossary.getLabel().equals("c")){
                cSet.add(glossary.getWord());
            } else if(glossary.getLabel().equals("op")){
                opSet.add(glossary.getWord());
            } else if(glossary.getLabel().equals("dt")){
                dtSet.add(glossary.getWord());
            } else if(glossary.getLabel().equals("i")){
                iSet.add(glossary.getWord());
            } else {
                System.out.println("Unknown label, continue..");
            }
        }
        // instances should also be extracted from existing ontologies
    }

    public static void placeSequencePermutationInit(List<Map<String, String>> fullPermutation, Set<String> cSet, Set<String> opSet, Set<String> dtSet, Set<String> iSet, List<String> placeholders){
        List<String> cList = cSet.stream().toList();
        List<String> opList = opSet.stream().toList();
        List<String> dtList = dtSet.stream().toList();
        List<String> iList = iSet.stream().toList();
        System.out.println("cList size = " + cList.size() + ", opList size = " + opList.size() + ", dtList size = " + dtList.size() + ", iList size = " + iList.size());
        // TODO: use permutation of index to fill templates

        // a bad implementation, out of memory. I should use disk storage instead.
//        Map<String, String> onecase = new HashMap<>();
//        List<List<Integer>> fullP = new ArrayList<>();
//
//        for(int index = 0; index < placeholders.size(); index++){
//            String placeholder = placeholders.get(index);
//            if(placeholder.contains("c")){
//                for(int i = 0; i < cList.size(); i++){
//                    if(index == 0){
//                        List<Integer> newP = new ArrayList<>();
//                        newP.add(i);
//                        fullP.add(newP);
//                    } else {
//                        for(int j = 0; j < fullP.size(); j++){
//                            List<Integer> previousP = fullP.get(j);
//                            for(int k = 0; k < cList.size(); k++){
//                                List<Integer> newP2 = previousP;
//                                newP2.add(k);
//                                fullP.add(newP2);
//                            }
//                        }
//                    }
//                }
//            } else if (placeholder.contains("op")) {
//                for(int i = 0; i < opList.size(); i++){
//                    if(index == 0){
//                        List<Integer> newP = new ArrayList<>();
//                        newP.add(i);
//                        fullP.add(newP);
//                    } else {
//                        for(int j = 0; j < fullP.size(); j++){
//                            List<Integer> previousP = fullP.get(j);
//                            for(int k = 0; k < opList.size(); k++){
//                                List<Integer> newP2 = previousP;
//                                newP2.add(k);
//                                fullP.add(newP2);
//                            }
//                        }
//                    }
//                }
//            } else if (placeholder.contains("dt")){
//                for(int i = 0; i < dtList.size(); i++){
//                    if(index == 0){
//                        List<Integer> newP = new ArrayList<>();
//                        newP.add(i);
//                        fullP.add(newP);
//                    } else {
//                        for(int j = 0; j < fullP.size(); j++){
//                            List<Integer> previousP = fullP.get(j);
//                            for(int k = 0; k < dtList.size(); k++){
//                                List<Integer> newP2 = previousP;
//                                newP2.add(k);
//                                fullP.add(newP2);
//                            }
//                        }
//                    }
//                }
//            } else if (placeholder.contains("i")) {
//                for(int i = 0; i < iList.size(); i++){
//                    if(index == 0){
//                        List<Integer> newP = new ArrayList<>();
//                        newP.add(i);
//                        fullP.add(newP);
//                    } else {
//                        for(int j = 0; j < fullP.size(); j++){
//                            List<Integer> previousP = fullP.get(j);
//                            for(int k = 0; k < iList.size(); k++){
//                                List<Integer> newP2 = previousP;
//                                newP2.add(k);
//                                fullP.add(newP2);
//                            }
//                        }
//                    }
//                }
//            } else {
//                System.out.println("Unknown placeholder.");
//            }
//        }
//        System.out.println(fullP);
    }

    // read and parse xsd schemas from Database, then integrate schema into Global Variable ontology forest
    // duplicated modules may exist
    // chaos
    public static void integrateXSDModules2OntologyForest(String moduleSource){
        List<String> xsdRootIds = DatabaseOperationMethods.extractXSDRootIdList();
        if(moduleSource.equals("")){
            moduleSource = "defaultModule";
        }
        for(String rootId : xsdRootIds){
            System.out.println("Parse from rootId = " + rootId);
            boolean moduleExist = false;
            for(int index = 0; index < GlobalVariables.ontMultiwayForest.size(); index++){
                OntMultiwayTree curTree = GlobalVariables.ontMultiwayForest.get(index);
                if(curTree.getModuleId().equals(rootId) || curTree.getModuleName().equals(moduleSource)){
                    moduleExist = true;
                    curTree.setModuleId(rootId);
                    curTree.setModuleName(moduleSource);
                }
            }
            if(moduleExist){
                System.out.println("Module " + rootId + " has been integrated into ontology forest.");
                continue;
            }
            // TODO: analyze the xsd schema and synchronize it into the global ontology forest
            appendOntTreeFromXSD(rootId, moduleSource);
        }

    }

    public static void appendOntTreeFromXSD(String rootId, String moduleSource){
        // TODO: implement 1) analyze element table and relation table; 2) append a new OntMultiwayTree
        OntMultiwayTree ontMultiwayTree = new OntMultiwayTree();
//        String moduleId = UUID.randomUUID().toString();
        ontMultiwayTree.setModuleId(rootId);
        ontMultiwayTree.setModuleName(moduleSource);
        ontMultiwayTree.setImportedOntology(true);
        OntMultiwayTreeNode rootNode = ontMultiwayTree.getRoot();
        appendOntTreeFromXSDByElementId(rootNode, rootId);
        // ...

         GlobalVariables.ontMultiwayForest.add(ontMultiwayTree);
    }

    // in a mess
    // need to figure out what xsd element with empty name and type means
    // if the element is a data property, figure out which concept should it be attached to
    public static void appendOntTreeFromXSDByElementId(OntMultiwayTreeNode ontMultiwayTreeNode, String elementId){
        XSDElement xsdElement = DatabaseOperationMethods.extractXSDElementById(elementId);
        // do not need to define another class corresponding to element
        String nodeId = UUID.randomUUID().toString(); // useful except root node
//        OntTreeNode ontTreeNode = new OntTreeNode(nodeId, ontMultiwayTreeNode.);
        // process current xsd element
        if(null != xsdElement){
            // process root element
            if(xsdElement.getId().contains("Root")){
                System.out.println("This is the root element of xsd.");
                if(!ontMultiwayTreeNode.getData().getNodeId().equals("root")){
                    ontMultiwayTreeNode.getData().setNodeId("root");
                }
                if(xsdElement.getName() != null && xsdElement.getType() != null){
                    // it is a concept, it should be an empty concept
                    if(!xsdElement.getName().equals("") && xsdElement.getType().equals("")){
                        OntTreeNode ontTreeNode = new OntTreeNode("root", null);
                        NodeData nodeData = new NodeData();
                        nodeData.setConcept(xsdElement.getName());
                        ontTreeNode.setNodeData(nodeData);
                        ontMultiwayTreeNode.setData(ontTreeNode);
                    }
                    // it is an attribute, actually it should not be an attribute
                    if(!xsdElement.getName().equals("") && !xsdElement.getType().equals("")){
                        System.out.println("As a root concept, it shouldn't be a data property");
                    }
                    // ignore other circumstances
                }
            } else {
                // not root element
                if(xsdElement.getName() != null && xsdElement.getType() != null){
                    // new concept, added to child list
                    if(!xsdElement.getName().equals("") && xsdElement.getType().equals("")){
                        OntTreeNode ontTreeNode = new OntTreeNode(nodeId, ontMultiwayTreeNode.getData().getNodeId());
                        NodeData nodeData = new NodeData();
                        nodeData.setConcept(xsdElement.getName());
                        ontTreeNode.setNodeData(nodeData);
                        OntMultiwayTreeNode tmpTreeNode = new OntMultiwayTreeNode(ontTreeNode);
                        ontMultiwayTreeNode.getChildList().add(tmpTreeNode);
                        List<String> subElementIds = DatabaseOperationMethods.extractSubXSDElementIdsByParentId(elementId);
                        for(String subElementId : subElementIds){
                            appendOntTreeFromXSDByElementId(tmpTreeNode, subElementId);
                        }
                    }
                    // new data property
                    if(!xsdElement.getName().equals("") && !xsdElement.getType().equals("")){
                        Map<String, Object> dp = new HashMap<>();
                        dp.put(xsdElement.getName(), xsdElement.getType());
                        if(ontMultiwayTreeNode.getData().getNodeData() == null){
                            NodeData tmpNodeData = new NodeData();
                            ontMultiwayTreeNode.getData().setNodeData(tmpNodeData);
                        }
                        // null dplist may exist
                        if(ontMultiwayTreeNode.getData().getNodeData().getDataProperties() == null){
                            List<Map<String, Object>> tmpDpList = new ArrayList<>();
                            ontMultiwayTreeNode.getData().getNodeData().setDataProperties(tmpDpList);
                        }
                        ontMultiwayTreeNode.getData().getNodeData().getDataProperties().add(dp);
                    }
                    // empty node, ignore
                }
            }
        }
        List<String> subElementIds = DatabaseOperationMethods.extractSubXSDElementIdsByParentId(elementId);
        for(String subElementId : subElementIds){
            appendOntTreeFromXSDByElementId(ontMultiwayTreeNode, subElementId);
        }
    }

    public static void analyzeXLSFile(File excelFile, String moduleSource){
        List<String> header = new ArrayList<>();
        try(Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFile))){
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            // in CCE, header is set in the 3rd row
            if(excelFile.getName().contains("cce") || excelFile.getName().contains("CCE")){
                headerRow = sheet.getRow(2);
            }

            for(int i = 0; i < headerRow.getLastCellNum(); i++){
                Cell cell = headerRow.getCell(i);
                if(cell != null){
                    header.add(cell.getStringCellValue());
                }
            }
            System.out.println("*************attributes**************");
            for(String term : header){
                System.out.println(term);
            }
//            for(Row row: sheet){
//                for(Cell cell : row){
//                    String cellValue = getCellValueAsString(cell);
//                    System.out.println(cellValue + "\t");
//                }
//                System.out.println();
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: the same as csv, update or create a new OntMultiwayTree
        updateGlobalOFByHeader(moduleSource, header);
    }

    private static String getCellValueAsString(Cell cell){
        if(cell == null){
            return "";
        }
        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    public static void analyzeCSVFile(File excelFile, String moduleSource) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        // follow steps in essay 348
        List<String> header = new ArrayList<>();
        try(CSVParser csvParser = new CSVParser(new FileReader(excelFile), csvFormat)){
            header = csvParser.getHeaderNames();
            for(CSVRecord csvRecord : csvParser){
                System.out.println("--------csvRecord--------");
                System.out.println(csvRecord.stream().toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*************Attributes**********");
        System.out.println(header.toString());
        updateGlobalOFByHeader(moduleSource, header);
    }

    private static void updateGlobalOFByHeader(String moduleSource, List<String> header) {
        boolean updateModule = false;
        // empty string should not be a concept
        header = header.stream()
                       .filter(str -> !str.isEmpty())
                       .collect(Collectors.toList());
        for(OntMultiwayTree tmpTree : GlobalVariables.ontMultiwayForest){
            if(tmpTree.isTheSameModule(moduleSource)){
                updateModule = true;
                // TODO: update this tree
                // In general, there should be no update because the corresponding module has been created
                for(String term: header){
                    OntTreeNode resTreeNode = tmpTree.traverseTreeByConcept(tmpTree.getRoot(), term);
                    if(resTreeNode == null){
                        try {
                            tmpTree.updateTree(term);
                        } catch (IOException e){
                            e.printStackTrace();
                        } catch (ReflectiveOperationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(!updateModule){
            // TODO: take it as a new module, create a new OntMultiwayTree
            OntMultiwayTree ontMultiwayTree = new OntMultiwayTree();
            ontMultiwayTree.setModuleName(moduleSource);
            ontMultiwayTree.setModuleId(UUID.randomUUID().toString());
            for(String term: header){
                // update, no duplicated column in a csv
                try {
                    ontMultiwayTree.updateTree(term);
                } catch (IOException e){
                    e.printStackTrace();
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
            GlobalVariables.ontMultiwayForest.add(ontMultiwayTree);
            OntologyOperationMethods.synchronizeTerms2LocalThesaurus();
        }
    }

    public static boolean cqMatch(String cqContent, String cqTemplate, Map<String, Object> placeholderContentMap){
        // TODO: precisely match
        // without placeholders
        String regex = "dt\\d+|op\\d+|c\\d+|i\\d+";
        String[] parts = cqTemplate.split(regex);
        System.out.println("cqTemplate: " + cqTemplate);
        int incIndex = 0;
        for(String part: parts){
            System.out.println(part);
            if(cqContent.contains(part)){
                if(incIndex > cqContent.indexOf(part)){
                    // incorrect sequence
                    System.out.println("Mismatch");
                    return false;
                } else {
                    incIndex = cqContent.indexOf(part);
                }
            } else {
                System.out.println("Mismatch: no such substring.");
                return false;
            }
        }
        // Here, match succeeded. It is actually a cq.
        // with placeholders
        List<String> partsWithPlaceholders = new ArrayList<>();
        List<String> placeHolders = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cqTemplate);
        int start = 0;
        while(matcher.find()){
            partsWithPlaceholders.add(cqTemplate.substring(start, matcher.start()));
            partsWithPlaceholders.add(matcher.group());
            placeHolders.add(matcher.group());
            start = matcher.end();
        }
        partsWithPlaceholders.add(cqTemplate.substring(start));
        System.out.println("remain the parts matched. split again.");

        boolean previousPlaceholderContentExist = false;
        String previousPlaceholder = "";

        for(String part: partsWithPlaceholders){
            System.out.println(part);
            Matcher matchPlaceholder = pattern.matcher(part);
            if(matchPlaceholder.matches()){
                if(previousPlaceholderContentExist){
                    System.out.println("Fatal error: two subsequent placeholders...");
                    return false;
                }
                previousPlaceholder = part;
                previousPlaceholderContentExist = true;
                // match
            } else {
                // mismatch, remove textual content
                if(previousPlaceholderContentExist){
                    int nextIndex = cqContent.indexOf(part);
                    String placeholderContent = cqContent.substring(0, nextIndex);
                    if(!previousPlaceholder.equals("")){
                        placeholderContentMap.put(previousPlaceholder, placeholderContent);
                    } else {
                        System.out.println("Fatal error: placeholder has not been initialized!!");
                    }
                    previousPlaceholderContentExist = false; // already handle it
                } else {
                    cqContent = cqContent.replaceFirst(part, "");
                }
            }
        }
        System.out.println("Only placeholders.");
        // check Map
        for(String placeholder: placeHolders){
            System.out.println(placeholder);
            if(!placeholderContentMap.containsKey(placeholder)){
                System.out.println("Fatal error: missing placeholder(s)!!");
                return false;
            }
        }
        return true;
    }

    public static void synchronizePlaceholderContent2localThesaurus(Map<String, Object> placeholderContentMap){
        // check whether it exists, maybe update
        Set<String> placeholders = placeholderContentMap.keySet();
        for(String placeholder : placeholders){
            String content = (String) placeholderContentMap.get(placeholder);
            boolean checkDuplicated = false;
            for(Glossary glossary : GlobalVariables.localThesaurus){
                if(glossary.getWord().equals(content)){
                    checkDuplicated = true;
                    System.out.println("Duplicated glossary in local thesaurus.");
                    String newPlaceholder = placeholder.replaceAll("\\d+", "");
                    // update only when the previous label is "u"
                    if(glossary.getLabel().equals("u")){
                        glossary.setLabel(newPlaceholder);
                    }
                    break; //  to the outside for loop
                }
            }
            if(!checkDuplicated){
                // insert a new Glossary instance
                Glossary glossary = new Glossary(content, "Comes from cq inputted", placeholder.replaceAll("\\d+", ""));
                GlobalVariables.localThesaurus.add(glossary);
            }
        }
    }
}
