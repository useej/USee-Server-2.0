package com.usee.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.model.Danmu;

@RunWith(SpringJUnit4ClassRunner.class)    
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})   
//开启Rollback，测试完毕后测试数据不会存到数据库
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class SendDanmuTestCase {
	
	@Autowired
	private DanmuDaoImp danmudao;

	@Test
	public void testSave() {
		Danmu danmu = new Danmu();
		
		danmu.setDevId("testdevice");
		danmu.setUserId("3");
		danmu.setStatus("3");
		danmu.setTopicId("3");
		danmu.setLon("33.333");
		danmu.setLat("33.333");
		danmu.setPraisenum(3);
		danmu.setDownnum(3);
		danmu.setCommentnum(0);
		danmu.setHitnum(3);
		danmu.setCreate_time("2016-07-12 10:49:53");
		danmu.setAddress("testaddress");
		danmu.setDelete_time("");
		danmu.setHead(1);
		danmu.setMessages("这是一条测试弹幕");
		
		try {
			danmudao.saveDanmu(danmu);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
