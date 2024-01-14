package com.whu.ontologybackend.common.structured;

import java.io.Serializable;

public class OntTreeNode implements Serializable {
    private NodeData nodeData; // core value
    private String nodeId;

    private String parentId;

    public NodeData getNodeData() {
        return nodeData;
    }

    public void setNodeData(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public OntTreeNode(String nodeId) {
        this.nodeId = nodeId;
    }

    public OntTreeNode(String nodeId, String parentId) {
        this.nodeId = nodeId;
        this.parentId = parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

//    public double similarityMetric(OntTreeNode ontTreeNode){
//        double metric = 0;
//
//        return metric;
//    }

//    public void mergeNode(OntTreeNode targetNode){
//        // 1. check properties and copy different properties
//        // 2. delete the target node (call delete node by node Id method, how?)
//        // 3. preserve the new node
//
//        return ;
//    }
}
