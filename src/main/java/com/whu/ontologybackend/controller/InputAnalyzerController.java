package com.whu.ontologybackend.controller;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("inputAnalyzer")
public class InputAnalyzerController {
    private static String resourcePrefix = "D:\\Lowcode-ontology-generator\\Lowcode-ontology-generator-backend\\backend\\src\\main\\resources\\multi-source\\";

    @RequestMapping("postTest")
    public String postTest(@RequestParam("name") String name, @RequestParam("age") Integer age){
        return "name: " + name + " age: " + age + "... OK";
    }

    @RequestMapping("postArticles")
    public String integrateArticles(@RequestParam Map<String, Object> params){
        return "articles: " + params.get("article") + ", value: " + params.get("value") + "...OK";
    }

    @RequestMapping("postExcels")
    public String integrateExcels(){
        return "excels";
    }

    @RequestMapping("postXML")
    public String integrateXML(){
        return "XML";
    }

    @RequestMapping("postXSD")
    public String integrateXSD(){
        return "XSD";
    }

    @RequestMapping("postCQ")
    public String integrateCQs(){
        return "CQs";
    }

    @RequestMapping("postJSON")
    public String integrateJSON(){
        return "json";
    }

    @RequestMapping("postOntology")
    public String integrateOntology(){
        File ontologyFile = new File(resourcePrefix + "MALOnt.owl");
        // move specific process away from controllers
        try{
            InputStream inputStream = new FileInputStream(ontologyFile);
            OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
            m.read(inputStream, "utf-8"); // second parameter "base"
            return "open ontology: " + m.getBaseModel().toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "ontology";
    } // process existing ontologies

    @RequestMapping("uploadFile")
    public String handleFileUpload(@RequestParam("file")MultipartFile file){
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();
                // for example, process ontology files
                return "redirect:/success";
            } catch (Exception e){
                return "redirect:/error";
            }
        } else {
            return "redirect:/error";
        }
    }
}
