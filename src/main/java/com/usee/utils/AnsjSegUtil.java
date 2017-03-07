package com.usee.utils;

import java.util.List;

import org.ansj.app.keyword.Keyword;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Jerry on 2016-08-12.
 */
public class AnsjSegUtil {
    public String getPhraseSpilted(String phrase, int num){
        JSONArray array = new JSONArray();
        JSONObject resultJson = new JSONObject();

//        KeyWordComputer kwc = new KeyWordComputer(num);
//        List<Keyword> result = kwc.computeArticleTfidf(phrase);

        NewKeyWordComputer nkwc = new NewKeyWordComputer(num);
        List<Keyword> result = nkwc.computeArticleTfidf(phrase);

        String temp = result.toString().substring(1, result.toString().length()-1);

        String[] ss = temp.split("/|,");
        for(int i =0; i < ss.length; i=i+2){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("word", ss[i].trim());
            jsonObject.put("weight", ss[i+1]);
            array.add(jsonObject);
        }
        resultJson.put("hotwords", array);
        return resultJson.toString();
    }
}
