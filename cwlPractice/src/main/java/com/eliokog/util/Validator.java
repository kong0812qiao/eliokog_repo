package com.eliokog.util;

/**
 * Created by eliokog on 2017/2/7.
 */
public class Validator {

    public static String nullStringChecker(Object o){
        if(o==null){
         return "";
        }
        return o.toString();
    }
}
