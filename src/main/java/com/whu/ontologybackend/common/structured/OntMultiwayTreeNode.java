package com.whu.ontologybackend.common.structured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OntMultiwayTreeNode implements Serializable {
    private OntTreeNode data;

    private List<OntMultiwayTreeNode> childList;

    public OntMultiwayTreeNode(OntTreeNode data) {
        this.data = data;
        this.childList = new ArrayList<OntMultiwayTreeNode>();
    }

    public OntMultiwayTreeNode(OntTreeNode data, List<OntMultiwayTreeNode> childList) {
        this.data = data;
        this.childList = childList;
    }

    public OntTreeNode getData() {
        return data;
    }

    public void setData(OntTreeNode data) {
        this.data = data;
    }

    public List<OntMultiwayTreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<OntMultiwayTreeNode> childList) {
        this.childList = childList;
    }
}
