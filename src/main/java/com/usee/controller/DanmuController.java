package com.usee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.model.Danmu;
import com.usee.service.impl.DanmuServiceImp;


@Controller
public class DanmuController {
	@Autowired
	private DanmuServiceImp danmuService;
	
	@RequestMapping(value = "test")
	public String test(Model model){
		return "getdm";
	}
	
	@RequestMapping(value = "senddanmu", method = RequestMethod.POST, headers="Accept=text/plain;charset=utf-8", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Danmu sendDanmu(@RequestBody Danmu newDanmu, @RequestBody boolean isAnnonymous){
		danmuService.sendDammu(newDanmu, isAnnonymous);
		Danmu danmu = danmuService.getDanmu(danmuService.getLatestDanmuId());
		return danmu;
	}
	
	@RequestMapping(value = "getdmbytopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getDanmubyTopic(Model model,String topicId,String pageNum,String pageSize){
		String danmu = danmuService.getDanmubyTopic(topicId, pageNum, pageSize);
		System.out.println(danmu);
		return danmu;
	}
	
	@RequestMapping(value = "getdmdetails", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getDanmuDetails(Model model, String danmuId){
		String danmuDetails = danmuService.getDanmuDetails(danmuId);
		System.out.println(danmuDetails);
		return danmuDetails;
	}
}
