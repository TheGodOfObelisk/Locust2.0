package com.whu.ontologybackend.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface InputAnalyzeService {
    // no SQL operation
    String analyzeExistingOntologies(File ontologyFile);

    // with SQL operations
    // require corresponding Classes have been defined
}
