package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;

import java.util.LinkedHashSet;

/**
 * Created by eliokog on 2017/1/12.
 */
public interface Parser {

    public FetcherResult parse(FetcherResult result);
    public void setProcessor(Processor processor) ;
}
