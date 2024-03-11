package com.whu.ontologybackend.service;

import org.springframework.stereotype.Service;

@Service
public interface HelloWorldService {
    String testDBConnect();

    String DBInit(String path);
}
