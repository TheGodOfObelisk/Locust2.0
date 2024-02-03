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
    public String integrateExcels() throws FileNotFoundException {
        File excelFile = new File(resourcePrefix + "2000.csv");
        return inputAnalyzeService.analyzeExistingExcel(excelFile);
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
