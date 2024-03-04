package com.whu.ontologybackend.common.utils;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class OntologyContextPreDestroy {

    @PreDestroy
    public void exportOntologyForestBeforeExit(){
        System.out.println("ready to exit!");
        OntologyOperationMethods.ontologyForestSerialization();
        OntologyOperationMethods.localThesaurusSerialization();
        System.out.println("deserialize successfully!");
    }
}
