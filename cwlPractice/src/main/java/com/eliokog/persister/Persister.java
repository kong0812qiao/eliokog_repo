package com.eliokog.persister;

import org.apache.poi.ss.formula.functions.T;

/**
 * Created by eliokog on 2017/1/12.
 */
public interface Persister {

    void persist(String s);
    void destroy();
}
