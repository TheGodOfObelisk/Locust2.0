package com.whu.ontologybackend.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import com.whu.ontologybackend.common.utils.CommonOperationMethods;
import com.whu.ontologybackend.common.utils.DatabaseOperationMethods;
import com.whu.ontologybackend.common.utils.ExternalCallOperationMethods;
import com.whu.ontologybackend.common.utils.OntologyOperationMethods;
import com.whu.ontologybackend.service.InputAnalyzeService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.base.Sys;
import org.springframework.stereotype.Service;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class InputAnalyzeServiceImpl implements InputAnalyzeService {
    // no SQL operation
    // if SQL, use mappers
    @Override
    public String analyzeExistingOntologies(File ontologyFile){
//        System.out.println(GlobalVariables.ontMultiwayForest.size());
//        Methods.ontologyForestSerialization();
//        Methods.ontologyForestDeserialization();
        try{
            InputStream inputStream = new FileInputStream(ontologyFile);
            OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            m.read(inputStream, "utf-8"); // second parameter "base"
//            OntClass upperOntClass = m.getOntClass();
//            OntClass anonClass = m.createClass();
            OntologyOperationMethods.outputOntInfo(m);
            // the 2nd and 3rd parameters are user inputs
            OntologyOperationMethods.integrateExistingOntology(m, "Malware Ontology", "0000001");

//            Methods.ontMetadataProperties(m);
            // synchronize before an input process's ending
            // call it before every time exiting the input process methods (fill)
            OntologyOperationMethods.synchronizeTerms2LocalThesaurus();
            return "open ontology: " + m.getBaseModel().toString();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return "In the serviceImpl class.";
    }

    // By default, module source is "", means not specified to any module
    // the first line is attributes, while others are instances
    @Override
    public String analyzeExistingExcel(File excelFile, String moduleSource) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
//        FileReader fileReader = new FileReader(excelFile);
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
        boolean updateModule = false;
        for(OntMultiwayTree tmpTree : GlobalVariables.ontMultiwayForest){
            if(tmpTree.isTheSameModule(moduleSource)){
                updateModule = true;
                // TODO: update this tree
                // ...
            }
        }
        if(!updateModule){
            // TODO: take it as a new module, create a new OntMultiwayTree
            // ...
        }
        return "In the serviceImpl class. Analyzing excels.";
    }

    // TODO: Term Typing Task. In our context, classifying glossary into five types
    @Override
    public String analyzeInputGlossary(File glossaryFile){
        try(InputStream is = new FileInputStream(glossaryFile);
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        JSONReader jr = new JSONReader(br)) {
            JSONObject object = new JSONObject();
            jr.startObject();
            while(jr.hasNext()){
                String key = jr.readString();
                JSONObject value = (JSONObject) jr.readObject();
                object.put(key, value);
            }
            jr.endObject();
            OntologyOperationMethods.synchronizeInputGlossaries2localThesaurus(object);
            return object.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return "Analyzing glossary files.";
    }

    // test input, from ISWC 2023
    // One of the earliest approaches [23] used lexicosyntactic patterns to extract new lexicosemantic concepts and relations from large collections of unstructured text, enhancing WordNet [41]. WordNet is a lexical database comprising a lexical ontology of concepts (nouns, verbs, etc.) and lexico-semantic relations (synonymy, hyponymy, etc.). Hwang [24] proposed an alternative approach for constructing a dynamic ontology specific to an application domain. The method involved iteratively discovering types and taxonomy from unstructured text using a seed set of terms representing high-level domain types. In each iteration, newly discovered specialized types were incorporated, and the algorithm detected relations between linguistic features. The approach utilized a simple ontology algebra based on inheritance hierarchy and set operations. Agirre et al [2] enhanced WordNet by extracting topically related words from web documents. This unique approach added topical signatures to enrich WordNet. Kietz et al [28] introduced the On-To-Knowledge system, which utilized a generic core ontology like GermaNet [22] or WordNet as the foundational structure. It aimed to discover a domain-specific ontology from corporate intranet text resources.
    @Override
    public String analyzeArticles(Map<String, Object> articles){
        String scriptPath = "D:\\researchPro\\testpyate\\testcode.py";
        String article = (String) articles.get("article");
        // may store many articles in the map, extract them and call the script iteratively
        Map<String, Double> extractedTerms = new HashMap<>();
        try{
            extractedTerms = ExternalCallOperationMethods.callPyateScript(scriptPath, article);
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e1){
            e1.printStackTrace();
        }
        // take top-K of the extracted terms
        Double threshold4Term = 0.8;
        Set<String> keySet = extractedTerms.keySet();
        Set<String> removeSet = new HashSet<>();
        for(String key: keySet){
            Double curVal = extractedTerms.get(key);
            if(curVal < threshold4Term){
                removeSet.add(key);
            }
        }
        for(String removedKey : removeSet){
            extractedTerms.remove(removedKey);
        }
        OntologyOperationMethods.synchronizeTermsFromArticles2localThesaurus(extractedTerms);
        return "Analyzing input articles. These are the main source of the resulting ontology";
    }


    // TODO: Postpone it until the Term Typing Task has been completed
    @Override
    public String materializeCQs(String filePrefix){
        // materialize when thesaurus is stable
        // materialize CQs in BigCQ using local thesaurus
        // & CQs from other resources
        // TODO: use 4 sets to materialize SPARQL and CQ, then update db
        Set<String> cSet = new HashSet<>();
        Set<String> opSet = new HashSet<>();
        Set<String> dtSet = new HashSet<>();
        Set<String> iSet = new HashSet<>();
        OntologyOperationMethods.placeholderSetInit(cSet, opSet, dtSet, iSet);
        // before fill in placeholders, check whether the set is empty
        try(BufferedReader reader = new BufferedReader(new FileReader(filePrefix + "sparqlowl_templates_only.txt"))){
            String SPARQLTemplate;
            while((SPARQLTemplate = reader.readLine()) != null){
                List<String> placeholders = OntologyOperationMethods.extractPlaceHoldersFromSPARQLT(SPARQLTemplate);
                // materializing...
                String materializedSPARQL = SPARQLTemplate;
                List<Map<String, String>> placeSequencePermutation = new ArrayList<>();
                OntologyOperationMethods.placeSequencePermutationInit(placeSequencePermutation, cSet, opSet, dtSet, iSet, placeholders);

            }
        } catch (IOException e){
            e.printStackTrace();
        }
//        if(!OntologyOperationMethods.checkCQTandSPARQLT(filePrefix)){
//            return "db hasn't been initialized.";
//        };

        return "materializing CQs";
    }

    @Override
    public String analyzeInputCQs(String filePrefix){
        // Step 1:
        // analyze inputted CQs to help constructing the resulting ontology
        // goal: enrich the ontology forest [design an update algorithm and implement it ]
        // check whether the inputted textual requirement is a CQ

        // Step 2:
        // use the inputted text to extract useful terms for enriching the resulting ontology

        // Step 3:
        // check whether the inputted CQ can match an existing template
        // form a new template or match an existing template

        // Step 4:
        // translate the inputted CQs into SPARQL-OWL format

        return "analyzing inputted CQs";
    }

    @Override
    public String analyzeInputXSD(File xsdFile){
        try{
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = schemaFactory.newSchema(xsdFile);
            Validator validator = schema.newValidator();

            System.out.println("XSD parsed successfully.");
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xsdFile);
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
//            int rootId = CommonOperationMethods.generateIntUUID();
//            String rootId = UUID.randomUUID().toString();
            String rootId = xsdFile.getName() + "Root"; // xsdfile + Root, indicates it is the root of the xsd tree
            // check whether the rootId existed in the element table
            if(DatabaseOperationMethods.checkXSDRootID(rootId)){
                // integrate XSD modules before exit
                OntologyOperationMethods.integrateXSDModules2OntologyForest();
                return "this XSD file has been integrated";
            }
            // insert root info into the element table
            CommonOperationMethods.parseXSDRootElement(root, rootId);
            // parse children of root
            CommonOperationMethods.parseXSDElements(root, rootId);
        } catch (Exception e){
            e.printStackTrace();
        }
        // then parse and extract the tree structure of xsd
        // since xsd files have high quality, maybe they should become a new module ontology
        OntologyOperationMethods.integrateXSDModules2OntologyForest();
        return "analyzing inputted XSD file";
    }
}
