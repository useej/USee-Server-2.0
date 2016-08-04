package com.usee.controller; 

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)   
@WebAppConfiguration(value = "src/main/webapp")  
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "classpath:spring-common.xml"),
    @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")
})   
@Transactional
public class UserTest {

	@Autowired  
    private WebApplicationContext wac;
	private MockMvc mockMvc;     
	
	private String signinJson = "{\"cellphone\":\"15150689613select ;\",\"password\":\"123456sselect ;\","
			+ "\"verificationCode\":\"25BCFCEB637CEC440E22AA7CCFE5FB11\"}";  
	private String loginJson = "{\"cellphone\":\"15150689613select ;\",\"password\":\"123456sselect ;\"}";  
	private String forgetPasswordJson = "{\"cellphone\":\"15150689612select ;\",\"password\":\"123456select ;\","
			+ "\"verificationCode\":\"25BCFCEB637CEC440E22AA7CCFE5FB11\"}";  
	private String modifyPasswordJson = "{\"userID\":\"E53537D4C4294FB88CF8C49C9820ECC5\","
			+ "\"oldPassword\":\"123456sselect ;\",\"newPassword\":\"1234560select ;\"}";
	private String updateUserJson = "{\"userID\":\"E53537D4C4294FB88CF8C49C9820ECC5\","
			+ "\"gender\":1,\"nickname\":\"liuhuaxinselect ;\",\"userIcon\":\"liuhuaxin.pngselect ;\"}"; 
	private String bindCellphoneJson = "{\"userID\":\"E53537D4C4294FB88CF8C49C9820ECC4\","
			+ "\"cellphone\":\"15150689614\",\"password\":\"123456a\","
			+ "\"verificationCode\":\"25BCFCEB637CEC440E22AA7CCFE5FB11\"}";   
	private String bindOAuthJson =  "{\"userID\":\"E53537D4C4294FB88CF8C49C9820ECC5\","
			+ "\"openID_qq\":\"das135da1sf23as\"}"; 
	
	@Before
	public void setUp() throws Exception {
		 mockMvc = webAppContextSetup(wac).build();         
	}

	/**
	 * 通过手机号注册测试
	 */
	@Test
	public void signinTest() throws Exception{
		mockMvc.perform((post("/user/signin"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(signinJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.returnInfo").value("success"))
		.andExpect(jsonPath("$.user").exists())
		.andExpect(jsonPath("$.user.password").value("123456s"))
		.andExpect(jsonPath("$.user.userIcon").value("1.png"))
		;
	}
	
	/**
	 * 通过手机号登录测试
	 */
	@Test
	public void loginTest() throws Exception{
		mockMvc.perform((post("/user/login"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(loginJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.returnInfo").value("success"))
		.andExpect(jsonPath("$.user").exists())
		;
	}
	
	/**
	 * 忘记密码测试
	 */
	@Test
	public void forgetPasswordTest() throws Exception{
		mockMvc.perform((post("/user/forgetpassword"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(forgetPasswordJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.returnInfo").value("success"))
		.andExpect(jsonPath("$.password").value("123456"))
		;
	}
	
	/**
	 * 修改密码测试
	 */
	@Test
	public void modifyPasswordTest() throws Exception{
		mockMvc.perform((post("/user/modifypassword"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(modifyPasswordJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.returnInfo").value("success"))
		.andExpect(jsonPath("$.password").value("1234560"))
		;
	}

	/**
	 * 更新信息
	 */
	@Test
	public void updateUserTest() throws Exception{
		mockMvc.perform((post("/user/updateuser"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(updateUserJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.userID").value("E53537D4C4294FB88CF8C49C9820ECC5"))
		.andExpect(jsonPath("$.gender").value(1))
		.andExpect(jsonPath("$.nickname").value("liuhuaxin"))
		.andExpect(jsonPath("$.userIcon").value("liuhuaxin.png"))
		;
	}
	
	/**
	 * 绑定手机号
	 */
	@Test
	public void bindCellphoneTest() throws Exception{
		mockMvc.perform((post("/user/bindcellphone"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(bindCellphoneJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.returnInfo").value("success"))
		.andExpect(jsonPath("$.cellphone").value("15150689614"))
		.andExpect(jsonPath("$.password").value("123456a"))
		;
	}
	
	/**
	 * 绑定第三方账号
	 */
	@Test
	public void bindOAuthTest() throws Exception{
		mockMvc.perform((post("/user/bindoauth"))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(bindOAuthJson.getBytes()) 
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
			)
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(jsonPath("$.openID_qq").value("das135da1sf23as"))
		;
	}
	
}
