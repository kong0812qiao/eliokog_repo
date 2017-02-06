package com.eliokog.parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by eliokog on 2017/1/16.
 */
public interface Processor {

    public Elements process(Document doc);

}
