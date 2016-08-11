package com.usee.utils;

import java.util.ArrayList;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Created by Jerry on 2016-08-09.
 */
public class FenciUtil {
    public ArrayList<String> fenci(String phrase){
        ArrayList<String> list = new ArrayList<String>();
        // 创建分词对象
        Analyzer analyzer = new IKAnalyzer(true);
        StringReader reader = new StringReader(phrase);
        // 分词
        TokenStream ts = analyzer.tokenStream("", reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        // 遍历分词数据
        try{
            while(ts.incrementToken()){
                list.add(term.toString());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            reader.close();
            return list;
        }
    }
}
