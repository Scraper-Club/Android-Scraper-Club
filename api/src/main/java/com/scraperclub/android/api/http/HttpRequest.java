package com.scraperclub.android.api.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

//TODO handle HTTPS connections
public class HttpRequest {

    private URL connectionUrl;
    private String method = "POST";
    private Map<String,String> headers;
    private String requestBody;

    private boolean ready;

    public HttpRequest() {
        headers = new HashMap<>();
        ready = false;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setConnectionUrl(URL connectionUrl) {
        this.connectionUrl = connectionUrl;
        ready = true;
    }

    public void addHeader(String name, String value){
        headers.put(name,value);
    }

    public void setRequestBody(String body){
        this.requestBody = body;
    }

    public Single<HttpResponse> doRequest(){
        return Single.create(emitter -> {
            if(!ready) {
                if(!emitter.isDisposed())
                    emitter.onError(new IllegalArgumentException("Url for connection not specified"));
            }else{
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) connectionUrl.openConnection();
                    httpURLConnection.setRequestMethod(method);
                    if (headers != null) setHeaders(httpURLConnection, headers);
                    if (requestBody != null && !requestBody.isEmpty())
                        sendData(httpURLConnection, requestBody);
                    String content = readAnswer(httpURLConnection);
                    httpURLConnection.disconnect();
                    int code = httpURLConnection.getResponseCode();
                    String contentType = httpURLConnection.getContentType();
                    if(!emitter.isDisposed())
                        emitter.onSuccess(new HttpResponse(code,contentType,content));
                }catch(Throwable e){
                    if(!emitter.isDisposed())
                        emitter.onError(e);
                }
            }
        });
    }

    private void setHeaders(HttpURLConnection connection, Map<String,String> headers){
        for(Map.Entry<String,String> header:headers.entrySet()){
            connection.setRequestProperty(header.getKey(),header.getValue());
        }
    }

    private void sendData(HttpURLConnection connection, String data) throws IOException {
        connection.setDoOutput(true);
        OutputStream output = connection.getOutputStream();
        output.write(data.getBytes("UTF-8"));
        output.flush();
        output.close();
    }

    private String readAnswer(HttpURLConnection connection) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (IOException e){
            InputStream inputStream = connection.getErrorStream();
            if(inputStream == null)
                throw e;
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        return Utils.readToEnd(reader);
    }
}
