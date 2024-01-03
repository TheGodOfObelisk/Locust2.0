package com.whu.ontologybackend.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/hello")
public class HelloWorldController{
    @RequestMapping("/helloworld")
    public String helloWorld(){
        return "hello world";
    }
}