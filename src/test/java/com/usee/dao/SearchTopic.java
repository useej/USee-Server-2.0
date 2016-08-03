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
public class SearchTopic {
	@Autowired
	private DanmuDaoImp danmudao;
	@Autowired
	private TopicDaoImpl topicdao;

	@Test
	public void gettopics() {
	
		String keyword="哈哈";
		List list1 = new ArrayList();
		List<String> userTopics = new ArrayList();
		list1 = topicdao.searchTopic(keyword);
		
		 JSONArray array = JSONArray.fromObject(list1);
		 JSONObject object = new JSONObject();
			object.put("topic", array.toString());
			
			
			
	}
}