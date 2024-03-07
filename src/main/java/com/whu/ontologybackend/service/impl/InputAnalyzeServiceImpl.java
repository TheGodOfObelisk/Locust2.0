package com.whu.ontologybackend.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.whu.ontologybackend.common.utils.OntologyOperationMethods;
import com.whu.ontologybackend.service.InputAnalyzeService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;


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
    public String analyzeInputCQs(){
        // Step 1:
        // materialize CQs in BigCQ using local thesaurus
        // & CQs from other resources
        
        // Step 2:
        // analyze materialized CQs to help constructing the resulting ontology
        // goal: enrich the ontology forest [design an update algorithm and implement it ]

        // Step 3:
        // translate materialized CQs into SPARQL-OWL format

        return "analyzing inputted CQs";
    }
}
