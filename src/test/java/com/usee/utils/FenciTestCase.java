package com.usee.utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jerry on 2016-08-09.
 */
public class FenciTestCase {
    public static void main(String[] args) throws IOException {
        FenciUtil fenciUtil = new FenciUtil();
        String text="原先使用的是内存目录对象 RAMDirectory 对象，Lucene 同时还提供了磁盘目录对象 SimpleFSDirectory 对象，至于索引写入器 IndexWriter 还是和以前一样";
        ArrayList<String> list = fenciUtil.fenci(text);
        for (String s:list){
            System.out.print(s);
            System.out.print(" | ");
        }
    }
}
