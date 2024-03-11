package com.whu.ontologybackend.controller;

import com.whu.ontologybackend.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/hello")
public class HelloWorldController{

    private static String BigCQTemplatePath = "D:\\Lowcode-ontology-generator\\Lowcode-ontology-generator-backend\\backend\\src\\main\\resources\\templates\\BigCQ_dataset\\query_templates_to_cq_template_mappings\\";
    @Autowired
    private HelloWorldService helloWorldService;

    @RequestMapping("/helloworld")
    public String helloWorld(){
        return "hello world";
    }

    @RequestMapping("/dbconnect")
    public String dbConnect(){
        return helloWorldService.testDBConnect();
    }

    @RequestMapping("/dbInitializer")
    public String dbInit(){
        return helloWorldService.DBInit(BigCQTemplatePath);
    }
}