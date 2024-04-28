package com.whu.ontologybackend.service.impl;

import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import com.whu.ontologybackend.service.ExportService;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.StringWriter;

@Service
public class ExportServiceImpl implements ExportService {



    @Override
    public String exportResultingOntologies(){
        String ontologyOutputPath = "D:\\cybersecurityOntologies\\";

        for(OntMultiwayTree ontMultiwayTree : GlobalVariables.ontMultiwayForest){
            OntModel model = ontMultiwayTree.exportOntologyRDF();
            try{
                StringWriter stringWriter = new StringWriter();
                RDFDataMgr.write(stringWriter, model, RDFFormat.RDFXML);
                StringBuffer buffer = stringWriter.getBuffer();
                FileWriter fileWriter = new FileWriter(ontologyOutputPath + ontMultiwayTree.getModuleName() + ".owl");
                fileWriter.write(buffer.toString());
                System.out.println("Writing ontology whose module is " + ontMultiwayTree.getModuleName() + " to OWL file....");
                fileWriter.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return "export resulting ontologies.";
    }
}
