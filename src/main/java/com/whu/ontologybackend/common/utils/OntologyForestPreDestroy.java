package com.whu.ontologybackend.common.utils;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class OntologyForestPreDestroy {

    @PreDestroy
    public void exportOntologyForestBeforeExit(){
        OntologyOperationMethods.ontologyForestSerialization();
        OntologyOperationMethods.localThesaurusSerialization();
    }
}
