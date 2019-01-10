package com.scraperclub.android.api.http;

public class TextHtmlHttpRequest extends HttpRequest {
    public TextHtmlHttpRequest() {
        super();
        super.addHeader("Content-Type","text/html");
    }
}
