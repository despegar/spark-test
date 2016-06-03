package com.despegar.sparkjava.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Client for make HTTP requests
 * @author fwasy
 */
public class SparkClient {

    private int port;

    private HttpClient httpClient;

    public SparkClient(int port) {
        this.port = port;
        this.httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    }

    public UrlResponse doMethodSecure(String requestMethod, String path, String body) throws Exception {
        return this.doMethod(requestMethod, path, body, true, "text/html");
    }

    public UrlResponse doMethod(String requestMethod, String path, String body) throws Exception {
        return this.doMethod(requestMethod, path, body, false, "text/html");
    }

    public UrlResponse doMethodSecure(String requestMethod, String path, String body, String acceptType) throws Exception {
        return this.doMethod(requestMethod, path, body, true, acceptType);
    }

    public UrlResponse doMethod(String requestMethod, String path, String body, String acceptType) throws Exception {
        return this.doMethod(requestMethod, path, body, false, acceptType);
    }

    private UrlResponse doMethod(String requestMethod, String path, String body, boolean secureConnection, String acceptType)
        throws Exception {
        return this.doMethod(requestMethod, path, body, secureConnection, acceptType, null);
    }

    public UrlResponse doMethod(String requestMethod, String path, String body, boolean secureConnection, String acceptType,
        Map<String, String> reqHeaders) throws IOException {
        HttpUriRequest httpRequest = this.getHttpRequest(requestMethod, path, body, secureConnection, acceptType,
            reqHeaders);
        HttpResponse httpResponse = this.httpClient.execute(httpRequest);

        UrlResponse urlResponse = new UrlResponse();
        urlResponse.status = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            urlResponse.body = EntityUtils.toString(entity);
        } else {
            urlResponse.body = "";
        }
        Map<String, String> headers = new HashMap<String, String>();
        Header[] allHeaders = httpResponse.getAllHeaders();
        for (Header header : allHeaders) {
            headers.put(header.getName(), header.getValue());
        }
        urlResponse.headers = headers;
        return urlResponse;
    }

    private HttpUriRequest getHttpRequest(String requestMethod, String path, String body, boolean secureConnection,
        String acceptType, Map<String, String> reqHeaders) {
        try {
            String protocol = secureConnection ? "https" : "http";
            String uri = protocol + "://localhost:" + this.port + path;
            if (requestMethod.equals("GET")) {
                HttpGet httpGet = new HttpGet(uri);
                httpGet.setHeader("Accept", acceptType);
                this.addHeaders(reqHeaders, httpGet);
                return httpGet;
            }
            if (requestMethod.equals("POST")) {
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setHeader("Accept", acceptType);
                this.addHeaders(reqHeaders, httpPost);
                httpPost.setEntity(new StringEntity(body));
                return httpPost;
            }
            if (requestMethod.equals("PATCH")) {
                HttpPatch httpPatch = new HttpPatch(uri);
                httpPatch.setHeader("Accept", acceptType);
                this.addHeaders(reqHeaders, httpPatch);
                httpPatch.setEntity(new StringEntity(body));
                return httpPatch;
            }
            if (requestMethod.equals("DELETE")) {
                HttpDelete httpDelete = new HttpDelete(uri);
                this.addHeaders(reqHeaders, httpDelete);
                httpDelete.setHeader("Accept", acceptType);
                return httpDelete;
            }
            if (requestMethod.equals("PUT")) {
                HttpPut httpPut = new HttpPut(uri);
                httpPut.setHeader("Accept", acceptType);
                this.addHeaders(reqHeaders, httpPut);
                httpPut.setEntity(new StringEntity(body));
                return httpPut;
            }
            if (requestMethod.equals("HEAD")) {
                HttpHead httpHead = new HttpHead(uri);
                this.addHeaders(reqHeaders, httpHead);
                return httpHead;
            }
            if (requestMethod.equals("TRACE")) {
                HttpTrace httpTrace = new HttpTrace(uri);
                this.addHeaders(reqHeaders, httpTrace);
                return httpTrace;
            }
            if (requestMethod.equals("OPTIONS")) {
                HttpOptions httpOptions = new HttpOptions(uri);
                this.addHeaders(reqHeaders, httpOptions);
                return httpOptions;
            }
            throw new IllegalArgumentException("Unknown method " + requestMethod);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHeaders(Map<String, String> reqHeaders, HttpRequest req) {
        if (reqHeaders != null) {
            for (Map.Entry<String, String> header : reqHeaders.entrySet()) {
                req.addHeader(header.getKey(), header.getValue());
            }
        }
    }

    public int getPort() {
        return this.port;
    }

    public static class UrlResponse {

        public Map<String, String> headers;
        public String body;
        public int status;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

}

