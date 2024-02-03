package com.whu.ontologybackend.service.impl;


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


}
