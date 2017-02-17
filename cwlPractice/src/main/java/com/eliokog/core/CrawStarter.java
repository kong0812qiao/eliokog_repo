package com.eliokog.core;

import com.eliokog.frontier.*;
import com.eliokog.parser.HTMLParser;
import com.eliokog.parser.LianjiaProcessor;
import com.eliokog.persister.CSVPersister;
import com.eliokog.policy.DefaultRetryNPolicy;
import com.eliokog.url.WebURL;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by eliokog on 2017/1/12.
 * start point
 */
public class CrawStarter {
    private final static Logger logger = LoggerFactory.getLogger(CrawStarter.class);

    public static void main(String[] args) throws InterruptedException, IOException, InvalidFormatException {
        CrawStarter cs = new CrawStarter();
        cs.init();

        PersiterQueue persiterQueue = new PersiterQueue();
        PersistWorker.build().withQueue(persiterQueue).withPersister(new CSVPersister()).start();

        WorkQueue workQueue = new WorkQueue();

        CrawlerContext.context().withPersistQService(new PersitEnQService().withPersistQueue(persiterQueue));
        CrawlerContext.context().withWorkQService(new WorkEnQService().withQueue(workQueue));

/*
        Crawler.build().withURL("https://www.zhihu.com/explore")
                .withParser(new HTMLParser())
                .withProcessor(new ZhihuProcessor())
                .withPersistQueue(persiterQueue).start();*/


        for (int i = 1; i < 2200; i++) {
            WebURL url = new WebURL("http://sh.lianjia.com/chengjiao/d" + i);
            url.setPolicy(new DefaultRetryNPolicy());
            workQueue.enQueue(url);
        }

        Crawler crawler = Crawler.build().withParser(new HTMLParser())
                .withProcessor(new LianjiaProcessor())
                .withPersistQueue(persiterQueue).withWorkQueue(workQueue).start();

        CrawlerContext.context().withCrawler(crawler);
        cs.startMonitor();
        logger.debug("Crawler Started...");

    }

    public void startMonitor() {
        new Thread(() -> {
            int closeCount = 0;

            while (true) {
                if (CrawlerMonitor.isIdle()) {
                    closeCount++;
                } else {
                    closeCount = 0;
                }
                if (closeCount == 3) {
                    System.exit(1);
                }
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.debug("closeCount {}" , closeCount);
            }


        }, "Crawler-Monitor").start();

    }

    private void init() {
        Properties p = new Properties();
        try (InputStream is = new FileInputStream((new File(getClass().getClassLoader().getResource("crawler.cfg").getFile())))) {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.forEach((k, v) -> {
            logger.info("Key: {}, Value:{}", k, v);
            System.setProperty(k.toString(), v.toString());
        });
    }

}
