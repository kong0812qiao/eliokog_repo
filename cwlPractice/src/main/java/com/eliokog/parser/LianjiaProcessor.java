package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;
import com.eliokog.util.URLUtils;
import com.eliokog.util.Validator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created by eliokog on 2017/2/8.
 */
public class LianjiaProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(LianjiaProcessor.class);

    @Override
    public FetcherResult process(FetcherResult result) {
        Document doc = Jsoup.parse(result.getContent());
        Elements liSet = doc.select("div.info-panel");

        LinkedHashSet<WebURL> parsedLinkSet = new LinkedHashSet<>();
        LinkedHashMap<String, String> parsedValMap = new LinkedHashMap<>();
        //TODO add duplicate removal logic here
        //TODO change the resultset handling
        //TODO add url completion here
        //get one apartment DIV
        StringBuilder sb = new StringBuilder();

        for(Element e : liSet){
            Elements nameLocationSet = e.select("a[href]");
            Elements info = e.select("div.introduce");//<div class="introduce">  满五 距离4号线浦东大道站343米
            Elements price = e.select("div-cun");
            sb.append("\r\n");
            nameLocationSet.forEach(s ->{
                sb.append(s).append("\r\n");
            });
            sb.append("价格").append("\r\n");
            price.forEach(s ->{
                sb.append(s).append("\r\n");
            });
            info.forEach(s ->{
                sb.append(s).append("\r\n");
            });
        }
        result.setParsedList(new LinkedList<WebURL>(parsedLinkSet));

        parsedValMap.put("房源信息: ", sb.toString());
        result.setFieldMap(parsedValMap);
        return result;
    }
}
