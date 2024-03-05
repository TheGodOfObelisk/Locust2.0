package com.whu.ontologybackend.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public interface InputAnalyzeService {
    // no SQL operation
    String analyzeExistingOntologies(File ontologyFile);

    String analyzeExistingExcel(File excelFile) throws FileNotFoundException;
    // with SQL operations
    // require corresponding Classes have been defined
    // consider vector databases

    String analyzeInputGlossary(File glossaryFile);

    String analyzeInputCQs();
}
