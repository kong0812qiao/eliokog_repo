package com.eliokog.fetcher;


import com.eliokog.core.Crawler;
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

    private Crawler crawler ;

    public HttpClientFetcher() {
//        httpClient = HttpClientBuilder.create().build();

    }
    public HttpClientFetcher(Crawler crawler){
        this.crawler = crawler;
    }

    public FetcherResult fetch(WebURL url) {
        FetcherResult result = new FetcherResult();
        result.setUrl(url);
        //initialize the httpClient
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
            logger.info("sending http request: {}", url.getURL() );
            HttpResponse response = httpClient.execute(request);
            result.setStatusCode(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() < 300 && response.getStatusLine().getStatusCode() > 199){
                String content = IOUtils.toString(response.getEntity().getContent());
                if(content.contains("重试")){
                    logger.error("blocked by hosts webside, start restrying.. link: {}", url.getURL());
                    if(request!=null){
                        //ugle code due to the declare of the RequestBuilder.build(). must do this to release the connection
                        HttpRequestBase req = (HttpRequestBase)request;
                        req.releaseConnection();
                    }
                    this.retryFetch(url);
                }
                result.setContent(content);
                Thread.currentThread().sleep(10*1000);
                logger.debug("   result.setContent(content); : {}", content );
            }
//            Thread.currentThread().sleep(1000);
        } catch (IOException e) {
            //TODO add retry here.
            logger.error("IO exception here {}", e.getMessage());
            e.printStackTrace();
            try {
                httpClient.execute(request);
            } catch (IOException e1) {
                logger.error("retry IO exception here {}", e.getMessage());
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(request!=null){
                //ugle code due to the declare of the RequestBuilder.build(). must do this to release the connection
                HttpRequestBase req = (HttpRequestBase)request;
                req.releaseConnection();
            }
        }
        return result;
    }

    private void retryFetch(WebURL url){
        try {
            Thread.currentThread().sleep(60*1000);
            FetcherResult result = this.fetch(url);
            this.crawler.enQueue(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
