package com.whu.ontologybackend.common.structured;

import java.io.Serializable;

// Thesaurus: candidate words or phrases for glossary
public class Glossary implements Serializable {
    private String word;
    private String description;

    // label can be (i) class; (ii) data property; (iii) object property; (iv) instances, (v) unknown, etc.
    // add more cases if necessary
    // In BigCQ templates:
    // (i) c1, c2, c3, ...
    // (ii) dt1, dt2, ...
    // (iii) op1, op2, ...
    // (iv) i1, i2, ...
    private String label;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
