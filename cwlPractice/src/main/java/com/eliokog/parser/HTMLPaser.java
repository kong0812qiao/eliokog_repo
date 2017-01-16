package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by eliokog on 2017/1/12.
 */
public class HTMLPaser  implements Parser{

    public void parse(FetcherResult result) {
      Document doc = Jsoup.parse(result.getContent());
        System.out.println(doc);
    }

}
