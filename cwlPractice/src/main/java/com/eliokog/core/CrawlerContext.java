package com.eliokog.core;

import com.eliokog.frontier.PersitEnQService;
import com.eliokog.frontier.WorkEnQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eliokog on 2017/2/9.
 */
public class CrawlerContext {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerContext.class);

    private static final CrawlerContext context = new CrawlerContext();

    private PersitEnQService persitEnQService;

    private WorkEnQService workEnQService;

    private Crawler crawler;

    public Crawler getCrawler() {
        return crawler;
    }

    private CrawlerContext() {
    }

    public CrawlerContext withCrawler(Crawler crawler){
        this.crawler = crawler;
        return this;
    }
    public static CrawlerContext context(){
        return context;
    }

    public PersitEnQService getPersitEnQService() {
        return persitEnQService;
    }

    public WorkEnQService getWorkEnQService() {
        return workEnQService;
    }


    public CrawlerContext withPersistQService(PersitEnQService persitEnQService){
        this.persitEnQService = persitEnQService;
        return this;
    }
    public CrawlerContext withWorkQService(WorkEnQService workEnQService){
        this.workEnQService = workEnQService;
        return this;
    }
}
