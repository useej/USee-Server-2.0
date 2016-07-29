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
//<<<<<<< HEAD:src/test/java/com/usee/controller/getDanmubyTopicTest.java
//public class getDanmubyTopicTest {
//=======
public class GetDanmubyTopicTestCase {
//>>>>>>> upstream/dev:src/test/java/com/usee/controller/GetDanmubyTopicTestCase.java

	@Autowired  
    private WebApplicationContext wac;
	private MockMvc mockMvc;     
	
	@Before
	public void setUp() throws Exception {
		 mockMvc = webAppContextSetup(wac).build();         
	}

	/**
	 * 通过话题Id获取弹幕测试
	 */
	@Test
	public void test() throws Exception{
		mockMvc.perform((post("/getdmbytopic").param("topicId", "0").param("pageSize", "1").param("pageNum", "1"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.danmu.*.address").value("8"))
        //.andExpect(jsonPath("$.danmu.*.address").doesNotExist())
		;
		
//		String errorBody = "{address:0, commentnum:0}";  
//		MvcResult result = mockMvc.perform((post("/getdmbytopic").param("topicId", "0").param("pageSize", "1").param("pageNum", "1"))
//		        .contentType(MediaType.APPLICATION_JSON).content(errorBody)  
//		        .accept(MediaType.APPLICATION_JSON)) // 执行请求  
//		        .andExpect(status().isBadRequest()) //400 错误请求  
//		        .andReturn();  
		  
//		Assert.assertTrue(HttpMessageNotReadableException.class.isAssignableFrom(result.getResolvedException().getClass()));// 错误的请求内容体  
	}

//	@Test
//    public void test2() throws Exception {
//        // 全局配置
//        mockMvc = webAppContextSetup(wac)
//        		.defaultRequest(post("/getdmbytopic").requestAttr("default", true)) // 默认请求 如果其是 Mergeable 类型的，会自动合并的哦 mockMvc.perform 中的 RequestBuilder
//                .alwaysDo(print())  // 默认每次执行请求后都做的动作
//                .alwaysExpect(request().attribute("default", true)) // 默认每次执行后进行验证的断言
//                .build();
//
//        mockMvc.perform(post("/getdmbytopic").param("topicId", "0").param("pageSize", "1").param("pageNum", "1"))
//        		.andExpect(model().attributeExists("danmu"));
//    }
}
