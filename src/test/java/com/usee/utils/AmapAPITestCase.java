package com.usee.utils;

import net.sf.json.JSONObject;

/**
 * Created by Jerry on 2016-09-02.
 */
public class AmapAPITestCase {
    public static void main(String args[]){
        AmapAPIUtil amap = new AmapAPIUtil();
        JSONObject addressJson = new JSONObject().fromObject(amap.getLocationName("118.790128,31.913611"));
        String address = addressJson.getJSONObject("regeocode").getString("formatted_address");
        System.out.println(address);
    }
}
