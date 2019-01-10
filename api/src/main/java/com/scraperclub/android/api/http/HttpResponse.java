package com.scraperclub.android.api.http;

public class HttpResponse {
    private int code;
    private String contentType;
    private String content;

    public HttpResponse(int code, String contentType, String content) {
        this.code = code;
        this.contentType = contentType;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

}
