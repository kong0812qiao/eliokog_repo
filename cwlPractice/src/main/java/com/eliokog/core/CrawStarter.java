package com.eliokog.core;

import com.eliokog.frontier.PersiterQueue;
import com.eliokog.parser.HTMLParser;
import com.eliokog.parser.ZhihuProcessor;
import com.eliokog.persister.FilerPersister;
import com.eliokog.frontier.PersistWorker;

/**
 * Created by eliokog on 2017/1/12.
 */
public class CrawStarter {

    public static void main(String[] args) {
        PersiterQueue persiterQueue = new PersiterQueue();
        PersistWorker.build().withQueue(persiterQueue).withPersister(new FilerPersister()).start();

        Crawler.build().withURL("https://www.zhihu.com/explore")
                .withParser(new HTMLParser())
                .withProcessor(new ZhihuProcessor())
                .withPersistQueue(persiterQueue).start();

    }

}
