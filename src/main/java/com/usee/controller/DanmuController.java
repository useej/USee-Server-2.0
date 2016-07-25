package com.usee.controller;

import net.sf.json.JSONObject;

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
	
	@RequestMapping(value = "senddanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String sendDanmu(@RequestBody String newDanmu){
		JSONObject newDanmuJson = new JSONObject().fromObject(newDanmu);
		danmuService.sendDammu(newDanmuJson);
		Danmu danmu = danmuService.getDanmu(danmuService.getLatestDanmuId());
		System.out.println(danmu);
		return danmu.toString();
	}
	
	@RequestMapping(value = "getdmbytopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getDanmubyTopic(@RequestBody String topicInfo){
		JSONObject topicJsonObject = new JSONObject().fromObject(topicInfo);
		String pageNum = null;
		String pageSize = null;
		if(topicJsonObject.containsKey("pagenum")){
			pageNum = topicJsonObject.getString("pagenum");
		}
		if(topicJsonObject.containsKey("pagesize")){
			pageSize = topicJsonObject.getString("pagesize");
		}
		String danmu = danmuService.getDanmubyTopic(topicJsonObject.getString("topicid"), pageNum, pageSize);
		System.out.println(danmu);
		return danmu;
	}
	
	@RequestMapping(value = "getdmdetails", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getDanmuDetails(@RequestBody String danmuId){
		JSONObject danmuJson = new JSONObject().fromObject(danmuId);
		String danmuDetails = danmuService.getDanmuDetails(danmuJson.getString("danmuid"));
		System.out.println(danmuDetails);
		return danmuDetails;
	}
}
