package com.whu.ontologybackend.service.impl;


import com.whu.ontologybackend.common.utils.Methods;
import com.whu.ontologybackend.service.InputAnalyzeService;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.util.iterator.ExtendedIterator;
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
            Methods.outputOntInfo(m);

//            Methods.ontMetadataProperties(m);
            return "open ontology: " + m.getBaseModel().toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "In the serviceImpl class.";
    }


}
