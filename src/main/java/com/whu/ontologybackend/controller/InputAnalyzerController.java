package com.whu.ontologybackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("inputAnalyzer")
public class InputAnalyzerController {
    @RequestMapping("postTest")
    public String postTest(@RequestParam("name") String name, @RequestParam("age") Integer age){
        return "name: " + name + " age: " + age + "... OK";
    }

    @RequestMapping("postArticles")
    public String integrateArticles(@RequestParam Map<String, Object> params){
        return "articles: " + params.get("article") + ", value: " + params.get("value") + "...OK";
    }

    @RequestMapping("postExcels")
    public String integrateExcels(){
        return "excels";
    }

    @RequestMapping("postXML")
    public String integrateXML(){
        return "XML";
    }

    @RequestMapping("postXSD")
    public String integrateXSD(){
        return "XSD";
    }

    @RequestMapping("postCQ")
    public String integrateCQs(){
        return "CQs";
    }

    @RequestMapping("postJSON")
    public String integrateJSON(){
        return "json";
    }
}
