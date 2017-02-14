package com.eliokog.frontier;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by eliokog on 2017/2/9.
 */
public class PersitEnQService {

    private static final Logger logger = LoggerFactory.getLogger(PersitEnQService.class);
    private PersiterQueue<T> queue;
    public PersitEnQService(){
        queue = new PersiterQueue<T>();
    }
    public static PersitEnQService build(){
        return new PersitEnQService();
    }

    public PersitEnQService withPersistQueue(PersiterQueue<T> queue){
        this.queue = queue;
        return this;
    }

    public void enQueue(T t){
        try {
            queue.enQueue(t);
        } catch (InterruptedException e) {
            logger.error("Interrupeted when trying enqueue {}", t.toString());
            e.printStackTrace();
        }

    }
}
