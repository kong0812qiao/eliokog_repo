package com.eliokog.core;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.fetcher.HttpClientFetcher;
import com.eliokog.parser.HTMLPaser;
import com.eliokog.parser.Parser;
import com.eliokog.url.WebURL;

/**
 * Created by eliokog on 2017/1/12.
 */
public class CrawStarter {

    public static void main(String[] args) {
//        WebURL url = new WebURL("http://flashsword20.iteye.com/");
        WebURL url = new WebURL("https://www.zhihu.com/explore");
        url.setTimeout(1000);
        HttpClientFetcher httpClientFetcher = new HttpClientFetcher();
        FetcherResult result = httpClientFetcher.fetch(url);
        Parser paser = new HTMLPaser();
//        paser.parse(result);
    }

}
