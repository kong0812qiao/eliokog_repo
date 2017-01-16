package com.eliokog.url;

/**
 * WebURL entity used for Fetcher
 * normal URL looks like: <scheme>://<net_loc>/<path>;<params>?<query>#<fragment>
 *
 */
public class WebURL {


    public WebURL(){

    }

    public WebURL(String url){
        this.URL = url;
    }

    String URL;

    int timeout;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    String scheme;

    String method;

    String absolutURL;

    String relativeURL;

    public String getURL() {
        return URL;
    }

    public String getAbsolutURL() {
        return absolutURL;
    }

    public void setAbsolutURL(String absolutURL) {
        this.absolutURL = absolutURL;
    }

    public String getRelativeURL() {
        return relativeURL;
    }

    public void setRelativeURL(String relativeURL) {
        this.relativeURL = relativeURL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
