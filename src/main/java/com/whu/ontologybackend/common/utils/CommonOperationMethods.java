package com.whu.ontologybackend.common.utils;

public class CommonOperationMethods {
    public static boolean isDecimal(String str){
        return str.matches("^0*(\\d+\\.\\d*|\\.\\d+)$");
    }
}
