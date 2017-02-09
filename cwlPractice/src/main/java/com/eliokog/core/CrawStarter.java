package com.eliokog.core;

import com.eliokog.frontier.PersiterQueue;
import com.eliokog.frontier.WorkQueue;
import com.eliokog.parser.HTMLParser;
import com.eliokog.parser.LianjiaProcessor;
import com.eliokog.parser.ZhihuProcessor;
import com.eliokog.persister.ExcelPersister;
import com.eliokog.persister.FilerPersister;
import com.eliokog.frontier.PersistWorker;
import com.eliokog.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by eliokog on 2017/1/12.
 */
public class CrawStarter {
    final static Logger logger = LoggerFactory.getLogger(CrawStarter.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        CrawStarter cs = new CrawStarter();
        cs.init();
        // file IO is not bottleneck here, the IO stream can accept the data in buffer,
        // so we can barely see any data in persist queue.
        PersiterQueue persiterQueue = new PersiterQueue();
        PersistWorker.build().withQueue(persiterQueue).withPersister(new FilerPersister()).start();
/*

        Crawler.build().withURL("https://www.zhihu.com/explore")
                .withParser(new HTMLParser())
                .withProcessor(new ZhihuProcessor())
                .withPersistQueue(persiterQueue).start();*/

        WorkQueue workQueue = new WorkQueue();
        for(int i=1; i<20; i++){
            workQueue.enQueue(new WebURL("http://sh.lianjia.com/chengjiao/d"+ i));
        }
        Crawler.build()//.withURL("http://sh.lianjia.com/chengjiao/d1")
                .withParser(new HTMLParser())
                .withProcessor(new LianjiaProcessor())
                .withPersistQueue(persiterQueue).withWorkQueue(workQueue).start();

        logger.info("Crawler Started...");

    }

    public  void init(){
        Properties p = new Properties();
        try (InputStream is =new  FileInputStream((new File(getClass().getClassLoader().getResource("crawler.cfg").getFile())))){
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.forEach((k, v)->{
            logger.info("Key: {}, Value:{}", k, v);
            System.setProperty(k.toString(),v.toString());
        });
    }

}
