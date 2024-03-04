package com.whu.ontologybackend.common.utils;

import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.Glossary;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
//@Order(1)
public class OntologyContextRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ready to initialize!");
        initializeOntologyForest();
        initializeLocalThesaurus();
        // no step 2
        System.out.println("initialize successfully!");
    }

    private void initializeOntologyForest(){
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

    private void initializeLocalThesaurus(){
        // initialize local thesaurus
        Set<Glossary> tmpGlossary = OntologyOperationMethods.localThesaurusDeserialization();
        if(tmpGlossary != null && tmpGlossary.size() > 0){
            GlobalVariables.localThesaurus = tmpGlossary;
        }
    }
}
