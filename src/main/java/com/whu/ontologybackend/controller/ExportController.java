package com.whu.ontologybackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExportController {
    @RequestMapping("/targetOntology")
    String exportOntology(){
        return "final ontology";
    }

    @RequestMapping("/graph")
    String exportGraph(){
        return "graph";
    } // may use the GraphViz tool
}
