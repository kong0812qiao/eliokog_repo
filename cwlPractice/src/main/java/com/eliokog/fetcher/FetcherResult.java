package com.eliokog.fetcher;

import com.eliokog.url.WebURL;
import org.apache.http.Header;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Result entity for fetcher
 */
public class FetcherResult {

    private LinkedHashMap<String, String> fieldMap;

    private LinkedList<WebURL> parsedList;

    int statusCode;

    WebURL url;

    Header header;

    String content;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public WebURL getUrl() {
        return url;
    }

    public void setUrl(WebURL url) {
        this.url = url;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LinkedList<WebURL> getParsedList() {
        return parsedList;
    }

    public void setParsedList(LinkedList<WebURL> parsedList) {
        this.parsedList = parsedList;
    }
    public LinkedHashMap<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(LinkedHashMap<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    @Override
    public String toString() {
        return "statusCode: " +this.statusCode + " ,url" + this.url + " ,header" + this.header + " ,content" + this.content;
    }
}
