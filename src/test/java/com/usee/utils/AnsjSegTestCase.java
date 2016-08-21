package com.usee.utils;

import net.minidev.json.JSONArray;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

import static org.ansj.splitWord.analysis.DicAnalysis.parse;

/**
 * Created by Jerry on 2016-08-12.
 */
public class AnsjSegTestCase {
    public static void main(String args[]){
        String str = "青春·砼行河海七步123456789吐槽王思聪奥运会轶事八零年代劲歌金曲龙门花甲！不是飞甲哟一个新的话题！qq登录发送话题！USee团队么么哒我们要空调！！测试话题。陈祥哈哈Usee 我的！新话题计信院毕业生晚会考研党我的新话题测试闪退";
        System.out.println("BaseAnalysis:" + BaseAnalysis.parse(str));
        System.out.println("ToAnalysis:" + ToAnalysis.parse(str));
        System.out.println("DicAnalysis:" + parse(str));
        System.out.println("IndexAnalysis:" + IndexAnalysis.parse(str));
        System.out.println("NlpAnalysis:" + NlpAnalysis.parse(str));

//        KeyWordComputer kwc = new KeyWordComputer(10);
//        String content = "青春·砼行河海七步123456789吐槽王思聪和奥运会轶事八零年代劲歌金曲龙门花甲！不是飞甲哟一个新的话题！qq登录发送话题！USee团队么么哒我们要空调！！测试话题。陈祥哈哈Usee 我的！新话题计信院毕业生晚会考研党我的新话题测试闪退";
//        System.out.println(content);
//        List<Keyword> result = kwc.computeArticleTfidf(content);
//        String s = result.toString().substring(1, result.toString().length()-1);
//        System.out.println(s);
//        String[] ss = s.split("/|,");
//        for(String str:ss){
//            System.out.println(str.trim());
//        }
//
//        AnsjSegUtil asu = new AnsjSegUtil();
//        System.out.println(asu.getPhraseSpilted(content, 10));

    }

}
