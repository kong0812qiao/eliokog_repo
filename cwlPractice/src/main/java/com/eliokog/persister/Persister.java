package com.eliokog.persister;

/**
 * Created by eliokog on 2017/1/12.
 */
public interface Persister {

    void persist(String s);
    void destroy();
}
