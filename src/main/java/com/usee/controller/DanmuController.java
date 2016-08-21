package com.usee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.service.JPushService;
import com.usee.service.impl.DanmuServiceImp;

import net.sf.json.JSONObject;


@Controller
public class DanmuController {
	@Autowired
	private DanmuServiceImp danmuService;
	@Autowired
	private JPushService jpushService;
	
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
		// 调用推送接口
		String receiver = commentJson.getString("receiver");
		int type = commentJson.getInt("type");
		if(type == 1) {
			jpushService.push(receiver, "有人评论您");
		} else if(type == 2) {
			jpushService.push(receiver, "有人回复您的评论");
		} else if(type == 3) {
			jpushService.push(receiver, "有人给你发悄悄话");
		}
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

    @RequestMapping(value = "getlatestdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getLatestDanmu(@RequestBody String info){
        JSONObject danmujson = new JSONObject().fromObject(info);
        String danmu = danmuService.getLatestDanmuList(danmujson);
        System.out.println(danmu);
        return danmu;
    }

    @RequestMapping(value = "havenewcomments", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String haveNewComments(@RequestBody String info){
        JSONObject danmuJson = new JSONObject().fromObject(info);
        String result = danmuService.haveNewComments(danmuJson);
        return result;
    }
}
