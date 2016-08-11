package com.usee.utils;

/**
 * Created by Jerry on 2016-08-10.
 */
public class PullwordApiTestCase {

    public static void main(String args[]){
        PullwordApi pa = new PullwordApi();
        String result = pa.getHotwords("青春·砼行吐槽王思聪八零年代劲歌金曲河海七步", 0.8);
        System.out.println(result);
    }
}
