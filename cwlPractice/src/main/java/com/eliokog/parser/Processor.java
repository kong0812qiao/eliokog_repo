package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;

/**
 * Created by eliokog on 2017/1/16.
 */
public interface Processor {

    public FetcherResult process(FetcherResult result);

}
