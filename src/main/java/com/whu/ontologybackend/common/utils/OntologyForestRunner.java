package com.whu.ontologybackend.common.utils;

import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class OntologyForestRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // initialize ontology forest
        // Step 1: deserialize from disk
        // TODO: implementation of deserialize from a "*.out" file
        List<OntMultiwayTree> tmpForest = OntologyOperationMethods.ontologyForestDeserialization();
        if(tmpForest != null && tmpForest.size() > 0){
            GlobalVariables.ontMultiwayForest = tmpForest;
            return;
        }
        // Step 2: deserialization fails, initialize a new one
        if(GlobalVariables.ontMultiwayForest.size() == 0){
            OntMultiwayTree newTree = new OntMultiwayTree();
            GlobalVariables.ontMultiwayForest.add(newTree);
        }
    }
}
