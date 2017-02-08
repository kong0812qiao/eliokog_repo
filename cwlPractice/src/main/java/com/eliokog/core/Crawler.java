package com.eliokog.core;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.fetcher.HttpClientFetcher;
import com.eliokog.frontier.PersiterQueue;
import com.eliokog.frontier.WorkQueue;
import com.eliokog.parser.HTMLParser;
import com.eliokog.parser.Parser;
import com.eliokog.parser.Processor;
import com.eliokog.parser.ZhihuProcessor;
import com.eliokog.url.WebURL;
import com.eliokog.util.SystemPropertyUtil;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by eliokog on 2017/2/7.
 */
public class Crawler {
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private boolean isTerminated = false;

    private WebURL url;

    private WorkQueue workQueue;

    private PersiterQueue persiterQueue;

    private Parser parser;

    ExecutorService executor;

    private Crawler() {
        workQueue = new WorkQueue();
        executor = new ThreadPoolExecutor(SystemPropertyUtil.getIntProperty("com.eliokog.crawlerThreads"), SystemPropertyUtil.getIntProperty("com.eliokog.crawlerThreads")*2, 10000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.DiscardOldestPolicy());
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

    public void start() {
      new Thread(()-> {
          HttpClientFetcher httpClientFetcher = new HttpClientFetcher();
          FetcherResult result = httpClientFetcher.fetch(url);
          parser.parse(result);
          enQueue(result);

          while (!Thread.interrupted() && !isTerminated) {
              try {
                  WebURL url = workQueue.deQueue();
                  logger.debug("start to crawle link: {}", url.getURL());
                  executor.submit(() -> {
                      FetcherResult res = httpClientFetcher.fetch(url);
                      parser.parse(res);
                      enQueue(res);
                  });
              } catch (InterruptedException e) {
                  e.printStackTrace();
                  Thread.currentThread().interrupt();
              }
          }

      }).start();
    }

    public void enQueue(FetcherResult result) {
        result.getParsedList().forEach((crawledURL) -> {
            try {
                this.workQueue.enQueue(crawledURL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        StringBuilder sb = new StringBuilder();
        result.getFieldMap().forEach((k, v) -> sb.append(k).append(" :").append(v).append("\r\n"));
        try {
            this.persiterQueue.enQueue(sb.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO error handling here
        }
    }

    public static Crawler build() {
        return new Crawler();
    }
}
