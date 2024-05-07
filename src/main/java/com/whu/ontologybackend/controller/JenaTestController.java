package com.whu.ontologybackend.controller;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

@RestController
@EnableAutoConfiguration
@RequestMapping("/JenaTest")
public class JenaTestController {
    @RequestMapping("/JenaMessage")
    public String JenaMessage(){
        String cybersecurityURI = "http://somewhere/cybersecurity";
        String givenName = "Tim Berners";
        String familyName = "Lee";
        String testPush = "git push test";
        String fullName = givenName + " " + familyName;

        // create an empty model
        Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        Resource something = model.createResource(cybersecurityURI).addProperty(VCARD.FN, fullName).addProperty(VCARD.N,
                model.createResource().addProperty(VCARD.Given, givenName).addProperty(VCARD.Family, familyName));

        System.out.println(model.createList());
        try{
            StringWriter stringWriter = new StringWriter();
            RDFDataMgr.write(stringWriter, model, RDFFormat.TURTLE_BLOCKS);
            return stringWriter.getBuffer().toString();
        } catch(Exception e){
            e.printStackTrace();
        }
        return "Error! Fail to generate example ontology message!";
    }

    @RequestMapping("fusekiTest")
    public String JenaFuseki(){
        File ontologyFile = new File("D:\\cybersecurityOntologies\\CAPEC.owl");
        try{
            InputStream inputStream = new FileInputStream(ontologyFile);
            OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            ontModel.read(inputStream, "utf-8");
            Dataset dataset = DatasetFactory.create(ontModel);
            FusekiServer server = FusekiServer.create().add("/dataset", dataset).build();
            server.start();
        } catch (Exception e){
            e.printStackTrace();
        }
//        Dataset dataset = DatasetFactory.createTxnMem();


//        server.stop();
        return "fuseki test ends.";
    }
}
