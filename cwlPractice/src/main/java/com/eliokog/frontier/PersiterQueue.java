package com.eliokog.frontier;

import com.eliokog.url.WebURL;
import com.eliokog.util.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 * Created by eliokog on 2017/2/7.
 */
public class PersiterQueue{

    private static final Logger logger = LoggerFactory.getLogger(PersiterQueue.class);

    private BlockingQueue<String> queue;

    public PersiterQueue(){
        queue =  new ArrayBlockingQueue<String>(SystemPropertyUtil.getIntProperty("com.eliokog.persistQueueSize"));
    }

       public void enQueue(String  value) throws InterruptedException {
           logger.info("Enqueue value {} ", value);
        queue.offer(value, 10000, TimeUnit.MICROSECONDS);
    }

    public String deQueue() throws InterruptedException {
        return queue.take();
    }

    public int size(){
        return queue.size();
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<String> queue) {
        this.queue = queue;
    }
}
