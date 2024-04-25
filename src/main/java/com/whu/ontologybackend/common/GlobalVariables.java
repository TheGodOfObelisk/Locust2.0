package com.whu.ontologybackend.common;

import com.whu.ontologybackend.common.structured.Glossary;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GlobalVariables {

    public static List<OntMultiwayTree> ontMultiwayForest = new ArrayList<>();

    public static Set<Glossary> localThesaurus = new HashSet<>();

    // key relationships between classes from CQs (ensure that the resulting ontology can answer a part of CQs)
    // four types of terms are stored in localThesaurus

    // actually these set should be stored in a file or db table
    public static Set<String> subClassOfRelationSet = new HashSet<>(){{
        add("is subclass of");
        add("is parent of");
        add("belong to");
        add("contain");
    }};

    public static Set<String> instanceOfRelationSet = new HashSet<>(){{
        add("is");
        add("am");
        add("are");
    }};
}
