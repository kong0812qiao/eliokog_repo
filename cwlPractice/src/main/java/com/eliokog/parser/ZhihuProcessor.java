package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;
import com.eliokog.util.URLUtils;
import com.eliokog.util.Validator;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static javafx.beans.binding.Bindings.select;

/**
 * Created by eliokog on 2017/2/6.
 */
public class ZhihuProcessor implements Processor {
    @Override
    public FetcherResult process(FetcherResult result) {
        Document doc = Jsoup.parse(result.getContent());
        Elements links = doc.select("a.question_link");

        LinkedHashSet<WebURL> parsedLinkSet = new LinkedHashSet<>();
        LinkedHashMap<String, String> parsedValMap = new LinkedHashMap<>();
        //TODO add duplicate removal logic here
        for(Element e : links){
            String url = URLUtils.getFullURL(e.attr("href"),result.getUrl());
            if(result.getUrl().getURL()!= url){
                parsedLinkSet.add(new WebURL(url));
            }
        }
        result.setParsedList(new LinkedList<WebURL>(parsedLinkSet));

        parsedValMap.put("Title: ", Validator.nullStringChecker(doc.select("h2.zm-item-title").first()));
        parsedValMap.put("Question: ", Validator.nullStringChecker(doc.select("title")));
        parsedValMap.put("Answer: ", Validator.nullStringChecker(doc.select("div.zm-editable-content ")));
        result.setFieldMap(parsedValMap);
        return result;
    }
}
