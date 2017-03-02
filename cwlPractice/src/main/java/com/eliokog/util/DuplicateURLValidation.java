package com.eliokog.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by win on 2017/3/2.
 */
public class DuplicateURLValidation {

    private static ConcurrentHashMap.KeySetView set = new ConcurrentHashMap(2 >> 12).newKeySet();

    //TODO optimize and see how to manage the hashing or URL
    public static boolean isDuplicatedURL(String url) {
        return set.add(url);
    }


}
