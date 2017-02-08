package com.eliokog.util;

import com.eliokog.fetcher.Fetcher;
import com.eliokog.fetcher.FetcherResult;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by eliokog on 2017/2/8.
 */
public class URLUtils {

    public static String getFullURL(String url, FetcherResult result){
        if(!(url.startsWith("http")||url.startsWith("https"))){
            String domain = result.getUrl().getNetURL().getProtocol()+ "://" + result.getUrl().getNetURL().getHost();

            return domain + url;
        }
        return url;
    }
}
