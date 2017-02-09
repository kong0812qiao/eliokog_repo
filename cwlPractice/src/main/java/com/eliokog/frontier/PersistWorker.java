package com.eliokog.frontier;

import com.eliokog.persister.Persister;

/**
 * Created by eliokog on 2017/2/7.
 */
public class PersistWorker {

    private PersiterQueue queue;

    private Persister persister;

    public PersistWorker withPersister(Persister persister) {
        this.persister = persister;
        return this;
    }

    private PersistWorker() {
    }

    public static PersistWorker build() {
        return new PersistWorker();
    }

    public PersistWorker withQueue(PersiterQueue queue) {
        this.queue = queue;
        return this;
    }

    public void start() {
        new Thread(
                () -> {
                    while (true) {
                        try {
                            persister.persist(queue.deQueue());

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

    }
}
