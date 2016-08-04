package com.usee.controller; 

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
public class MessageTest {

	@Autowired  
    private WebApplicationContext wac;
	private MockMvc mockMvc;     
	
	private String getNewMsgsNumJson = "{\"userID\":\"866328023315987\","
			+ "\"latestReadTime\":\"2016-07-27 17:49:10\"}"; 
	
	private String getNewMsgsJson = "{\"userID\":\"866328023315987select ;\","
			+ "\"latestReadTime\":\"1469612947select ;\"}"; 
	
	private String getallMsgsJson = "{\"userID\":\"866328023315987\"}"; 
	
	@Before
	public void setUp() throws Exception {
		 mockMvc = webAppContextSetup(wac).build();         
	}

	/**
	 * 得到新消息数目测试
	 */
	@Test
	public void getNewMsgsNumTest() throws Exception{
		mockMvc.perform((post("/message/getNewMsgsNum"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(getNewMsgsNumJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.newMsgsNum").exists())
		.andExpect(jsonPath("$.newMsgsNum").value(10))
		;
	}
	
	/**
	 * 得到新消息测试
	 */
	@Test
	public void getNewMsgsTest() throws Exception{
		mockMvc.perform((post("/message/getNewMsgs"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(getNewMsgsJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.newMsgs").exists())
		;
	}
	
	/**
	 * 得到所有消息测试
	 */
	@Test
	public void getallMsgsTest() throws Exception{
		mockMvc.perform((post("/message/getallMsgs"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(getallMsgsJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.allMsgs").exists())
		;
	}
	
}
