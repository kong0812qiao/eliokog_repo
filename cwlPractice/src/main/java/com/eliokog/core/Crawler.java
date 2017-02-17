package com.eliokog.core;

import com.eliokog.executors.CrawleThreadFactory;
import com.eliokog.fetcher.FetcherResult;
import com.eliokog.fetcher.HttpClientFetcher;
import com.eliokog.frontier.PersiterQueue;
import com.eliokog.frontier.WorkQueue;
import com.eliokog.parser.Parser;
import com.eliokog.parser.Processor;
import com.eliokog.url.WebURL;
import com.eliokog.util.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by eliokog on 2017/2/7.
 * Crawler
 */
public class Crawler {
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private boolean isTerminated = false;

    private WebURL url;

    private WorkQueue workQueue;

    private PersiterQueue persiterQueue;

    private Parser parser;

    private ThreadPoolExecutor executor;

    private Crawler() {
        workQueue = new WorkQueue();
        executor = new ThreadPoolExecutor(SystemPropertyUtil.getIntProperty("com.eliokog.crawlerThreads"), SystemPropertyUtil.getIntProperty("com.eliokog.crawlerThreads") * 2, 10000, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(),new CrawleThreadFactory("CrawleTask"),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        executor.allowCoreThreadTimeOut(true);
    }

    public Crawler withURL(String url) {
        this.url = new WebURL(url);
        this.url.setTimeout(1000);
        return this;
    }

    public Crawler withParser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Crawler withProcessor(Processor processor) {
        this.parser.setProcessor(processor);
        return this;
    }

    public Crawler withPersistQueue(PersiterQueue persiterQueue) {
        this.persiterQueue = persiterQueue;
        return this;
    }

    public Crawler withWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    public boolean  isIdle(){
        return this.executor.getActiveCount()==0 && this.executor.getTaskCount()==0;
    }

    public Crawler start() {
        new Thread(() -> {
            HttpClientFetcher httpClientFetcher = new HttpClientFetcher();
            if (null != url) {
                FetcherResult result = httpClientFetcher.fetch(url);
                parser.parse(result);
            }
            while (!Thread.interrupted() && !isTerminated) {
                try {
                    WebURL url = workQueue.deQueue();
                    logger.debug("start to crawle link: {}", url.getURL());
                    executor.submit(() -> {
                        FetcherResult res = httpClientFetcher.fetch(url);
                        parser.parse(res);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

        }, "Crawler").start();
        return this;
    }

    public void enQueue(FetcherResult result) {
        result.getParsedList().forEach((crawledURL) -> {
            try {
                this.workQueue.enQueue(crawledURL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        result.getFieldMap().forEach((k, v) ->
        {
            try {
                this.persiterQueue.enQueue(v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static Crawler build() {
        return new Crawler();
    }
}
