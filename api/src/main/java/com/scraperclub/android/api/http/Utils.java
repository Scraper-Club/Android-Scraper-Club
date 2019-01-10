package com.scraperclub.android.api.http;

import java.io.BufferedReader;
import java.io.IOException;

final class Utils {

    public static String readToEnd(BufferedReader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line =reader.readLine())!=null){
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
