package com.whu.ontologybackend.common.structured;

public class XSDElement {
    String id;
    String name;
    String type;
    int minOccurs;
    int maxOccurs;
    String parentId;

    public XSDElement(){
        id = "";
        name = "";
        type = "";
        minOccurs = 0;
        maxOccurs = -2; // means unbound
        parentId = "";
    }

    public XSDElement(String id, String name, String type, int minOccurs, int maxOccurs, String parentId){
        this.id = id;
        this.name = name;
        this.type = type;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
