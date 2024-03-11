package com.whu.ontologybackend.service.impl;

import com.whu.ontologybackend.common.utils.OntologyOperationMethods;
import com.whu.ontologybackend.service.HelloWorldService;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String testDBConnect() {
        OntologyOperationMethods.DBQuery();
        return "test db connect";
    }

    @Override
    public String DBInit(String path) {
        // import CQ templates and their corresponding SPARQL queries into DB
        OntologyOperationMethods.DBInit(path);
        return "db init";
    }
}
