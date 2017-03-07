//package com.usee.controller; 
//
//import org.junit.Before;  
//import org.junit.Test;  
//import org.junit.runner.RunWith;  
//import org.springframework.beans.factory.annotation.Autowired;   
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;  
//import org.springframework.test.context.ContextHierarchy;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
//import org.springframework.test.context.web.WebAppConfiguration;  
//import org.springframework.test.web.servlet.MockMvc;   
//import org.springframework.transaction.annotation.Transactional;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;  
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; 
//import org.springframework.web.context.WebApplicationContext;  
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
//@RunWith(SpringJUnit4ClassRunner.class)   
//@WebAppConfiguration(value = "src/main/webapp")  
//@ContextHierarchy({
//    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
//    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
//})   
//@Transactional
//
//public class JPushTestCase {
//
//	@Autowired  
//    private WebApplicationContext wac;
//	private MockMvc mockMvc;     
//	
//	private String json = "{\"message\":\"系统推送USee\"}";  
//	
//	@Before
//	public void setUp() throws Exception {
//		 mockMvc = webAppContextSetup(wac).build();         
//	}
//
//	
//	/**
//	 * 删除评论测试
//	 */
//	@Test
//	public void test() throws Exception{
//		mockMvc.perform((post("/pushall"))
//			.contentType(MediaType.APPLICATION_JSON_VALUE)
//			.content(json.getBytes()) 
//			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
//			)
//		.andExpect(status().isOk())
//		.andExpect(content().contentType("application/json;charset=UTF-8"))
//		;
//	}
//}
