package com.whu.ontologybackend.service.impl;

import com.whu.ontologybackend.service.InputAnalyzeService;
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
        try{
            InputStream inputStream = new FileInputStream(ontologyFile);
            OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
            m.read(inputStream, "utf-8"); // second parameter "base"
            return "open ontology: " + m.getBaseModel().toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "In the serviceImpl class.";
    }
}
