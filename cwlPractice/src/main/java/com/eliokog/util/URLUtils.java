package com.eliokog.util;

import com.eliokog.fetcher.Fetcher;
import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;
import org.apache.commons.lang3.StringUtils;

import static com.sun.org.apache.xml.internal.serialize.LineSeparator.Web;

/**
 * Created by eliokog on 2017/2/8.
 */
public class URLUtils {

    public static String getFullURL(String url, WebURL weburl){
        if(!(url.startsWith("http")||url.startsWith("https"))){
            String domain = weburl.getNetURL().getProtocol()+ "://" + weburl.getNetURL().getHost();

            return domain + url;
        }
        return url;
    }
}
