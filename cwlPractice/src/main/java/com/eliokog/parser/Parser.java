package com.eliokog.parser;

import com.eliokog.fetcher.FetcherResult;

import java.util.LinkedHashSet;

/**
 * Created by eliokog on 2017/1/12.
 */
public interface Parser {

    public LinkedHashSet<String> parse(FetcherResult result);
}
