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

public class GetRealnameInfoTestCase {

	@Autowired  
    private WebApplicationContext wac;
	private MockMvc mockMvc;     
	
	private String json = "{\"userID\":\"7A5214CA1C76457586F91E8EE4F0DCFB\"}";  
	
	@Before
	public void setUp() throws Exception {
		 mockMvc = webAppContextSetup(wac).build();         
	}

	
	/**
	 * 删除评论测试
	 */
	@Test
	public void test() throws Exception{
		mockMvc.perform((post("/user/getrealnameinfo"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(json.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		;
		
		/*
		{
			"userID":"7A5214CA1C76457586F91E8EE4F0DCFB",
			"gender":1,
			"nickname":"森林",
			"userIcon":"7A5214CA1C76457586F91E8EE4F0DCFB.png",
			"topic":[
				{
					"create_time":"1473499854",
					"danmuNum":32,
					"delete_time":"",
					"description":"贝微微&肖奈",
					"expired":0,
					"id":"1",
					"imgurls":[],
					"lastDanmu_time":"1474607673",
					"lat":31.912884,
					"lon":118.78847,
					"poi":"",
					"radius":15000,
					"title":"微微一笑很倾城",
					"type":"",
					"userID":"7A5214CA1C76457586F91E8EE4F0DCFB"
				},
			]
		}
		
		*/
	}
}
