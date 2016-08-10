package com.usee.controller;

import net.sf.json.JSONObject;
import org.junit.Before;  
import org.junit.Test;  
import org.junit.runner.RunWith;  
import org.springframework.beans.factory.annotation.Autowired;   
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
import org.springframework.test.context.web.WebAppConfiguration;  
import org.springframework.test.web.servlet.MockMvc;   
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;  
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; 

import org.springframework.web.context.WebApplicationContext;  
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)   
@WebAppConfiguration(value = "src/main/webapp")  
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})   
@Transactional
public class SendDanmuTestCase {

	@Autowired  
    private WebApplicationContext wac;
	private MockMvc mockMvc;     
	
	@Before
	public void setUp() throws Exception {
		 mockMvc = webAppContextSetup(wac).build();         
	}
	
	String sendDanmuJson = "{\"delete_time\":\"2020-01-01 10:10:10\","
			+ "\"devid\":\"866328023315987\","
			+ "\"isannoymous\":true,"
			+ "\"lat\":\"31.917352\","
			+ "\"lon\":\"118.786877\","
			+ "\"messages\":\"热 40度了\","
			+ "\"topicid\":\"11\","
			+ "\"userid\":\"3509482502700609635\"}";
	
	String commentDanmuJson = "{\"userid\":\"3509482502700609635\","
			+ "\"danmuid\":2000,"
			+ "\"receiver\":3,"
			+ "\"content\":\"弹幕评论测试\","
			+ "\"type\":2,"
			+ "\"isannoymous\":true,"
			+ "\"reply_commentid\":14}";
	
	
	@Test
	public void sendDanmuTest() throws Exception{
//		String temp = "{\"danmu\":[{\"address\":\"33\",\"commentnum\":0,\"create_time\":\"1\",\"delete_time\":\"1\",\"devId\":\"33\",\"downnum\":3,\"head\":1,\"hitnum\":3,\"id\":1,\"lat\":\"1\",\"lon\":\"1\",\"messages\":\"3333333 \",\"praisenum\":1,\"status\":\"1\",\"topicId\":\"1\",\"userId\":\"866328023315987\"}]}";
		mockMvc.perform((post("/senddanmu"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(sendDanmuJson.getBytes()) 
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
			.andExpect(status().isOk())
//			.andExpect(content().contentType("application/json;charset=UTF-8"))
//			.andExpect(jsonPath("$.returnInfo").value("success"))
//			.andExpect(jsonPath("$.user").exists())
//			.andExpect(jsonPath("$.user.password").value("123456s"))
//			.andExpect(jsonPath("$.user.userIcon").value("1.png"))
			;
	}
	
	@Test
	public void commentDanmuTest() throws Exception{
		mockMvc.perform((post("/commentdanmu"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(commentDanmuJson.getBytes()) 
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
			.andExpect(status().isOk())
			;
	}
}
