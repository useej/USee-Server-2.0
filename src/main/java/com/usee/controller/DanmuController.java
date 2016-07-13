package com.usee.controller;

import javax.servlet.http.HttpServletRequest;
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
	
	@RequestMapping(value = "getdmbytopic", method = RequestMethod.POST)
	public void getDanmubyTopic(Model model,String topicId,String pageNum,String pageSize){
		String danmu = danmuService.getDanmubyTopic(topicId, pageNum, pageSize);
		model.addAttribute("danmu", danmu);
	}
	
	@RequestMapping(value = "getdmdetails", method = RequestMethod.POST)
	public void getDanmuDetails(Model model, String danmuID){
		
	}
}
