package com.eliokog.frontier;

import com.eliokog.url.WebURL;
import com.eliokog.util.SystemPropertyUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by eliokog on 2017/1/12.
 */
public class WorkQueue {



    private BlockingQueue<WebURL> queue;

    public WorkQueue(){
        queue =  new ArrayBlockingQueue<WebURL>(SystemPropertyUtil.getIntProperty("com.eliokog.workQueueSize"));
    }

    public void enQueue(WebURL url) throws InterruptedException {
        queue.offer(url, 10000, TimeUnit.MICROSECONDS);
    }

    public WebURL deQueue() throws InterruptedException {
        return queue.take();
    }

    public BlockingQueue<WebURL> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<WebURL> queue) {
        this.queue = queue;
    }
}
