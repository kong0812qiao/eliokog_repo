package com.eliokog.frontier;

import com.eliokog.persister.Persister;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eliokog on 2017/2/7.
 */
public class PersistWorker {

    private static final Logger logger = LoggerFactory.getLogger(PersistWorker.class);
    private PersiterQueue queue;

    private Persister persister;

    public PersistWorker withPersister(Persister persister) {
        this.persister = persister;
        return this;
    }

    private PersistWorker() {
    }

    public static <T>PersistWorker build() {
        return new PersistWorker();
    }

    public <T>PersistWorker withQueue(PersiterQueue queue) {
        this.queue = queue;
        return this;
    }

    public void start() {
        new Thread(
                () -> {
                    while (true) {
                        try {
                            String s = queue.deQueue();
                            logger.info("start persist String: {}", s);
                            persister.persist(s);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "PersistWorker"
        ).start();

    }
}
