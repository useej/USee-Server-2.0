package com.usee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.model.Comment;
import com.usee.model.Danmu;
import com.usee.model.DanmuImgs;
import com.usee.service.JPushService;
import com.usee.service.impl.DanmuImgsServiceImp;
import com.usee.service.impl.DanmuServiceImp;
import com.usee.utils.StatusCode;

import net.sf.json.JSONObject;


@Controller
public class DanmuController {
	@Autowired
	private DanmuServiceImp danmuService;
	@Autowired
	private JPushService jpushService;
    @Autowired
    private DanmuImgsServiceImp danmuImgsService;
	
	@RequestMapping(value = "senddanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> sendDanmu(@RequestBody String newDanmu){
		JSONObject newDanmuJson = new JSONObject().fromObject(newDanmu);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 检查弹幕
		int statusCode = danmuService.checkDanmu(newDanmuJson);
		returnMap.put("code", statusCode);
		// 判断返回码
		if(statusCode != StatusCode._200) {
			if(statusCode == StatusCode._405){
				returnMap.put("comment", null);
				return returnMap;
			}
		} 
		
		danmuService.sendDammu(newDanmuJson);
		Danmu danmu = danmuService.getDanmu(danmuService.getLatestDanmuId());
		
		// 保存图片
		newDanmuJson.put("danmuID", danmu.getId());
		DanmuImgs newdanmuImgs = danmuImgsService.saveDanmuImgs(newDanmuJson);
		danmu.setImgurls(newdanmuImgs.getImgurls());
		
		returnMap.put("danmu", danmu);
		System.out.println(returnMap);
		return returnMap;
		
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
	public Map<String, Object> commentDanmu(@RequestBody String danmuComment){
		JSONObject commentJson = new JSONObject().fromObject(danmuComment);
		System.out.println(commentJson);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 检查评论
		int statusCode = danmuService.checkComment(commentJson);
		returnMap.put("code", statusCode);
		// 判断返回码
		if(statusCode != StatusCode._200) {
			
		} 
		
		Comment comment = danmuService.commentDanmu(commentJson);
		// 调用推送接口
		String receiver = commentJson.getString("receiver");
		int type = commentJson.getInt("type");
		Map<String, String> extras = new HashMap<String, String>();
		if(type == 1) {
			extras.put("type", "common");
			jpushService.push(receiver, "有人评论您", extras);
		} else if(type == 2) {
			extras.put("type", "common");
			jpushService.push(receiver, "有人回复您的评论", extras);
		} else if(type == 3) {
			extras.put("type", "common");
			jpushService.push(receiver, "有人给你发悄悄话", extras);
		}
		
		returnMap.put("comment", comment);
		System.out.println(returnMap);
		return returnMap;
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

    /**
     * 已与getfavdanmulist接口合并到一起
     * @param info
     * @return
     */
    @RequestMapping(value = "havenewcomments", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String haveNewComments(@RequestBody String info){
        JSONObject danmuJson = new JSONObject().fromObject(info);
        String result = danmuService.haveNewComments(danmuJson);
        return result;
    }

    @RequestMapping(value = "reportcontent", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String reportContent(@RequestBody String info){
        JSONObject contentJson = new JSONObject().fromObject(info);
        String result = danmuService.reportContent(contentJson);
        return result;
    }
    
    @RequestMapping(value = "deletecomment", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String deleteComment(@RequestBody String json){
		JSONObject deleteCommentJson = new JSONObject().fromObject(json);
		boolean isSuccess = danmuService.deleteComment(deleteCommentJson);
		JSONObject result = new JSONObject();
		result.put("status", isSuccess);
		System.out.println(result);
		return result.toString();
	}
    
    @RequestMapping(value = "deletedanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String deleteDanmu(@RequestBody String json){
		JSONObject deleteDanmuJson = new JSONObject().fromObject(json);
		boolean isSuccess = danmuService.deleteDanmu(deleteDanmuJson);
		JSONObject result = new JSONObject();
		result.put("status", isSuccess);
		System.out.println(result);
		return result.toString();
	}
    
    @RequestMapping(value = "getnewdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getNewDanmu(@RequestBody String json){
		JSONObject newDanmuJson = new JSONObject().fromObject(json);
		String returnJson = danmuService.getNewDanmu(newDanmuJson);
		System.out.println(returnJson);
		return returnJson;
	}


    @RequestMapping(value = "getuserdmByInterval", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getIntervalDanmu(@RequestBody String json){
		JSONObject intervalDmJson = new JSONObject().fromObject(json);
		String startTime = intervalDmJson.getString("startTime");
		String endTime = intervalDmJson.getString("endTime");
		String topicID = intervalDmJson.getString("topicID");
		List<Map<String, String>> list = danmuService.getIntervalDanmu(topicID, startTime, endTime);
		
		JSONObject result = new JSONObject();
		result.put("intervaldanmu", list);
		System.out.println(result);
		return result.toString();
	}
    
    @RequestMapping(value = "gethotdanmu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getHotDanmu(@RequestBody String json){
    	JSONObject jsonObject = JSONObject.fromObject(json);
    	String topicID  = jsonObject.getString("topicID");
    	List<Danmu> hotDanmuList = danmuService.getHotDanmu(topicID);
    	JSONObject resultJson = new JSONObject();
		resultJson.put("hotdanmu", hotDanmuList);
		
		System.out.println(resultJson);
		return resultJson.toString();
    }
    
	/*
	 * 下面的与DanmuImgs有关
	 * 下面两个接口已经合并至其他接口，暂时废弃
	 */
	@RequestMapping(value = "savedanmuimgs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String saveDanmuImgs(@RequestBody String json){
    	JSONObject jsonObject = JSONObject.fromObject(json);
    	DanmuImgs newdanmuImgs = danmuImgsService.saveDanmuImgs(jsonObject);
    	JSONObject resultJson = new JSONObject();
		resultJson.put("danmuImgs", newdanmuImgs);
		return resultJson.toString();
    }
    
    @RequestMapping(value = "getdanmuimgs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getDanmuImgs(@RequestBody String json){
    	JSONObject jsonObject = JSONObject.fromObject(json);
    	int danmuID  = jsonObject.getInt("danmuID");
    	List<String> imgUrlList = danmuImgsService.getDanmuImgs(danmuID);
    	
    	JSONObject resultJson = new JSONObject();
		resultJson.put("imgUrl", imgUrlList);
		return resultJson.toString();
    }

}
