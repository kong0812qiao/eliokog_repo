package com.eliokog.executors;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eliokog on 2017/2/15.
 */
public class CrawleThreadFactory  implements ThreadFactory{

    private final AtomicInteger poolNumber = new AtomicInteger(0);
    private final String prefix;

    public CrawleThreadFactory(String name){
        this.prefix = name;
    }
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,prefix+"-"+poolNumber.getAndIncrement());
    }
}
