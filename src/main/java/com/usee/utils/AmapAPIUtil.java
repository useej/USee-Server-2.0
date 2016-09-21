package com.usee.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jerry on 2016-09-09.
 */
public class AmapAPIUtil {
    private String key = "06a41b64cd15ed0ed127d55add89716a";    // 高德地图KEY
    private String result = "{\"status\":\"0\"}";

    /**
     * 输入经纬度坐标（经度在前，纬度在后，经纬度间以 “，” 分割，经纬度小数点后不得超过 6 位），返回包含位置信息的JSON
     * @param location
     * @return status:值为0表示请求失败，1表示成功
     */
    public String getLocationName(String location){
        String url = "http://restapi.amap.com/v3/geocode/regeo?location=" + location + "&key=" + key;
        BufferedReader reader = null;
        try {
            URL myUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            result = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }
}
