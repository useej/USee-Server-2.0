package com.usee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(value = "getdmbytopic", method = RequestMethod.POST)
	public String getDanmubyTopic(Model model,String topicId,String pageNum,String pageSize){
		String danmu = danmuService.getDanmubyTopic(topicId, pageNum, pageSize);
		model.addAttribute("danmu", danmu);
		return "getdm";
	}
	
	@RequestMapping(value = "getdmdetails", method = RequestMethod.POST)
	public String getDanmuDetails(Model model, String danmuId){
		String danmuDetails = danmuService.getDanmuDetails(danmuId);
		model.addAttribute("danmuDetails", danmuDetails);
		return "getdm";
	}
}
