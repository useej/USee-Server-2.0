package com.usee.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.model.Danmu;
import com.usee.model.Topic;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@RunWith(SpringJUnit4ClassRunner.class)    
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})   
@Transactional
public class Gettopics {
	@Autowired
	private DanmuDaoImp danmudao;
	@Autowired
	private TopicDaoImpl topicdao;

	@Test
	public void gettopics() {
	
		String userID="3";
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List<String> userTopics = new ArrayList();
		list1 = topicdao.getUserTopicsID(userID);
		list2 = danmudao.getDanmubyUserId(userID);
		/*
		检查重复
		 */	
		 for (int i = 0; i < list2.size() - 1; i++)
         {
             for (int j = i + 1; j < list2.size(); j++)
             {
                 if (list2.get(i).equals(list2.get(j)))
                 {
                     list2.remove(j);
                     j--;
                 }
             }
         }
		list1.addAll(list2);
		
		for(int i=0; i<list1.size();i++){   
		       String a = (String) list1.get(i); 
		       userTopics.addAll(topicdao.getUserTopics(a));
		}
		 JSONArray array = JSONArray.fromObject(userTopics);
		 JSONObject object = new JSONObject();
			object.put("topic", array.toString());
			
			
			
	}
}