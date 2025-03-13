package com.whu.ontologybackend.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@Service
public interface InputAnalyzeService {
    // no SQL operation
    String analyzeExistingOntologies(File ontologyFile);

    String analyzeExistingExcel(File excelFile, String moduleSource) throws FileNotFoundException;
    // with SQL operations
    // require corresponding Classes have been defined
    // consider vector databases

    String analyzeInputGlossary(File glossaryFile);

    String analyzeArticles(Map<String, Object> articles);

    String analyzeInputCQs(String CQContent, int CQType);

    String analyzeInputXML(File xmlFile, String moduleSource) throws IOException, InterruptedException;

    String analyzeInputXSD(File xsdFile, String moduleSource);

    String analyzeInputJSON(File jsonFile, String moduleSource);

    String materializeCQs(String filePrefix);

    String enrichOntologyForest();

    // extended functions
    String analyzeDatabaseSchema(String dbUrl, String username, String password);

    String analyzeInputYAML(File yamlFile);

    String analyzeInputRDF(File rdfFile);  

    String analyzeInputPDF(File pdfFile);

    String analyzeWebContent(String url);

    String analyzeWikiContent(String wikiUrl);

    String analyzeCVEData();

    String analyzeNVDData();

    String analyzeCWEData();

    String analyzeSwaggerDoc(String swaggerUrl);

    String analyzeGraphQLSchema(File graphQLFile);
    
}
