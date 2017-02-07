package com.eliokog.fetcher;


import com.eliokog.url.WebURL;
import com.eliokog.util.CONSTANTs;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static com.eliokog.fetcher.HttpConnectionPoolMgr.connectionManager;
import static javafx.scene.input.KeyCode.F;

/**
 * Created by eliokog on 2017/1/12.
 */
public class HttpClientFetcher {

    protected HttpClient httpClient;

    HttpConnectionPoolMgr connectionManager;

    public HttpClientFetcher() {
//        httpClient = HttpClientBuilder.create().build();
        connectionManager = HttpConnectionPoolMgr.getConnectionManager();

    }

    public FetcherResult fetch(WebURL url) {
        FetcherResult result = new FetcherResult();
        result.setUrl(url);
        //initialize the httpClient
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager.connectionManager);
        httpClient = httpClientBuilder.build();

        RequestBuilder requestBuilder = requestBuilder(url);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(url.getTimeout())
                .setSocketTimeout(url.getTimeout())
                .setConnectTimeout(url.getTimeout())
                .setCookieSpec(CookieSpecs.BEST_MATCH);
        requestBuilder.setConfig(requestConfigBuilder.build()).setUri(url.getURL()).build();
        HttpUriRequest request = null;
        try {
            request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            result.setStatusCode(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() < 300 && response.getStatusLine().getStatusCode() > 199) {
                String content = IOUtils.toString(response.getEntity().getContent());
//                System.out.println(content);
                result.setContent(content);
            }
        } catch (IOException e) {
            //TODO add retry here.
            e.printStackTrace();
        }finally {
            if(request!=null){
                HttpRequestBase req = (HttpRequestBase)request;
                req.releaseConnection();
            }
        }
        return result;
    }

    public RequestBuilder requestBuilder(WebURL url) {
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
