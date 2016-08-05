package com.usee.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.utils.TimeUtil;
import com.usee.dao.impl.CommentDaoImpl;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.UserDaoImpl;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Comment;
import com.usee.model.Danmu;
import com.usee.model.User;
import com.usee.model.UserTopic;
import com.usee.service.DanmuService;
import com.usee.utils.RandomNumber;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class DanmuServiceImp implements DanmuService{
	@Resource
	private DanmuDaoImp danmuDao;
	@Resource
	private UserTopicDaoImp userTopicDao;
	@Resource
	private UserDaoImpl userDao;
	@Resource
	private CommentDaoImpl commentDao;
	
	public static final int MAX_RANDOM_NAME_NUMBER = 100;
	public static final int MAX_RANDOM_ICON_NUMBER = 10;
	
	public RandomNumber randomNumber = new RandomNumber();
	public int randomUserIconId = randomNumber.getRandom(1, MAX_RANDOM_ICON_NUMBER);
	public int randomUserNameId = randomNumber.getRandom(1, MAX_RANDOM_NAME_NUMBER);
	
	public void sendDammu(JSONObject danmu) {
		TimeUtil timeUtil = new TimeUtil();
		
		String userId = danmu.getString("userid");
		String topicId = danmu.getString("topicid");
		int randomIconId = 0;
		int randomNameId = 0;
		String lastVisitTime = timeUtil.currentTimeStamp;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		String userIcon = userId + ".png";
		
		if(userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId) != null){
			if(danmu.getBoolean("isannoymous")){
				randomIconId = randomUserIconId;
				randomNameId = randomUserNameId;
				userIcon = randomIconId + ".png";
			}
			userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
		}
		else{
			UserTopic userTopic = new UserTopic();
			userTopic.setUserId(userId);
			userTopic.setTopicId(topicId);
			userTopic.setFirstvisit_time(timeUtil.currentTimeStamp);
			userTopic.setLastVisit_time(timeUtil.currentTimeStamp);
			userTopic.setFrequency(0);
			if(danmu.getBoolean("isannoymous")){
				userTopic.setRandomNameID(randomUserNameId);
				userTopic.setRandomIconID(randomUserIconId);
			}
			else{
				userTopic.setRandomNameID(0);
				userTopic.setRandomIconID(0);
				userTopic.setUserIcon(userId + ".png");
			}
			userTopicDao.saveUserTopic(userTopic);
		}
		
		Danmu newDanmu = new Danmu();
		newDanmu.setDevId(danmu.getString("devid"));
		newDanmu.setUserId(userId);
		newDanmu.setStatus("0");
		newDanmu.setTopicId(danmu.getString("topicid"));
		newDanmu.setLon(danmu.getString("lon"));
		newDanmu.setLat(danmu.getString("lat"));
		newDanmu.setCreate_time(timeUtil.currentTimeStamp);
		newDanmu.setAddress("江苏省南京市佛城西路河海大学江宁校区");
		newDanmu.setDelete_time(danmu.getString("delete_time"));
		newDanmu.setMessages(danmu.getString("messages"));
		if(danmu.getBoolean("isannoymous")){
			newDanmu.setUserIcon(randomUserIconId + ".png");
		}
		else {
			newDanmu.setUserIcon(userId + ".png");
		}
		danmuDao.saveDanmu(newDanmu);
	}

	
	public String getDanmubyTopic(String topicId, String pageNum,
			String pageSize) {
		List<Danmu> list = new ArrayList<Danmu>();
		if((pageNum == "" && pageSize == "")||(pageNum == null && pageSize == null)){
			list = danmuDao.getDanmuList(topicId);
			System.out.println(list.get(0).getMessages());
		}
		else {
			int _pageNum = Integer.parseInt(pageNum);
			int _pageSize = Integer.parseInt(pageSize);
			list = danmuDao.getDanmuList(topicId, _pageNum, _pageSize);
			System.out.println(list.get(0).getMessages());
		}
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmu", array.toString());
		
		return object.toString();
	} 

	public String getDanmuDetails(int danmuId, String currentUserId) {
		
		Danmu danmu = danmuDao.getDanmu(danmuId);
		List<Comment> comments = commentDao.getCommentbyDanmuId(danmuId);
		User danmuSender = userDao.getUser(danmu.getUserId());
		
		updateUserDanmu(currentUserId, danmuId);
		
		JSONArray jsonArray_danmu = JSONArray.fromObject(danmu);
		JSONArray jsonArray_usercomment = new JSONArray();
		JSONObject danmuDetails = jsonArray_danmu.getJSONObject(0);
		danmuDetails.put("nickname", danmuSender.getNickname());
		danmuDetails.put("gender", danmuSender.getGender());
		danmuDetails.put("action", danmuDao.getActionbyUserIdandDanmuId(currentUserId, danmuId));
		danmuDetails.put("isfav", danmuDao.checkUserFavDanmu(currentUserId, danmuId));

		for (Comment comment : comments) {
			User sender = userDao.getUser(comment.getSender());
			User receiver = userDao.getUser(comment.getReceiver());
			
			/*
			 悄悄话需要后端过滤，获得当前用户的userID 
				1.	如果没有userID，即匿名使用，type=3的评论都不加载
				2.	如果有userID，返回sender或者receiver = currentUserID的评论。 
			*/
			if(sender.getUserID() == null && comment.getType() == 3){
				continue;
			}
			
			if(sender.getUserID() != null){
				if((!comment.getSender().equals(sender.getUserID())) && (!comment.getReceiver().equals(sender.getUserID()))){
					continue;
				}
			}
			
			JSONObject jsonObject_usercomment = new JSONObject();
			
			jsonObject_usercomment.put("user", sender);
			jsonObject_usercomment.put("comment", comment);
			jsonObject_usercomment.put("replycomment_name", receiver.getNickname());	//应该是receiver_name和receiver_gender
			jsonObject_usercomment.put("replycomment_gender", receiver.getGender());

			jsonArray_usercomment.add(jsonObject_usercomment);
			danmuDetails.put("usercomments", jsonArray_usercomment);
		}		
		return danmuDetails.toString();
	}
	
	public int getLatestDanmuId(){
		return danmuDao.getLatestDanmuId();
	}
	
	public Danmu getDanmu(int danmuId){
		return danmuDao.getDanmu(danmuId);
	}
	
	public Comment commentDanmu(JSONObject danmuComment){
		TimeUtil timeUtil = new TimeUtil();
		
		String userId = danmuComment.getString("userid");
		int danmuId = danmuComment.getInt("danmuid");
		String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
		
		int randomIconId = 0;
		int randomNameId = 0;
		String lastVisitTime = timeUtil.currentTimeStamp;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		String userIcon = userId + ".png";
		
		if(userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId) != null){
			if(danmuComment.getBoolean("isannoymous")){
				randomIconId = randomUserIconId;
				randomNameId = randomUserNameId;
				userIcon = randomIconId + ".png";
			}
			userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
		}
		else{
			UserTopic userTopic = new UserTopic();
			userTopic.setUserId(userId);
			userTopic.setTopicId(topicId);
			userTopic.setFirstvisit_time(timeUtil.currentTimeStamp);
			userTopic.setLastVisit_time(timeUtil.currentTimeStamp);
			userTopic.setFrequency(0);
			if(danmuComment.getBoolean("isannoymous")){
				userTopic.setRandomNameID(randomUserNameId);
				userTopic.setRandomIconID(randomUserIconId);
			}
			else{
				userTopic.setRandomNameID(0);
				userTopic.setRandomIconID(0);
				userTopic.setUserIcon(userId + ".png");
			}
			userTopicDao.saveUserTopic(userTopic);
		}
		
		Comment comment = new Comment();
		comment.setDanmuId(danmuId);
		comment.setSender(userId);
		comment.setReceiver(danmuComment.getString("receiver"));
		comment.setContent(danmuComment.getString("content"));
		if(danmuComment.get("reply_commentid") != null){
			comment.setReply_commentId(danmuComment.getInt("reply_commentid"));
		}
		comment.setType(danmuComment.getInt("type"));
		comment.setCreate_time(timeUtil.currentTimeStamp);
		
		commentDao.saveComment(comment);
		
		return comment;
	}

	@Override
	public boolean upDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		String upTime = timeUtil.currentTimeStamp;
		
		return danmuDao.updateUserUpDanmu(jsonObject.getBoolean("isup"), jsonObject.getString("userid"), jsonObject.getInt("danmuid"), upTime);
	}


	@Override
	public boolean downDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		String downTime = timeUtil.currentTimeStamp;
		
		return danmuDao.updateUserDownDanmu(jsonObject.getBoolean("isdown"), jsonObject.getString("userid"), jsonObject.getInt("danmuid"), downTime);
	}


	@Override
	public boolean favDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		String favTime = timeUtil.currentTimeStamp;
		String userId = jsonObject.getString("userid");
		int danmuId = jsonObject.getInt("danmuid");
		
		Boolean isFav = danmuDao.checkUserFavDanmu(userId, danmuId);
		
		return danmuDao.updateUserFavDanmu(isFav, userId, danmuId, favTime);
	}


	@Override
	public void updateUserDanmu(String userId, int danmuId) {
		TimeUtil timeUtil = new TimeUtil();
		int frequency = danmuDao.getLatestFrequency();
		
		if(danmuDao.getUniqueUserDanmubyUserIdandDanmuId(userId, danmuId) == 1){
			danmuDao.updateUserDanmu(userId, danmuId, timeUtil.currentTimeStamp, frequency+1);
		}
		else {
			danmuDao.saveUserDanmu(userId, danmuId, timeUtil.currentTimeStamp, timeUtil.currentTimeStamp, 0);
		}
		
	}

	@Override
	public String getFavDanmuList(JSONObject jsonObject) {
		
		List<Object[]> list = danmuDao.listFavDanmu(jsonObject.getString("userid"));
		JSONArray jsonArray = JSONArray.fromObject(list);
		//System.out.println(jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i ++) {
			JSONObject tempJsonObject = jsonArray.getJSONObject(i);
			int danmuId = tempJsonObject.getInt("danmuID");
			String messages = danmuDao.getDanmu(danmuId).getMessages();
			tempJsonObject.put("messages", messages);
			//jsonArray.add(tempJsonObject);
		}
		JSONObject json = new JSONObject();
		json.put("favdanmu", jsonArray);
		System.out.println(json.toString());
		return json.toString();
	}

	@Override
	public int updateUserAction(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		
		String userId = jsonObject.getString("userid");
		int danmuId = jsonObject.getInt("danmuid");
		int action = jsonObject.getInt("action");
		if (danmuDao.getUniqueUpDownDanmubyUserIdandDanmuId(userId, danmuId) == 1) {
			return danmuDao.updateUserDanmuAction(userId, danmuId, action, timeUtil.currentTimeStamp);
		} else {
			return danmuDao.saveUserDanmuAction(userId, danmuId, action, timeUtil.currentTimeStamp);
		}
	}
}
