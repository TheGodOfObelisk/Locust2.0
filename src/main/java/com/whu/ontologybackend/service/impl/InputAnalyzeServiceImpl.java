package com.whu.ontologybackend.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.whu.ontologybackend.common.utils.DatabaseOperationMethods;
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

    @Override
    public String analyzeExistingExcel(File excelFile) throws FileNotFoundException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
//        FileReader fileReader = new FileReader(excelFile);
        // follow steps in essay 348
        try(CSVParser csvParser = new CSVParser(new FileReader(excelFile), csvFormat)){
            for(CSVRecord csvRecord : csvParser){
                System.out.println("--------csvRecord--------");
                System.out.println(csvRecord.stream().toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "In the serviceImpl class. Analyzing excels.";
    }

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
}
