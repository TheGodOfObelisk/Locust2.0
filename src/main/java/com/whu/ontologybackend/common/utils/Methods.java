package com.whu.ontologybackend.common.utils;

import com.whu.ontologybackend.common.GlobalVariables;
import com.whu.ontologybackend.common.structured.OntMultiwayTree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Methods {
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
}
