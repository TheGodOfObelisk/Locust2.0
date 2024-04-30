package com.whu.ontologybackend.service;

import org.springframework.stereotype.Service;

@Service
public interface ExportService {
    String exportResultingOntologies();

    String exportMaterializedCQs();
}
