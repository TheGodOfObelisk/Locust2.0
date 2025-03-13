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
        File glossaryFile = new File(resourcePrefix + "Thesaurus\\globalknowledge-cybersecurity.json");
        // test 2
//        File glossaryFile = new File(resourcePrefix + "Thesaurus\\NICCS_3.json");
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
    public String integrateXML() throws IOException, InterruptedException {
        // TODO: ignore instances first, focus on its schema
        String filename = resourcePrefix + "nvdcce-0.1-feed.xml";
        File xmlFile = new File(filename);
        String moduleSource = "CCE";
        // empty moduleSource means default module or unknown module
        // Actually, the parameters should be set by users
//        String moduleSource = "";
        return inputAnalyzeService.analyzeInputXML(xmlFile, moduleSource);
    }

    // use XSD again?
    @RequestMapping("postXSD")
    public String integrateXSD(){
        File xsdFile = new File(resourcePrefix + "CAPEC\\ap_schema_latest.xsd");
        String moduleSource = "CAPEC";
        return inputAnalyzeService.analyzeInputXSD(xsdFile, moduleSource);
    }

    @RequestMapping("postCQ")
    public String integrateCQs(){
        // take a list of competency questions
        // each competency question is a single query sentence
        // CQ is divided into two parts: 1, from words of existing ontologies and glossaries; 2, from user-defined competency questions or requirement descriptions
        String inputCQ = "Hey, man. What can I say? Mamba out!";
//        String inputCQ = "ACM SIGMOD is a leading international forum for data management research. Through the conference, participants will explore cutting edge ideas and results, and exchange techniques, tools and experiences."; // test
        // match: what are the main types of c2 that op1 at most 1 thing and that op2 at least 1 thing?
//        String inputCQ = "what are the main types of SB Renyuan Ni that go at most 1 thing and that go back silly B at least 1 thing?";
        // 0: informal cq; 1: formal cq
        int CQType = 1;
        return inputAnalyzeService.analyzeInputCQs(inputCQ, CQType);
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
        File jsonFile = new File(resourcePrefix + "malware-family.json");
        String moduleSource = "MITRE MAEC";
        return inputAnalyzeService.analyzeInputJSON(jsonFile, moduleSource);
    }

    @RequestMapping("postOntology")
    public String integrateOntology(){
        File ontologyFile = new File(resourcePrefix + "MALOnt.owl");
//        File ontologyFile = new File(resourcePrefix + "killchain.owl");
//        File ontologyFile = new File(resourcePrefix + "uco_1_5_rdf.owl");
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

    @RequestMapping("ontologyEnrichment")
    public String enrichOntology(){

        return inputAnalyzeService.enrichOntologyForest();
    }

    @RequestMapping("postDatabase")
    public String integrateDatabase(@RequestParam String dbUrl, @RequestParam String username, @RequestParam String password) {
        // 处理关系型数据库模式
        return inputAnalyzeService.analyzeDatabaseSchema(dbUrl, username, password);
    }

    @RequestMapping("postYAML")
    public String integrateYAML(@RequestParam("file") File file) {
        // 处理YAML格式的配置文件或数据
        return inputAnalyzeService.analyzeInputYAML(file);
    }

    @RequestMapping("postRDF")
    public String integrateRDF(@RequestParam("file") File file) {
        // 处理RDF/RDFS格式的数据
        return inputAnalyzeService.analyzeInputRDF(file);
    }

    @RequestMapping("postPDF")
    public String integratePDF(@RequestParam("file") File file) {
        // 处理PDF文档中的领域知识
        return inputAnalyzeService.analyzeInputPDF(file);
    }

    @RequestMapping("postWebPage")
    public String integrateWebPage(@RequestParam String url) {
        // 处理网页内容
        return inputAnalyzeService.analyzeWebContent(url);
    }

    @RequestMapping("postWiki")
    public String integrateWikiContent(@RequestParam String wikiUrl) {
        // 处理维基百科或其他wiki平台的结构化内容
        return inputAnalyzeService.analyzeWikiContent(wikiUrl);
    }

    @RequestMapping("postCVE")
    public String integrateCVE() {
        // 处理CVE漏洞数据
        return inputAnalyzeService.analyzeCVEData();
    }

    @RequestMapping("postNVD")
    public String integrateNVD() {
        // 处理NVD数据库信息
        return inputAnalyzeService.analyzeNVDData();
    }

    @RequestMapping("postCWE")
    public String integrateCWE() {
        // 处理CWE弱点数据
        return inputAnalyzeService.analyzeCWEData();
    }

    @RequestMapping("postSwagger")
    public String integrateSwagger(@RequestParam String swaggerUrl) {
        // 处理Swagger/OpenAPI文档
        return inputAnalyzeService.analyzeSwaggerDoc(swaggerUrl);
    }

    @RequestMapping("postGraphQL")
    public String integrateGraphQLSchema(@RequestParam("file") MultipartFile file) {
        // 处理GraphQL模式定义
        return inputAnalyzeService.analyzeGraphQLSchema(file);
    }
}
