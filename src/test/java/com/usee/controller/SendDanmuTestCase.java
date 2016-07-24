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
	
	@Test
	public void test() throws Exception{
		String temp = "{\"danmu\":[{\"address\":\"33\",\"commentnum\":0,\"create_time\":\"1\",\"delete_time\":\"1\",\"devId\":\"33\",\"downnum\":3,\"head\":1,\"hitnum\":3,\"id\":1,\"lat\":\"1\",\"lon\":\"1\",\"messages\":\"3333333 \",\"praisenum\":1,\"status\":\"1\",\"topicId\":\"1\",\"userId\":\"866328023315987\"}]}";
		//JSONObject danmu = new JSONObject(temp);
		mockMvc.perform((post("/senddanmu").param("danmu", temp))
			.contentType(MediaType.TEXT_PLAIN)
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		//.andExpect(jsonPath("$.danmudetails.*.receiver").value("866328023315987"))
		;
	}
}
