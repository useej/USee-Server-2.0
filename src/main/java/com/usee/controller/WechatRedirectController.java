package com.usee.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.usee.model.User;
import com.usee.service.impl.UserServiceImpl;
import com.usee.service.impl.WechatRedirectServiceImpl;

@Controller
public class WechatRedirectController {
	private static final String DEFAULT_CELLPHONE = "<dbnull>";
	
	@Autowired
	WechatRedirectServiceImpl wechatRedirectService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@RequestMapping(value = "useeauth", method = RequestMethod.GET)
	public ModelAndView authToUSee(@RequestParam String code, @RequestParam String state, HttpServletRequest request) {
		
		 ModelAndView modelAndView = new ModelAndView("/user/index");  
		 
		// 获取用户的 access_token 和 openid
		Map<String, String> token_data = wechatRedirectService.getToken(code);
		
		User validateUser = userService.getUserByOpenId("openID_wx", token_data.get("openid"));
		
		// 假如数据库中没有对应的用户信息,则证明用户第一次登录
		if(validateUser == null) {
			// 得到用户信息
			User user = wechatRedirectService.getUserInfo(token_data.get("access_token"), token_data.get("openid"));
			wechatRedirectService.addUser(user, request.getSession().getServletContext().getRealPath("/"));
			// 加入数据库中的user信息为默认的手机号(用户是用第三方登录的，没有设置手机号和密码)
			// 则将手机号和密码置为空再返回给前端
			if(user.getCellphone().equals(DEFAULT_CELLPHONE)) {
				user.setCellphone(null);
				user.setPassword(null);
			}
			modelAndView.addObject("user", user);  
		} else {
			if(validateUser.getCellphone().equals(DEFAULT_CELLPHONE)) {
				validateUser.setCellphone(null);
			}
			validateUser.setPassword(null);
			modelAndView.addObject("user", validateUser);  
		}
		
		 return modelAndView;  
	}
}
