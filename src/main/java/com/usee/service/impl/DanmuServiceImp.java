package com.usee.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.model.Danmu;
import com.usee.service.DanmuService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class DanmuServiceImp implements DanmuService{
	@Resource
	private DanmuDaoImp danmudao;
	@Override
	public void sendDammu(Danmu danmu) {
		// TODO Auto-generated method stub
		Danmu d1=new Danmu ();
		Danmu d2=new Danmu ();
		Danmu d3=new Danmu ();
		Danmu d4=new Danmu ();
//		d1.setId(1);
		d1.setDevId("123");
		d1.setUserId("111");
		d1.setTopicId("1");
		d1.setStatus("true");
		d1.setMessages("第一条弹幕");
//		d2.setId(2);
		d2.setDevId("123");
		d2.setUserId("111");
		d2.setTopicId("1");
		d2.setStatus("true");
		d2.setMessages("第二条弹幕");
//		d3.setId(3);
		d3.setDevId("123");
		d3.setUserId("111");
		d3.setTopicId("1");
		d3.setStatus("true");
		d3.setMessages("第三条弹幕");
//		d4.setId(4);
		d4.setDevId("123");
		d4.setUserId("111");
		d4.setTopicId("1");
		d4.setStatus("true");
		d4.setMessages("第四条弹幕");
		for(int i=0;i<250;i++){
			Danmu dd1=d1;
			danmudao.saveDanmu(dd1);
			Danmu dd2=d2;
			danmudao.saveDanmu(dd2);
			Danmu dd3=d3;
			danmudao.saveDanmu(dd3);
			Danmu dd4=d4;
			danmudao.saveDanmu(dd4);
		}
		
	}

	@Override
	public String getDanmuDetails(String danmuId) {
		// TODO Auto-generated method stub
		List<Object[]> list = new ArrayList<Object[]>();
		list = danmudao.getDanmuDetails(danmuId);
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmu", array.toString());
		
		return object.toString();
	}

	@Override
	public String getDanmubyTopic(String topicId, String pageNum,
			String pageSize) {
		// TODO Auto-generated method stub
		List<Danmu> list = new ArrayList<Danmu>();
		if(pageNum == null && pageSize == null){
			list=danmudao.getDanmuList(topicId);
			System.out.println(list.get(0).getMessages());
		}
		else {
			int _pageNum = Integer.parseInt(pageNum);
			int _pageSize = Integer.parseInt(pageSize);
			list = danmudao.getDanmuList(topicId,_pageNum, _pageSize);
			System.out.println(list.get(0).getMessages());
		}
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmu", array.toString());
		
		return object.toString();
	}

	@Override
	public void postMessage() {
		// TODO Auto-generated method stub
		
	}
}
