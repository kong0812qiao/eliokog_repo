package com.eliokog.frontier;

import com.eliokog.url.WebURL;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 * Created by eliokog on 2017/2/7.
 */
public class PersiterQueue{
    private BlockingQueue<String> queue;

    public PersiterQueue(){
        queue =  new ArrayBlockingQueue<String>(10000);
    }

       public void enQueue(String  value) throws InterruptedException {
        queue.offer(value, 10000, TimeUnit.MICROSECONDS);
    }

    public String deQueue() throws InterruptedException {
        return queue.take();
    }


    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<String> queue) {
        this.queue = queue;
    }
}
