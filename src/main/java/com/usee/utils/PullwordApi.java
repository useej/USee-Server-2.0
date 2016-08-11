package com.usee.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jerry on 2016-08-10.
 */
public class PullwordApi {

    /**
     *
     * @param phrase 需要分词的句子
     * @param threshold 出词的阈值：0-1 之间的小数， 1 表示只抽出 100% 有把握的词;
     * @return
     */
    public String getHotwords(String phrase, double threshold) {
        String httpUrl = "http://apis.baidu.com/apistore/pullword/words";
        String httpArg = "source=" + phrase + "&param1=" + threshold + "&param2=1";
        BufferedReader reader = null;
        httpUrl = httpUrl + "?" + httpArg;
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "8ece221b2588f4da064919978726ad96");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            ArrayList<String> list = new ArrayList<String>();
            while ((strRead = reader.readLine()) != null) {
                if(!(strRead.equals(""))){
                    String[] sourceStrArray = strRead.split(":");
                    if(!list.contains(sourceStrArray[0])){
                        JSONObject tempJson = new JSONObject();
                        tempJson.put("word", sourceStrArray[0]);
                        tempJson.put("weight", Double.valueOf(sourceStrArray[1]));
                        jsonArray.add(tempJson);
                        list.add(sourceStrArray[0]);
                    }
                }
            }
            reader.close();
            sort(jsonArray, "weight", false);
            resultJson.put("hotwords", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson.toString();
    }

    public void sort(JSONArray ja,final String field, boolean isAsc){
        Collections.sort(ja, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                Object f1 = o1.get(field);
                Object f2 = o2.get(field);
                if(f1 instanceof Number && f2 instanceof Number){
                    return ((Number)f1).intValue() - ((Number)f2).intValue();
                }else{
                    return f1.toString().compareTo(f2.toString());
                }
            }
        });
        if(!isAsc){
            Collections.reverse(ja);
        }
    }

}
