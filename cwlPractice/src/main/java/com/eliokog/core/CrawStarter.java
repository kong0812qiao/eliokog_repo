package com.eliokog.core;

import com.eliokog.frontier.*;
import com.eliokog.parser.HTMLParser;
import com.eliokog.parser.LianjiaProcessor;
import com.eliokog.parser.ZhihuProcessor;
import com.eliokog.persister.CSVPersister;
import com.eliokog.persister.ExcelPersister;
import com.eliokog.persister.FilerPersister;
import com.eliokog.url.WebURL;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static org.apache.xmlbeans.impl.schema.StscState.start;

/**
 * Created by eliokog on 2017/1/12.
 */
public class CrawStarter {
    private final static Logger logger = LoggerFactory.getLogger(CrawStarter.class);

    public static void main(String[] args) throws InterruptedException, IOException, InvalidFormatException {
        CrawStarter cs = new CrawStarter();
        cs.init();

        PersiterQueue persiterQueue = new PersiterQueue();
        PersistWorker.build().withQueue(persiterQueue).withPersister(new ExcelPersister()).start();

        WorkQueue workQueue = new WorkQueue();

        CrawlerContext.context().withPersistQService(new PersitEnQService().withPersistQueue(persiterQueue));
        CrawlerContext.context().withWorkQService(new WorkEnQService().withQueue(workQueue));

/*

        Crawler.build().withURL("https://www.zhihu.com/explore")
                .withParser(new HTMLParser())
                .withProcessor(new ZhihuProcessor())
                .withPersistQueue(persiterQueue).start();*/


        for(int i=1; i<2200; i++){
            workQueue.enQueue(new WebURL("http://sh.lianjia.com/chengjiao/d"+ i));
        }
       Crawler.build().withURL("http://sh.lianjia.com/chengjiao/d1")
                .withParser(new HTMLParser())
                .withProcessor(new LianjiaProcessor())
                .withPersistQueue(persiterQueue).withWorkQueue(workQueue).start();

        logger.debug("Crawler Started...");

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
