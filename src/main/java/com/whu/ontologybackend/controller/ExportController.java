package com.whu.ontologybackend.controller;

import com.whu.ontologybackend.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExportController {
    @Autowired
    ExportService exportService;

    @RequestMapping("/targetOntology")
    String exportOntology(){
        return exportService.exportResultingOntologies();
    }

    @RequestMapping("/graph")
    String exportGraph(){
        return "graph";
    } // may use the GraphViz tool
}
