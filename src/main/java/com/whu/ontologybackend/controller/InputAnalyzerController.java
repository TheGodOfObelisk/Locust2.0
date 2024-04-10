package com.whu.ontologybackend.controller;

import com.whu.ontologybackend.service.InputAnalyzeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("inputAnalyzer")
public class InputAnalyzerController {
    @Autowired
    private InputAnalyzeService inputAnalyzeService;

    // replace it with relative path in the release version
    private static String resourcePrefix = "D:\\Lowcode-ontology-generator\\Lowcode-ontology-generator-backend\\backend\\src\\main\\resources\\multi-source\\";

    private static String templatePrefix = "D:\\Lowcode-ontology-generator\\Lowcode-ontology-generator-backend\\backend\\src\\main\\resources\\templates\\BigCQ_dataset\\";

    @RequestMapping("postTest")
    public String postTest(@RequestParam("name") String name, @RequestParam("age") Integer age){
        return "name: " + name + " age: " + age + "... OK";
    }

    @RequestMapping("glossary")
    public String integrateGlossary(){
        // handle two sources of glossaries, integrate them into local thesaurus
        // in the release version, the file path should be conveyed as a parameter
        // test 1
//        File glossaryFile = new File(resourcePrefix + "Thesaurus\\globalknowledge-cybersecurity.json");
        // test 2
        File glossaryFile = new File(resourcePrefix + "Thesaurus\\NICCS_3.json");
        return inputAnalyzeService.analyzeInputGlossary(glossaryFile);
    }

    @RequestMapping("postArticles")
    public String integrateArticles(@RequestParam Map<String, Object> params){
        if(!params.containsKey("article")){
            return "Error: missing key of params!";
        }
//        return "articles: " + params.get("article") + ", value: " + params.get("value") + "...OK";
        return inputAnalyzeService.analyzeArticles(params);
    }

    @RequestMapping("postExcels")
    public String integrateExcels() throws FileNotFoundException {
        // It's unnecessary to process CAPEC csv, because our focus is on schema
//        String filename = resourcePrefix + "2000.csv";
        String filename = resourcePrefix + "cce-win7-5.20120521.xls";
        File excelFile = new File(filename);
        String moduleSource = "CAPEC";
        // use module source to determine whether the small schema should be cast into an extra multiway tree
        return inputAnalyzeService.analyzeExistingExcel(excelFile, moduleSource);
    }

    // first convert it to XSD form using the trang tool
    // each xml corresponding to more than one xsd, then process the resulting xsd using methods in "postXSD"
    @RequestMapping("postXML")
    public String integrateXML(){
        // TODO: ignore instances first, focus on its schema
        return "XML";
    }

    // use XSD again?
    @RequestMapping("postXSD")
    public String integrateXSD(){
        File xsdFile = new File(resourcePrefix + "CAPEC\\ap_schema_latest.xsd");
        return inputAnalyzeService.analyzeInputXSD(xsdFile);
    }

    @RequestMapping("postCQ")
    public String integrateCQs(){
        // take a list of competency questions
        // each competency question is a single query sentence
        // CQ is divided into two parts: 1, from words of existing ontologies and glossaries; 2, from user-defined competency questions or requirement descriptions

        return inputAnalyzeService.analyzeInputCQs(templatePrefix);
    }

    @RequestMapping("materializeCQ")
    public String materializeCQs(){
        return inputAnalyzeService.materializeCQs(templatePrefix);
    }

    // requirements in the form of text
    @RequestMapping("postRequirementDocuments")
    public String integrateRequirements(){
        return "requirements";
    }

    @RequestMapping("postJSON")
    public String integrateJSON(){
        return "json";
    }

    @RequestMapping("postOntology")
    public String integrateOntology(){
        File ontologyFile = new File(resourcePrefix + "MALOnt.owl");
        return inputAnalyzeService.analyzeExistingOntologies(ontologyFile);
        // move specific process away from controllers
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
