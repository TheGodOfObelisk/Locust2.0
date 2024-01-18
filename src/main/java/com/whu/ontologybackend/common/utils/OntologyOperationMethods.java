package com.whu.ontologybackend.common.utils;

import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;

public class OntologyOperationMethods {
    public static void ontologyForestSerialization(){
        // serialization test
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D:\\test.out"));
            objectOutputStream.writeObject(GlobalVariables.ontMultiwayForest);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<OntMultiwayTree> ontologyForestDeserialization(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:\\test.out"));
            List<OntMultiwayTree> res = (List<OntMultiwayTree>) objectInputStream.readObject();
            return res;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void outputOntInfo(OntModel m) {
        ExtendedIterator<OntClass> ontClassExtendedIterator = m.listClasses();
//            ExtendedIterator<OntClass> ontClassExtendedIterator = m.listHierarchyRootClasses();
        // close iterators manually if it doesn't iterator until hasNext() returns false
        while(ontClassExtendedIterator.hasNext()){
            OntClass presentOntClass = ontClassExtendedIterator.next();

            System.out.println("Class name: " + presentOntClass.getLocalName());
            if(presentOntClass.getSuperClass() != null){
                System.out.println("SuperClass name: " + presentOntClass.getSuperClass().getLocalName());
            } else {
                System.out.println("This class has no super class.");
            }
            if(presentOntClass.getSubClass() != null){
                System.out.println("SubClass name: " + presentOntClass.getSubClass().getLocalName());
            } else {
                System.out.println("This class has no sub class.");
            }
            ExtendedIterator<OntProperty> ontPropertyExtendedIterator = presentOntClass.listDeclaredProperties(true);
            while(ontPropertyExtendedIterator.hasNext()){
                OntProperty presentOntProperty = ontPropertyExtendedIterator.next();
                System.out.println("Properties: " + presentOntProperty.getLocalName());
                System.out.println("Property Type: " + propertyType(presentOntProperty));
            }
        }
    }

    public static String propertyType(OntProperty ontProperty){
        String propertyType = "";
        if(ontProperty.isDatatypeProperty()){
            propertyType += "Data Property.";
        } else if (ontProperty.isObjectProperty()) {
            propertyType += "Object Property.";
        } else if (ontProperty.isFunctionalProperty()) {
            propertyType += "Functional Property.";
        } else if (ontProperty.isSymmetricProperty()){
            propertyType += "Symmetric Property.";
        } else if (ontProperty.isInverseFunctionalProperty()){
            propertyType += "Inverse Functional Property.";
        } else if (ontProperty.isTransitiveProperty()){
            propertyType += "Transitive Property.";
        } else if (ontProperty.isAnnotationProperty()) {
            propertyType += "Annotation Property.";
        }
        return propertyType;
    }

    public static void ontMetadataProperties(OntModel m){
        String base = "http://idea.rpi.edu/malont";
        Ontology ont = m.getOntology(base);

        // list the ontology imports
        for(String imp : ont.getOntModel().listImportedOntologyURIs()){
            System.out.println("Ontology " + base + " imports " + imp);
        }
    }

    public static void listOntologyResources(OntModel m){
        OntModel mBase = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, m.getBaseModel());

        for(Iterator i = mBase.listOntologies(); i.hasNext();){
            Ontology ont = (Ontology) i.next();
            // m's base model has ont as an import ...
            // processing ...
        }
    }

    // module name and module id are user inputs
    public static void integrateExistingOntology(OntModel m, String moduleName, String moduleId){
        OntMultiwayTree targetTree = new OntMultiwayTree();
        // case 1: first initialization and import an existing ontology
        // case 2: not first initialization and import an existing ontology
        // case 3: not first initialization and this existing ontology has been imported before
        // operation 1: initialize and set the only one multi-way tree's attributes
        // operation 2: add a new multi-way tree to the forest
        // operation 3: update the corresponding multi-way tree that was created before
        if(GlobalVariables.ontMultiwayForest.size() == 1 && GlobalVariables.ontMultiwayForest.get(0).getModuleName().equals("default module")){
            // initialize it.
            targetTree = GlobalVariables.ontMultiwayForest.get(0);
            targetTree.setModuleName(moduleName);
            targetTree.setModuleId(moduleId);
            targetTree.setImportedOntology(true);
        } else if(GlobalVariables.ontMultiwayForest.size() == 1 && !GlobalVariables.ontMultiwayForest.get(0).getModuleName().equals("default Module")){
            // add targetTree to the global forest
            targetTree.setModuleName(moduleName);
            targetTree.setModuleId(moduleId);
            targetTree.setImportedOntology(true);
        } else if(GlobalVariables.ontMultiwayForest.size() > 1){
            for(OntMultiwayTree tmpTree : GlobalVariables.ontMultiwayForest){
                if(tmpTree.getModuleId().equals(moduleId) && tmpTree.getModuleName().equals(moduleName)){
                    targetTree = tmpTree;
                    break;
                }
            }
            // recheck
            if(!targetTree.getModuleName().equals(moduleName) || !targetTree.getModuleId().equals(moduleId)){
                targetTree.setModuleName(moduleName);
                targetTree.setModuleId(moduleId);
                targetTree.setImportedOntology(true);
            }
        }

        // further update operations should be implemented in OntMultiwayTree's methods
        targetTree.updateByExistingOntology(m);
    }
}
