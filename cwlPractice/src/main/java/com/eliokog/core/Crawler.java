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

/**
 * Created by eliokog on 2017/2/7.
 */
public class Crawler {

    private boolean isTerminated = false;

    private WebURL url;

    private WorkQueue workQueue;

    private PersiterQueue persiterQueue;

    private Parser parser;


    private Crawler(){
        workQueue =  new WorkQueue();
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

    public Crawler withPersistQueue(PersiterQueue persiterQueue){
        this.persiterQueue =  persiterQueue;
        return this;
    }
    public void start() {
        HttpClientFetcher httpClientFetcher = new HttpClientFetcher();
        FetcherResult result = httpClientFetcher.fetch(url);
        parser.parse(result);
        enQueue(result);
        while (!Thread.interrupted() && !isTerminated) {
            try {
                //TODO the thread was waiting on lock here for http client, need check further.
                result = httpClientFetcher.fetch(workQueue.deQueue());
                parser.parse(result);
                enQueue(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }
    }

    public void enQueue(FetcherResult result){
        result.getParsedList().forEach((crawledURL) -> {
            try {
                this.workQueue.enQueue(crawledURL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        result.getFieldMap().forEach((k, v) -> {
            try {
                this.persiterQueue.enQueue(k + " :" + v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    public static Crawler build() {
        return new Crawler();
    }
}
