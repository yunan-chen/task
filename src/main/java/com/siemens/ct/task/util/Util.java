package com.siemens.ct.task.util;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Util {

    /**
     * parse
     * @param inputStream
     */
    public static String dealResponseResult(InputStream inputStream) {
        StringBuilder tempJson = new StringBuilder();
        try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader)) {
            tempJson.append(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempJson.toString();
    }

    /**
     * get json base on the url
     * @param urlStr
     * @exception Exception
     */
    public static String getJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5 * 1000);
        connection.setReadTimeout(5 * 1000);
        connection.connect();
        int response = connection.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            return dealResponseResult(inputStream);
        }
        return null;
    }

    /**
     * get code
     * @param codeJson
     * @param str
     */
    public static String getCode(String codeJson, String str){
        Map codeName = JSON.parseObject(codeJson);
        for (Object code : codeName.keySet()){
            String name = codeName.get(code).toString();
            if(name.equals(str) || str.contains(name)){
                return code.toString();
            }
        }
        return null;
    }
}
