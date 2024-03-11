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
}
