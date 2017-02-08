package com.eliokog.util;

/**
 * Created by eliokog on 2017/2/8.
 */
public class SystemPropertyUtil {

    public static int getIntProperty(String key){
        return Integer.parseInt(getAndAssertProperty(key));
    }

    public static String getStringProperty(String key){
        return getAndAssertProperty(key);
    }

    private static String getAndAssertProperty(String name){
        String value = System.getProperty(name);
        if(value==null){
            throw new IllegalArgumentException("Missing system property["+name+"]");
        }
        return value;
    }
}
