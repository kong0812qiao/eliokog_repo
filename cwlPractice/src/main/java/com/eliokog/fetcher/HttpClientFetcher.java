package com.eliokog.fetcher;


import com.eliokog.core.Crawler;
import com.eliokog.core.CrawlerContext;
import com.eliokog.url.WebURL;
import com.eliokog.util.CONSTANTs;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.eliokog.fetcher.HttpConnectionPoolMgr.connectionManager;

/**
 * Created by eliokog on 2017/1/12.
 * Fetcher with pool mechnism
 */
public class HttpClientFetcher {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientFetcher.class);

    private HttpClient httpClient;

    private Crawler crawler;

    public HttpClientFetcher() {

    }

    public HttpClientFetcher(Crawler crawler) {
        this.crawler = crawler;
    }

    public FetcherResult fetch(WebURL url) {
        FetcherResult result = new FetcherResult();
        result.setUrl(url);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClient = httpClientBuilder.build();

        //load the http request
        RequestBuilder requestBuilder = requestBuilder(url);
        url.setTimeout(10000);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(url.getTimeout())
                .setSocketTimeout(url.getTimeout())
                .setConnectTimeout(url.getTimeout());
        requestBuilder.setConfig(requestConfigBuilder.build()).setUri(url.getURL()).build();

        HttpUriRequest request = null;
        try {
            request = requestBuilder.build();
            logger.debug("sending http request: {}", url.getURL());
            HttpResponse response = httpClient.execute(request);
            int statuscode = response.getStatusLine().getStatusCode();
            result.setStatusCode(statuscode);
            if (statuscode < 300 && statuscode > 199) {
                String content = IOUtils.toString(response.getEntity().getContent());
                if (url.getPolicy().needRetry(content)) {
                    logger.error("blocked by hosts website, start retrying.. link: {}", url.getURL());
                    if (url.getPolicy().allowRetry()) {
                        Thread.currentThread().sleep(url.getPolicy().getSleepBeforeRetry());
                        CrawlerContext.context().getWorkEnQService().enQueue(url);
                    }
                }
                logger.debug("The content is: {},", content);
                result.setContent(content);
                Thread.currentThread().sleep(1000);
            } else {
                logger.error("Request failed with status code: {}", statuscode);
                if (url.getPolicy().allowRetry()) {
                    Thread.currentThread().sleep(url.getPolicy().getSleepBeforeRetry());
                    CrawlerContext.context().getWorkEnQService().enQueue(url);
                }
            }
        } catch (IOException e) {
            logger.error("IO exception here {}", e.getMessage());
            e.printStackTrace();
            logger.info("Retrying with URL: {}", url);
            try {
                Thread.currentThread().sleep(10000);
                CrawlerContext.context().getWorkEnQService().enQueue(url);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (request != null) {
                HttpRequestBase req = (HttpRequestBase) request;
                req.releaseConnection();
            }
        }
        return result;
    }


    private RequestBuilder requestBuilder(WebURL url) {
        if (url.getMethod() == null || url.getMethod().equalsIgnoreCase(String.valueOf(CONSTANTs.METHOD.GET))) {
            return RequestBuilder.get();
        } else if (url.getMethod().equalsIgnoreCase(String.valueOf(CONSTANTs.METHOD.PUT))) {
            return RequestBuilder.put();
        } else if (url.getMethod().equalsIgnoreCase(String.valueOf(CONSTANTs.METHOD.POST))) {
            return RequestBuilder.post();
        } else if (url.getMethod().equalsIgnoreCase(String.valueOf(CONSTANTs.METHOD.HEAD))) {
            return RequestBuilder.head();
        } else if (url.getMethod().equalsIgnoreCase(String.valueOf(CONSTANTs.METHOD.DELETE))) {
            return RequestBuilder.delete();
        } else if (url.getMethod().equalsIgnoreCase(String.valueOf(CONSTANTs.METHOD.TRACE))) {
            return RequestBuilder.trace();
        }

        throw new IllegalArgumentException("Illegal HTTP Method " + url.getMethod());

    }
}
