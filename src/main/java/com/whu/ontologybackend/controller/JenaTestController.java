package com.whu.ontologybackend.controller;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.iri.impl.Main;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.util.FileManager;
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
        // error occurred
//        File ontologyFile = new File("D:\\cybersecurityOntologies\\CAPEC.owl");
        File ontologyFile = new File("D:\\ontologies\\killchain.owl"); //test existing ontology
        try{
            InputStream inputStream = new FileInputStream(ontologyFile);
            FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
            Model model = FileManager.get().loadModel("D:\\ontologies\\killchain.owl");
            OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            ontModel.read(inputStream, "utf-8");
//            Dataset dataset = DatasetFactory.create(ontModel);
            Dataset dataset = DatasetFactory.createTxnMem();
            FusekiServer server = FusekiServer.create().add("/dataset", dataset).build();
            server.start();
            // refer to https://hydrargillite4.rssing.com/chan-3685425/all_p13.html
            String queryString =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n " +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n " +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n " +
                    "SELECT ?subject ?object \n" +
                    "\tWHERE { ?subject rdfs:subClassOf ?object }";
            Query query = QueryFactory.create(queryString);
            QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
//            QueryExecution queryExecution = QueryExecution.create("SELECT * { ?s  ?o}", dataset);
            ResultSet rs = queryExecution.execSelect();
            ResultSetFormatter.out(rs);

            System.out.println("SPARQL query ends. Stop the fuseki server.");
            queryExecution.close();
            server.stop();
        } catch (Exception e){
            e.printStackTrace();
        }



//        server.stop();
        return "fuseki test ends.";
    }
}
