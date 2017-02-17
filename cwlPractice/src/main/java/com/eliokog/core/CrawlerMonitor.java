package com.eliokog.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eliokog on 2017/2/15.
 */
public class CrawlerMonitor {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerMonitor.class);

    public static boolean workQIdle() {
        return CrawlerContext.context().getWorkEnQService().queueSize() == 0;
    }

    public static boolean persistQIdle() {
        return CrawlerContext.context().getPersitEnQService().queueSize() == 0;

    }

    public static boolean executorIdle() {
        return CrawlerContext.context().getCrawler().isIdle();
    }

    public static boolean isIdle() {
        logger.info("Current status workQIdle " + workQIdle() + "persistQIdle:" + persistQIdle()+ "executorIdle: " + executorIdle());
        return workQIdle() && persistQIdle() && executorIdle();
    }

}
