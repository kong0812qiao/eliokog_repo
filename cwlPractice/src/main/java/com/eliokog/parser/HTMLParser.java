package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;

/**
 * Created by eliokog on 2017/1/12.
 */
public class HTMLParser  implements Parser{

    private Processor processor;

    public LinkedHashSet<String> parse(FetcherResult result) {
      Document doc = Jsoup.parse(result.getContent());
        Elements links = processor.process(doc);
        LinkedHashSet<String> parsedLinkSet = new LinkedHashSet<>();
        for(Element e : links){
            String url = e.attr("href");
            parsedLinkSet.add(url);
        }
        System.out.println(parsedLinkSet);
        return parsedLinkSet;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

}
