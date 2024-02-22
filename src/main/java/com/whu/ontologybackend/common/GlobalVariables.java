package com.whu.ontologybackend.common;

import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GlobalVariables {

    public static List<OntMultiwayTree> ontMultiwayForest = new ArrayList<>();
}
