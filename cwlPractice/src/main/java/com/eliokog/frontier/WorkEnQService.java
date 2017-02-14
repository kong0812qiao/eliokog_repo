package com.eliokog.frontier;

import com.eliokog.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eliokog on 2017/2/14.
 */
public class WorkEnQService {
    private static final Logger logger = LoggerFactory.getLogger(WorkEnQService.class);

    private WorkQueue queue;

    public WorkEnQService(){
        queue = new WorkQueue();
    }

    public WorkEnQService withQueue(WorkQueue queue){
        this.queue = queue;
        return this;
    }

    public void enQueue(WebURL url){
        try {
            queue.enQueue(url);
        } catch (InterruptedException e) {
            logger.error("Interrupted while enqueue, URL: {}", url);
            e.printStackTrace();
        }
    }

    public WebURL deQueue() throws InterruptedException {
        return queue.deQueue();
    }


    public static WorkEnQService build(){
        return new WorkEnQService();
    }

}
