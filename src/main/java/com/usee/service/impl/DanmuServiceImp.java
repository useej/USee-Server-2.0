package com.usee.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.ColorDao;
import com.usee.dao.RandomNameDao;
import com.usee.dao.TopicDao;
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
import com.usee.utils.TimeUtil;

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
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private RandomNameDao randomNameDao;
	
	public static final int MAX_RANDOM_NAME_NUMBER = 590;
	public static final int MAX_RANDOM_ICON_NUMBER = 6400;
	
	public RandomNumber randomNumber = new RandomNumber();
	public int randomUserIconId = 0;
//	public int randomUserNameId = randomNumber.getRandom(1, MAX_RANDOM_NAME_NUMBER);
	
	public void sendDammu(JSONObject danmu) {
		TimeUtil timeUtil = new TimeUtil();
		
		// 前台传输过来的userIcon和userName和randomIconId
		String _randomUserIcon = danmu.getString("randomUserIcon");
		String _randomUserName = danmu.getString("randomUserName");
		int _randomIconId = danmu.getInt("randomIconId");
		
		String userId = danmu.getString("userid");
		String topicId = danmu.getString("topicid");
		int randomIconId = 0;
		int randomNameId = 0;
		String lastVisitTime = timeUtil.currentTimeStamp;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		String userIcon = userId + ".png";
		
		// 通过userId和topicId从数据库中获取存在的UserTopic信息
		UserTopic existUserTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId);
		// 如果UserTopic存在记录,则用户不是第一次在此话题底下做操作
		if(existUserTopic != null){
			// 如果用户选择匿名发送,并且existUserTopic.getUserIcon()中存在字符串.png：则证明之前是实名发送,现在是匿名发送
			if(danmu.getBoolean("isannoymous") && existUserTopic.getUserIcon().contains(".png")) {
				// 如果用户第一次使用匿名,则将前台发送回来的userIcon和userName和randomIconId做相应处理保存下来
				if(existUserTopic.getRandomIconID() == 0) {
					// 由于randomUserIconId之前保存过了,所以不需要进行 
					randomIconId = _randomIconId;
					randomNameId = randomNameDao.getRandomNameIdByIdRandomName(_randomUserName);
					userIcon = _randomUserIcon;
				} else {
					// 从existUserTopic中得到randomIconId
					randomUserIconId = existUserTopic.getRandomIconID();
					randomIconId = randomUserIconId;
					// 得到随机头像 id
					int iconId = randomIconId / 100 + 1;
					// 得到随机头像的色值
					int iconColorId = randomIconId % 100 + 1;
					String iconCode = colorDao.getColorById(iconColorId);
					// 从existUserTopic中得到 randomNameId
					randomNameId = existUserTopic.getRandomNameID();
					userIcon = iconId + "_" + iconCode; // 63_E6A473
				}
				
				userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
			} 
			// 如果用户选择实名发送,并且existUserTopic.getUserIcon()中不存在字符串.png：则证明之前是匿名发送,现在是实名发送
			else if((!danmu.getBoolean("isannoymous")) && (!existUserTopic.getUserIcon().contains(".png"))) {
				// 从existUserTopic中得到randomIconId
				randomUserIconId = existUserTopic.getRandomIconID();
				randomIconId = randomUserIconId;
				// 从existUserTopic中得到 randomNameId
				randomNameId = existUserTopic.getRandomNameID();
				userIcon = userId + ".png";
				
				userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
			} 
			// 如果用户之前选择的是匿名发送,现在选择的也是匿名发送.或者用户之前选择的是实名发送,现在选择的也是实名发送
			else {
				// 从existUserTopic中得到randomIconId
				randomUserIconId = existUserTopic.getRandomIconID();
				randomIconId = randomUserIconId;
				// 从existUserTopic中得到 randomNameId
				randomNameId = existUserTopic.getRandomNameID();
				userIcon = existUserTopic.getUserIcon();
				
				userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
			}
		}
		// 如果UserTopic不存在记录,则用户是第一次在此话题底下做操作
		else{
			UserTopic userTopic = new UserTopic();
			userTopic.setUserId(userId);
			userTopic.setTopicId(topicId);
			userTopic.setFirstvisit_time(timeUtil.currentTimeStamp);
			userTopic.setLastVisit_time(timeUtil.currentTimeStamp);
			userTopic.setFrequency(0);
			if(danmu.getBoolean("isannoymous")){
				// 由于randomUserIconId之前保存过了,所以不需要进行 
				randomIconId = _randomIconId;
				randomNameId = randomNameDao.getRandomNameIdByIdRandomName(_randomUserName);
				userIcon = _randomUserIcon;
				
				userTopic.setRandomNameID(randomNameId);
				userTopic.setRandomIconID(randomIconId);
				userTopic.setUserIcon(userIcon);
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
		
//		// 进行encode
//		String encode = Base64.getUrlEncoder().encodeToString(danmu.getString("messages").getBytes());
//		newDanmu.setMessages(encode);
		
		newDanmu.setMessages(danmu.getString("messages"));
		
		if(danmu.getBoolean("isannoymous")){
//			newDanmu.setUserIcon(randomUserIconId + ".png");
			newDanmu.setUserIcon(userIcon);
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
		
//		// 进行decode
//		for (Danmu danmu : list) {
//			String decode = danmu.getMessages();
//			try {
//				decode = new String(Base64.getUrlDecoder().decode(danmu.getMessages()),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			danmu.setMessages(decode);
//		}
		
		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("danmu", array.toString());
		
		return object.toString();
	} 

	public String getDanmuDetails(int danmuId, String currentUserId) {
		
		Danmu danmu = danmuDao.getDanmu(danmuId);
		List<Comment> comments = commentDao.getCommentbyDanmuId(danmuId);
		User danmuSender = userDao.getUser(danmu.getUserId());
		
//		// 进行decode
//		String decode = danmu.getMessages();
//		try {
//			decode = new String(Base64.getUrlDecoder().decode(danmu.getMessages()),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		danmu.setMessages(decode);
		
		updateUserDanmu(currentUserId, danmuId);
		
		JSONArray jsonArray_danmu = JSONArray.fromObject(danmu);
		JSONArray jsonArray_usercomment = new JSONArray();
		JSONObject danmuDetails = jsonArray_danmu.getJSONObject(0);
		
		// 得到弹幕的昵称,根据弹幕的userIcon判断用户是匿名发送的还是实名发送的
		String nickname = null;
		if(danmu.getUserIcon().contains(".png")) {
			nickname = danmuSender.getNickname();
		} else {
			// 得到topicID
			String danmuSenderTopicId = danmu.getTopicId();
			// 根据topicID和userID得到UserTopic
			String danmuSenderUserId = danmuSender.getUserID();
			UserTopic userTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(danmuSenderUserId, danmuSenderTopicId);
			// 根据UserTopic得到randomID
			int randomNameId = userTopic.getRandomNameID();
			// 根据randomNameId 得到userName
			String userName = randomNameDao.getRandomNameById(randomNameId);
			// 将user对象中的userIcon和nickname替换掉
			nickname = userName;
		}
		
		
		danmuDetails.put("nickname", nickname);
		danmuDetails.put("gender", danmuSender.getGender());
		danmuDetails.put("action", danmuDao.getActionbyUserIdandDanmuId(currentUserId, danmuId));
		danmuDetails.put("isfav", danmuDao.checkUserFavDanmu(currentUserId, danmuId));

		for (Comment comment : comments) {
			
			User sender = userDao.getUser(comment.getSender());
			
			// 解决hibernate缓存问题,先保存原始数据
			User temp_user = new User();
			temp_user.setCellphone(sender.getCellphone());
			temp_user.setCreateTime(sender.getCreateTime());
			temp_user.setGender(sender.getGender());
			temp_user.setNickname(sender.getNickname());
			temp_user.setOpenID_qq(sender.getOpenID_qq());
			temp_user.setOpenID_wb(sender.getOpenID_wb());
			temp_user.setOpenID_wx(sender.getOpenID_wx());
			temp_user.setPassword(sender.getPassword());
			temp_user.setUserIcon(sender.getUserIcon());
			temp_user.setUserID(sender.getUserID());
			temp_user.setVcSendTime(sender.getVcSendTime());
			temp_user.setVerificationCode(sender.getVerificationCode());
			
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
			
			// 判断评论时是匿名还是实名. isanonymous等于0为匿名,isanonymous等于1为实名
			// 如果为匿名,则从UserTopic表中取出匿名头像和昵称
			if(comment.getIsanonymous() == 0) {
//				// 根据弹幕ID得到topicID
//				String topicId = danmuDao.getTopicIdbyDanmuId(comment.getDanmuId());
//				String userId = comment.getSender();
//				// 根据topicID和userID得到UserTopic
//				UserTopic existUserTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId);
//				// 根据UserTopic得到randomID
//				int randomIconId = existUserTopic.getRandomIconID();
//				int randomNameId = existUserTopic.getRandomNameID();
				
				// 得到randomID
				int randomIconId = comment.getRandomIconID();
				int randomNameId = comment.getRandomNameID();
				// 得到随机头像 id
				int iconId = randomIconId / 100 + 1;
				// 得到随机头像的色值
				int iconColorId = randomIconId % 100 + 1;
				String iconCode = colorDao.getColorById(iconColorId);
				// 根据randomID 得到userIcon和userName
				String userIcon = iconId + "_" + iconCode; // 63_E6A473
				String userName = randomNameDao.getRandomNameById(randomNameId);
				// 将user对象中的userIcon和nickname替换掉
				sender.setNickname(userName);
				sender.setUserIcon(userIcon);
			} 
			
			JSONObject jsonObject_usercomment = new JSONObject();
			sender.setCellphone(null);
			sender.setCreateTime(null);
			sender.setOpenID_qq(null);
			sender.setOpenID_wx(null);
			sender.setOpenID_wb(null);
			sender.setPassword(null);
			sender.setVcSendTime(null);
			sender.setVerificationCode(null);
			jsonObject_usercomment.put("user", sender);
			jsonObject_usercomment.put("comment", comment);
			jsonObject_usercomment.put("replycomment_name", receiver.getNickname());	//应该是receiver_name和receiver_gender
			jsonObject_usercomment.put("replycomment_gender", receiver.getGender());
			jsonArray_usercomment.add(jsonObject_usercomment);
			
			// 解决hibernate缓存问题,还原原始数据
			sender = temp_user;
		}		
		danmuDetails.put("usercomments", jsonArray_usercomment);
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
		
		// 前台传输过来的userIcon和userName和randomIconId
		String _randomUserIcon = danmuComment.getString("randomUserIcon");
		String _randomUserName = danmuComment.getString("randomUserName");
		int _randomIconId = danmuComment.getInt("randomIconId");
		
		String userId = danmuComment.getString("userid");
		int danmuId = danmuComment.getInt("danmuid");
		String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
		
		int randomIconId = 0;
		int randomNameId = 0;
		String lastVisitTime = timeUtil.currentTimeStamp;
		int frequency = userTopicDao.getLatestFrequency() + 1;
		String userIcon = userId + ".png";
		int isanonymous = 0; //0为匿名,1为实名
		
		// 通过userId和topicId从数据库中获取存在的UserTopic信息
		UserTopic existUserTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId);
		// 如果UserTopic存在记录,则用户不是第一次在此话题底下做操作
		if(existUserTopic != null){
			// 如果用户选择匿名发送,并且existUserTopic.getUserIcon()中存在字符串.png：则证明之前是实名发送,现在是匿名发送
			if(danmuComment.getBoolean("isannoymous") && existUserTopic.getUserIcon().contains(".png")) {
				// 如果用户第一次使用匿名,则将前台发送回来的userIcon和userName和randomIconId做相应处理保存下来
				if(existUserTopic.getRandomIconID() == 0) {
					// 由于randomUserIconId之前保存过了,所以不需要进行 
					randomIconId = _randomIconId;
					randomNameId = randomNameDao.getRandomNameIdByIdRandomName(_randomUserName);
					userIcon = _randomUserIcon;
				} else {
					// 从existUserTopic中得到randomIconId
					randomUserIconId = existUserTopic.getRandomIconID();
					randomIconId = randomUserIconId;
					// 得到随机头像 id
					int iconId = randomIconId / 100 + 1;
					// 得到随机头像的色值
					int iconColorId = randomIconId % 100 + 1;
					String iconCode = colorDao.getColorById(iconColorId);
					// 从existUserTopic中得到 randomNameId
					randomNameId = existUserTopic.getRandomNameID();
					userIcon = iconId + "_" + iconCode; // 63_E6A473
				}
				userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
			} 
			// 如果用户选择实名发送,并且existUserTopic.getUserIcon()中不存在字符串.png：则证明之前是匿名发送,现在是实名发送
			else if((!danmuComment.getBoolean("isannoymous")) && (!existUserTopic.getUserIcon().contains(".png"))) {
				// 从existUserTopic中得到randomIconId
				randomUserIconId = existUserTopic.getRandomIconID();
				randomIconId = randomUserIconId;
				// 从existUserTopic中得到 randomNameId
				randomNameId = existUserTopic.getRandomNameID();
				userIcon = userId + ".png";
				userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
			} 
			// 如果用户之前选择的是匿名发送,现在选择的也是匿名发送.或者用户之前选择的是实名发送,现在选择的也是实名发送
			else {
				// 从existUserTopic中得到randomIconId
				randomUserIconId = existUserTopic.getRandomIconID();
				randomIconId = randomUserIconId;
				// 从existUserTopic中得到 randomNameId
				randomNameId = existUserTopic.getRandomNameID();
				userIcon = existUserTopic.getUserIcon();
				
				userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
			}
		}
		// 如果UserTopic不存在记录,则用户是第一次在此话题底下做操作
		else{
			UserTopic userTopic = new UserTopic();
			userTopic.setUserId(userId);
			userTopic.setTopicId(topicId);
			userTopic.setFirstvisit_time(timeUtil.currentTimeStamp);
			userTopic.setLastVisit_time(timeUtil.currentTimeStamp);
			userTopic.setFrequency(0);
			if(danmuComment.getBoolean("isannoymous")){
				// 由于randomUserIconId之前保存过了,所以不需要进行 
				randomIconId = _randomIconId;
				randomNameId = randomNameDao.getRandomNameIdByIdRandomName(_randomUserName);
				userIcon = _randomUserIcon;
				
				userTopic.setRandomNameID(randomNameId);
				userTopic.setRandomIconID(randomIconId);
				userTopic.setUserIcon(userIcon);
			}
			else{
				userTopic.setRandomNameID(0);
				userTopic.setRandomIconID(0);
				userTopic.setUserIcon(userId + ".png");
			}
			userTopicDao.saveUserTopic(userTopic);
		}
		
		Comment comment = new Comment();
		// 如果是匿名isanonymous为0,如果是实名isanonymous为1
		if(danmuComment.getBoolean("isannoymous")){
			isanonymous = 0;
			comment.setRandomIconID(randomIconId);
			comment.setRandomNameID(randomNameId);
		} else {
			isanonymous = 1;
			comment.setRandomIconID(0);
			comment.setRandomNameID(0);
		}
		comment.setDanmuId(danmuId);
		comment.setSender(userId);
		comment.setReceiver(danmuComment.getString("receiver"));
		comment.setContent(danmuComment.getString("content"));
		if(danmuComment.get("reply_commentid") != null){
			comment.setReply_commentId(danmuComment.getInt("reply_commentid"));
		}
		comment.setType(danmuComment.getInt("type"));
		comment.setCreate_time(timeUtil.currentTimeStamp);
		comment.setIsanonymous(isanonymous);
		commentDao.saveComment(comment);
		
		return comment;
	}

	public boolean upDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		String upTime = timeUtil.currentTimeStamp;
		
		return danmuDao.updateUserUpDanmu(jsonObject.getBoolean("isup"), jsonObject.getString("userid"), jsonObject.getInt("danmuid"), upTime);
	}

	public boolean downDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		String downTime = timeUtil.currentTimeStamp;
		
		return danmuDao.updateUserDownDanmu(jsonObject.getBoolean("isdown"), jsonObject.getString("userid"), jsonObject.getInt("danmuid"), downTime);
	}

	public boolean favDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
		String favTime = timeUtil.currentTimeStamp;
		String userId = jsonObject.getString("userid");
		int danmuId = jsonObject.getInt("danmuid");
		
		Boolean isFav = danmuDao.checkUserFavDanmu(userId, danmuId);
		
		return danmuDao.updateUserFavDanmu(isFav, userId, danmuId, favTime);
	}

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

	public String getFavDanmuList(JSONObject jsonObject) {
		
		List<Object[]> list = danmuDao.listFavDanmu(jsonObject.getString("userid"));
		JSONArray jsonArray = JSONArray.fromObject(list);
		for (int i = 0; i < jsonArray.size(); i ++) {
			JSONObject tempJsonObject = jsonArray.getJSONObject(i);
			int danmuId = tempJsonObject.getInt("danmuID");
			Danmu danmu = danmuDao.getDanmu(danmuId);
			String messages = danmu.getMessages();
//			// 进行decode
//			try {
//				messages = new String(Base64.getUrlDecoder().decode(messages),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
			tempJsonObject.put("messages", messages);
			
			// 根据userID 得到user信息
			String userID = danmu.getUserId();
			User user = userDao.getUser(userID);
			
			// 得到弹幕的昵称,根据弹幕的userIcon判断用户是匿名发送的还是实名发送的
			String nickname = null;
			if(danmu.getUserIcon().contains(".png")) {
				nickname = user.getNickname();
			} else {
				// 得到topicID
				String topicId = danmu.getTopicId();
				// 根据topicID和userID得到UserTopic
				UserTopic userTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicId);
				// 根据UserTopic得到randomID
				int randomNameId = userTopic.getRandomNameID();
				// 根据randomNameId 得到userName
				String userName = randomNameDao.getRandomNameById(randomNameId);
				// 将user对象中的userIcon和nickname替换掉
				nickname = userName;
			}
			
			tempJsonObject.put("gender", user.getGender());
			tempJsonObject.put("nickname", nickname);
			tempJsonObject.put("userIcon", danmu.getUserIcon());
			
			// 根据danmuID得到话题名称
			String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
			String topicTitle = topicDao.getTopic(topicId).getTitle();
			tempJsonObject.put("topicTitle", topicTitle);
			
		}
		JSONObject json = new JSONObject();
		json.put("favdanmu", jsonArray);
		System.out.println(json.toString());
		return json.toString();
	}

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
