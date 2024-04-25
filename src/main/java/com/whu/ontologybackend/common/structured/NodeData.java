package com.whu.ontologybackend.common.structured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeData implements Serializable {


    private String concept;
    private List<Map<String, Object>> dataProperties;

    private List<Map<String, Object>> objectProperties;

    public List<Map<String, Object>> getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(List<Map<String, Object>> objectProperties) {
        this.objectProperties = objectProperties;
    }

    private Map<String, Object> axioms; // unknown

    public NodeData() {
        concept = "";
        dataProperties = new ArrayList<>();
        objectProperties = new ArrayList<>();
        axioms = new HashMap<>();
    }

    public NodeData(String concept, List<Map<String, Object>> dataProperties, Map<String, Object> axioms) {
        this.concept = concept;
        this.dataProperties = dataProperties;
        this.axioms = axioms;
    }
    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }


    public List<Map<String, Object>> getDataProperties() {
        return dataProperties;
    }

    public void setDataProperties(List<Map<String, Object>> dataProperties) {
        this.dataProperties = dataProperties;
    }


    public Map<String, Object> getAxioms() {
        return axioms;
    }

    public void setAxioms(Map<String, Object> axioms) {
        this.axioms = axioms;
    }
// maybe need adaptions
}
