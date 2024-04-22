package com.whu.ontologybackend.common.unstructured;

public class TripleRecord {
    private String subject;
    private String relation;
    private String object;

    @Override
    public String toString() {
        return "TripleRecord{" +
                "subject='" + subject + '\'' +
                ", relation='" + relation + '\'' +
                ", object='" + object + '\'' +
                '}';
    }

    public TripleRecord(){
        subject = relation = object = "";
    }

    public TripleRecord(String s, String r, String o){
        subject = s;
        relation = r;
        object = o;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
