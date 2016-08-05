package com.usee.controller;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.model.Comment;
import com.usee.model.Danmu;
import com.usee.service.impl.DanmuServiceImp;


@Controller
public class DanmuController {
	@Autowired
	private DanmuServiceImp danmuService;
	
	@RequestMapping(value = "senddanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String sendDanmu(@RequestBody String newDanmu){
		JSONObject newDanmuJson = new JSONObject().fromObject(newDanmu);
		danmuService.sendDammu(newDanmuJson);
		String danmu = danmuService.getDanmu(danmuService.getLatestDanmuId()).toString();
		System.out.println(danmu);
		return danmu;
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
		String danmuDetails = danmuService.getDanmuDetails(danmuJson.getInt("danmuid"), danmuJson.getString("userid"));
		System.out.println(danmuDetails);
		return danmuDetails;
	}
	
	@RequestMapping(value = "commentdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String commentDanmu(@RequestBody String danmuComment){
		JSONObject commentJson = new JSONObject().fromObject(danmuComment);
		String comment = danmuService.commentDanmu(commentJson).toString();
		System.out.println(comment);
		return comment;
	}
	
	@RequestMapping(value = "updanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String upDanmu(@RequestBody String info){
		JSONObject danmuJson = new JSONObject().fromObject(info);
		boolean status = danmuService.upDanmu(danmuJson);
		JSONObject result = new JSONObject();
		result.put("status", status);
		return result.toString();
	}
	
	@RequestMapping(value = "downdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String downDanmu(@RequestBody String info){
		JSONObject danmuJson = new JSONObject().fromObject(info);
		boolean status = danmuService.downDanmu(danmuJson);
		JSONObject result = new JSONObject();
		result.put("status", status);
		return result.toString();
	}
	
	@RequestMapping(value = "favdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String favDanmu(@RequestBody String info){
		JSONObject danmuJson = new JSONObject().fromObject(info);
		boolean status = danmuService.favDanmu(danmuJson);
		JSONObject result = new JSONObject();
		result.put("status", status);
		return result.toString();
	}
	
//	@RequestMapping(value = "clickdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//	@ResponseBody
//	public void clickDanmu(@RequestBody String info){
//		JSONObject danmuJson = new JSONObject().fromObject(info);
//		danmuService.updateUserDanmu(danmuJson);
//	}
	
	@RequestMapping(value = "getfavdanmulist", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getFavDanmuList(@RequestBody String info){
		JSONObject danmuJson = new JSONObject().fromObject(info);
		return danmuService.getFavDanmuList(danmuJson);
	}
	
	@RequestMapping(value = "updateuseraction", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String updateUserAction(@RequestBody String info){
		JSONObject danmuJson = new JSONObject().fromObject(info);
		int status = danmuService.updateUserAction(danmuJson);
		JSONObject result = new JSONObject();
		result.put("result", status);
		return result.toString();
	}
}
