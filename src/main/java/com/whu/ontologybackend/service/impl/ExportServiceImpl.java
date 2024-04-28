package com.whu.ontologybackend.service.impl;

import com.whu.ontologybackend.service.ExportService;
import org.springframework.stereotype.Service;

@Service
public class ExportServiceImpl implements ExportService {
    @Override
    public String exportResultingOntologies(){
        return "export resulting ontologies.";
    }
}
