package com.usee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.moddel.Danmu;
import com.usee.moddel.User;
import com.usee.serivce.impl.DanmuServiceImp;


@Controller
@RequestMapping("/danmu")
public class DanmuController {
	@Resource
	private DanmuServiceImp danmuService;
	@ResponseBody 
	@RequestMapping("/send")
	public String sendDanmu(Danmu danmu, HttpServletRequest request) {
		System.out.println("开始存储");
		danmuService.sendDammu(danmu);
		
		return "hello";
	}
	@ResponseBody 
	@RequestMapping("/getByTopic")
	public String getDanmubyTopic(String topicId,String pageNum,String pageSize){
		String result=danmuService.getDanmubyTopic(topicId, pageNum, pageSize);
		return result;
	}
}
