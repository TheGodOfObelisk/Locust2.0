package com.whu.ontologybackend.common.structured;


import java.io.Serializable;
import java.util.Objects;

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

    public Glossary(){
        this.word = "";
        this.description = "";
        this.label = "";
    }

    public Glossary(String word, String description, String label) {
        this.word = word;
        this.description = description;
        this.label = label;
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

    @Override
    public String toString(){
        return "Candidate Term\nWord: " + getWord() + ",\nDescription: " + getDescription() + "\nLabel: " + getLabel();
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        Glossary other = (Glossary) obj;

        return word.equals(other.getWord()) && description.equals(other.getDescription()) && label.equals(other.getLabel());
    }

    @Override
    public int hashCode(){
        return Objects.hash(word, description, label);
    }
}
