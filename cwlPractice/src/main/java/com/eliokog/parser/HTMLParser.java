package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created by eliokog on 2017/1/12.
 */
public class HTMLParser  implements Parser{

    private Processor processor;

    public FetcherResult parse(FetcherResult result) {

        return processor.process(result);
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

}
