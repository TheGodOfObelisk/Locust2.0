package com.whu.ontologybackend.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
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

    String analyzeInputCQs(String filePrefix);

    String analyzeInputXSD(File xsdFile);

    String materializeCQs(String filePrefix);
}
