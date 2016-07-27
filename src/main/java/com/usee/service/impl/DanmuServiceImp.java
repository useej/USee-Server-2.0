package com.usee.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.CommentDao;
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
	
	public static int MaxRandomNameNumber = 100;
	public static int MaxRandomIconNumber = 10;
	
	public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String currentTime = df.format(new Date());
	
	public RandomNumber randomNumber = new RandomNumber();
	public int randomUserIconId = randomNumber.getRandom(1, MaxRandomIconNumber);
	public int randomUserNameId = randomNumber.getRandom(1, MaxRandomNameNumber);
	
	public void sendDammu(JSONObject danmu) {
		String userId = danmu.getString("userid");
		String topicId = danmu.getString("topicid");
		int randomIconId = 0;
		int randomNameId = 0;
		String lastVisitTime = currentTime;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		String userIcon = userId + ".png";
		
		if(userTopicDao.checkUserTopic(userId, topicId) != null){
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
			userTopic.setFirstvisit_time(currentTime);
			userTopic.setLastVisit_time(currentTime);
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
		newDanmu.setCreate_time(currentTime);
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

	public String getDanmuDetails(int danmuId) {
		int i = 0;
		
		Danmu danmu = danmuDao.getDanmu(danmuId);
		List<Comment> comments = commentDao.getCommentbyDanmuId(danmuId);
		
		JSONArray jsonArray_danmu = JSONArray.fromObject(danmu);
		JSONObject commentsJsonObject = new JSONObject();
		JSONObject danmuDetails = jsonArray_danmu.getJSONObject(0);

		for (Comment comment : comments) {
			User user = userDao.getUser(comment.getSender());
			
			/*
			 悄悄话需要后端过滤，获得当前用户的userID 
				1.	如果没有userID，即匿名使用，type=3的评论都不加载
				2.	如果有userID，返回sender或者receiver = currentUserID的评论。 
			*/
			if(user.getUserID() == null && comment.getType() == 3){
				continue;
			}
			
			if(user.getUserID() != null){
				if((!comment.getSender().equals(user.getUserID())) && (!comment.getReceiver().equals(user.getUserID()))){
					continue;
				}
			}
			
			JSONObject jsonObject_usercomment = new JSONObject();
			
			JSONArray jsonArray_user = new JSONArray();
			JSONArray jsonArray_comment = new JSONArray();
			
			jsonArray_user.add(user);
			jsonArray_comment.add(comment);
			jsonObject_usercomment.put("user", jsonArray_user);
			jsonObject_usercomment.put("comment", jsonArray_comment);

			commentsJsonObject.put("usercomment"+i, jsonObject_usercomment);
			danmuDetails.put("usercomments", commentsJsonObject);
			
			i ++;
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
		String userId = danmuComment.getString("userid");
		int danmuId = danmuComment.getInt("danmuid");
		String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
		
		int randomIconId = 0;
		int randomNameId = 0;
		String lastVisitTime = currentTime;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		String userIcon = userId + ".png";
		
		if(userTopicDao.checkUserTopic(userId, topicId) != null){
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
			userTopic.setFirstvisit_time(currentTime);
			userTopic.setLastVisit_time(currentTime);
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
		comment.setCreate_time(currentTime);
		
		commentDao.saveComment(comment);
		
		return comment;
	}
}
