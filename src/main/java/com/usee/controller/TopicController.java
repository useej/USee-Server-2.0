package com.usee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.api.auth.AuthException;
import com.usee.model.Topic;
import com.usee.model.Topicimg;
import com.usee.service.impl.TopicImgServiceImp;
import com.usee.service.impl.TopicServiceImpl;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

@Controller
public class TopicController {
	@Autowired
	private TopicServiceImpl topicService;
    @Autowired
    private TopicImgServiceImp topicImgService;

	@RequestMapping(value = "getusertopics", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserTopics(@RequestBody String userID){
		JSONObject userIDJson =  JSONObject.fromObject(userID);
		String userTopics = topicService.getUserTopics(userIDJson.getString("userid"));
		System.out.println(userTopics);
		return userTopics ;
	}

	@RequestMapping(value = "getnearbytopics", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getNearbyTopics(@RequestBody String location ){
		JSONObject locationJson = JSONObject.fromObject(location);
		double ux = locationJson.getDouble("lon");
		double uy = locationJson.getDouble("lat");
		int userRadius = locationJson.getInt("radius");
		String userid = locationJson.getString("userid");
		String NearbyTopics = topicService.getNearbyTopics(ux,uy,userRadius,userid);
		System.out.println(NearbyTopics);
		return NearbyTopics;
	}

	@RequestMapping(value = "getusericonbytopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserIconbyTopic(@RequestBody String userAndTopicInfo, HttpServletRequest request){
		JSONObject userAndTopicInfoJson = JSONObject.fromObject(userAndTopicInfo);
		String userId = userAndTopicInfoJson.getString("userid");
		String topicId = userAndTopicInfoJson.getString("topicid");

		JSONObject iconNameJsonObject = topicService.getUserIconbyTopic(userId, topicId);

		JSONObject userIconjJsonObject = new JSONObject();
		String userIcon = iconNameJsonObject.getString("iconname");
		String userName = iconNameJsonObject.getString("username");
		int isAnonymous = iconNameJsonObject.getInt("isAnonymous");
		int randomIconId = iconNameJsonObject.getInt("randomIconId");

		userIconjJsonObject.put("usericon", userIcon);
		userIconjJsonObject.put("username", userName);
		userIconjJsonObject.put("isanonymous", isAnonymous);
		userIconjJsonObject.put("randomIconId", randomIconId);
		return userIconjJsonObject.toString();
	}

	@RequestMapping(value = "getusericonbycomment", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getUserIconByComment(@RequestBody String userAndTopicInfo, HttpServletRequest request){
		JSONObject userAndTopicInfoJson = JSONObject.fromObject(userAndTopicInfo);
		String userId = userAndTopicInfoJson.getString("userid");
		int danmuId = userAndTopicInfoJson.getInt("danmuid");

		JSONObject iconNameJsonObject = topicService.getUserIconByComment(userId, danmuId);

		JSONObject userIconjJsonObject = new JSONObject();
		String userIcon = iconNameJsonObject.getString("iconname");
		String userName = iconNameJsonObject.getString("username");
		int isAnonymous = iconNameJsonObject.getInt("isAnonymous");
		int randomIconId = iconNameJsonObject.getInt("randomIconId");

		userIconjJsonObject.put("usericon", userIcon);
		userIconjJsonObject.put("username", userName);
		userIconjJsonObject.put("isanonymous", isAnonymous);
		userIconjJsonObject.put("randomIconId", randomIconId);
		return userIconjJsonObject.toString();
	}


	@RequestMapping(value = "updateusertopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void updateUser_topic(@RequestBody String ID){
		JSONObject IDJson =  JSONObject.fromObject(ID);
		topicService.updateUser_topic(IDJson.getString("userid"),IDJson.getString("topicid"));
	}

	@RequestMapping(value = "createtopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Topic createtopic(@RequestBody String newTopic){
		JSONObject newTopicJson = JSONObject.fromObject(newTopic);
		
		String newId = topicService.createTopic(newTopicJson);
		Topic userTopics = topicService.getTopic(newId);
		topicService.changeTypeOfTopic(userTopics);

		// 保存图片
		newTopicJson.put("topicID", userTopics.getId());
		Topicimg newtopicimg = topicImgService.saveTopicimg(newTopicJson);
		userTopics.setImgurls(newtopicimg.getImgurls());

		System.out.println(userTopics);
		return userTopics;
	}


	@RequestMapping(value = "searchtopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchtopic(@RequestBody String keyword){
		JSONObject keywordJson =  JSONObject.fromObject(keyword);
		String userTopics = topicService.searchTopic(keywordJson.getString("keyword"));
		System.out.println(userTopics);
		return userTopics ;
	}

    @RequestMapping(value = "gethotsearch", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getHotSearch(){
        return topicService.getHotestTopics();
    }

    @RequestMapping(value = "disliketopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public void disliketopic(@RequestBody String topic){
        JSONObject topicjson =  JSONObject.fromObject(topic);
        String userID = topicjson.getString("userid");
        String topics = topicjson.getString("topics");
        String[] topicArray = topics.split(",");
        ArrayList<String> list = new ArrayList<String>();
        for(String s:topicArray){
            list.add(s);
        }
        topicService.dislikeTopic(userID, list);
    }

    @RequestMapping(value = "checkversion", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkVersion(){
        JSONObject jsonObject = new JSONObject();
        String[] version = topicService.checkVersion().split(",");
        jsonObject.put("versionCode", version[0]);
        jsonObject.put("versionName", version[1]);
        return jsonObject.toString();
    }

    @RequestMapping(value = "liketopic", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public void liketopic(@RequestBody String topic){
        JSONObject topicjson =  JSONObject.fromObject(topic);
        String userID = topicjson.getString("userid");
        String topics = topicjson.getString("topics");
        String[] topicArray = topics.split(",");
        ArrayList<String> list = new ArrayList<String>();
        for(String s:topicArray){
            list.add(s);
        }
        topicService.likeTopic(userID, list);
    }

    @RequestMapping(value = "gettopictitle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> getTopicTitleForWeb(@RequestBody String topicIDJson){
    	Map<String, Object> returnMap = new HashMap<String, Object>();

    	ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(topicIDJson, new TypeReference<Map<String, String>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
    	String topicID = map.get("topicID");

    	String topicTitle = topicService.getTopicTitleForWeb(topicID);
    	returnMap.put("topicTitle", topicTitle);
    	return returnMap;
    }

    @ResponseBody
	@RequestMapping(value = "gettopicinfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String getTopicInfo(@RequestBody String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String topicID  = jsonObject.getString("topicID");

		Topic topic = topicService.getTopic(topicID);
		JSONObject resultJson = new JSONObject();
		resultJson.put("topic", topic);

		System.out.println(resultJson);
		return resultJson.toString();
	}


    /*
     * 下面的与topicImg有关
     * 下面两个接口已经合并至其他接口，暂时废弃，只有getuptoken有用
     */
    @RequestMapping(value = "getuptoken", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getuptoken( ) throws AuthException, JSONException{
        String resultJson = topicImgService.getuptoken();
        return resultJson.toString();
    }

    @RequestMapping(value = "savetopicimg", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String saveTopicImg(@RequestBody String json){
    	JSONObject jsonObject = JSONObject.fromObject(json);
    	Topicimg newtopicimg = topicImgService.saveTopicimg(jsonObject);
    	JSONObject resultJson = new JSONObject();
		resultJson.put("topicimg", newtopicimg);
		return resultJson.toString();
    }

    @RequestMapping(value = "gettopicimg", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getTopicImg(@RequestBody String json){
    	JSONObject jsonObject = JSONObject.fromObject(json);
    	String topicID  = jsonObject.getString("topicID");
    	List<String> imgUrlList = topicImgService.getTopicimg(topicID);

    	JSONObject resultJson = new JSONObject();
		resultJson.put("imgUrl", imgUrlList);
		return resultJson.toString();
    }

		/**
		 * 通过话题类型得到topics
		 * @param typeID
		 * @return
		 */
		@RequestMapping(value = "gettopicsbytype", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getTopicsbyType(@RequestBody String typeID){
		JSONObject typeJsonObject = JSONObject.fromObject(typeID);
		typeID=typeJsonObject.getString("typeID");

		String topic = topicService.getTopicsbyType(typeID);
		
		
		
		return topic;
	}
		/**
		 * 更新话题类型     还没写好  暂用作测试
		 * @param topictype
		 * @return
		 */
	@RequestMapping(value = "updatetopictype", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public void updateTopicType(@RequestBody String topictype){
		JSONObject topictypeJsonObject = JSONObject.fromObject(topictype);
		
		String topicID = null;
		if(topictypeJsonObject.containsKey("topicTitle")) {
			String topicTitle = topictypeJsonObject.getString("topicTitle");
			topicID = topicService.getTopicIDBytitle(topicTitle);
			System.out.println(topicTitle + " : " + topicID);
		} else {
			topicID = topictypeJsonObject.getString("topicID");
		}
		
		JSONArray typeIDJson = topictypeJsonObject.getJSONArray("typeID");
		List<Integer> typeIDList = JSONArray.toList(typeIDJson);
		
		topicService.addTopicType(topicID, typeIDList);
		topicService.updateType(topicID,typeIDList.size());
		
		Topic topic = topicService.getTopic(topicID);
		String topicStr = topicService.changeTypeOfTopic(topic);
		System.out.println(topicStr);
		
		return;
	}

	

}
