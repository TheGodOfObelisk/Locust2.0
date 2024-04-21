package com.whu.ontologybackend.service.impl;

import com.whu.ontologybackend.common.utils.DatabaseOperationMethods;
import com.whu.ontologybackend.service.HelloWorldService;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String testDBConnect() {
        DatabaseOperationMethods.DBQuery();
        return "test db connect";
    }

    @Override
    public String DBInit(String path) {
        // import CQ templates and their corresponding SPARQL queries into DB
        DatabaseOperationMethods.DBInit(path);
        return "db init";
    }
}
