package com.usee.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.UserDaoImpl;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.service.impl.DanmuServiceImp;


@RunWith(SpringJUnit4ClassRunner.class)    
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})   
//开启Rollback，测试完毕后测试数据不会存到数据库
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DanmuServiceTestCase {
	@Autowired
	private DanmuDaoImp danmudao;
	@Autowired
	private UserDaoImpl userDao;
	@Autowired
	private UserTopicDaoImp userTopicDao;
	@Autowired
	private DanmuServiceImp danmuServiceImp;
	
	String expectedDanmuDetails = "{\"danmudetails\":[{\"danmuid\":1,\"receiver\":null,\"delete_time\":\"1\",\"dcreatetime\":\"1\",\"userid\":\"866328023315987\",\"downnum\":1,\"type\":null,\"password\":\"123456\",\"messages\":\"今晚6.30晚会准时开始! \",\"sender\":null,\"commentnum\":1,\"replycommontid\":null,\"cellphone\":\"13951739234\",\"gender\":0,\"topicID\":\"1\",\"hitnum\":1,\"head\":1,\"lat\":\"1\",\"ccreatetime\":null,\"lon\":\"1\",\"devid\":\"0\",\"status\":\"1\",\"nickname\":\"mark\",\"praisenum\":1,\"duserIcon\":null,\"commontid\":null,\"content\":null,\"address\":\"1\"}]}";
	
	@Test
	public void getDanmuDetailsTest(){
		int danmuId = 1;
		String userId = "09B395EDB463498F9319E7954BAF7160";
		String result = danmuServiceImp.getDanmuDetails(danmuId, userId);
		Assert.assertEquals(expectedDanmuDetails, result);
	}
}
