package com.whu.ontologybackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validate")
public class ValidateController {
    @RequestMapping("/consistencyValidate")
    public String consistencyValidate(){
        return "The target ontology is consistent.";
    }

    @RequestMapping("/attributeMetric")
    public String attributeMetric(){
        return "0";
    } // an example of ontology metric
}
