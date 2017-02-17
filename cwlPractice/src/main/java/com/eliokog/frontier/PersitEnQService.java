package com.eliokog.frontier;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eliokog on 2017/2/9.
 */
public class PersitEnQService<T> {

    private static final Logger logger = LoggerFactory.getLogger(PersitEnQService.class);
    private PersiterQueue queue;

    public PersitEnQService() {
        queue = new PersiterQueue();
    }

    public static PersitEnQService build() {
        return new PersitEnQService();
    }

    public PersitEnQService withPersistQueue(PersiterQueue queue) {
        this.queue = queue;
        return this;
    }

    public void enQueue(String t) {
        try {
            queue.enQueue(t);
        } catch (InterruptedException e) {
            logger.error("Interrupeted when trying enqueue {}", t.toString());
            e.printStackTrace();
        }
    }

    public int queueSize(){

        return queue.size();
    }
}
