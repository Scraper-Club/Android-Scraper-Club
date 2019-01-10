package com.scraperclub.android.api;

import com.scraperclub.android.api.errors.ServerErrorException;
import com.scraperclub.android.api.errors.UnauthorizedException;
import com.scraperclub.android.api.http.HttpResponse;

import io.reactivex.functions.Consumer;

public class HttpResponseParser implements Consumer<HttpResponse> {
    @Override
    public void accept(HttpResponse httpResponse) throws Exception {
        if(httpResponse.getCode()==401) throw new UnauthorizedException(httpResponse.getContent());
        if(httpResponse.getCode()==500) throw new ServerErrorException("Internal server error");
    }
}
