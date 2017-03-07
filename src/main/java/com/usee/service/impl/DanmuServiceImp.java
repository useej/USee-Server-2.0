package com.usee.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.impl.ColorDaoImpl;
import com.usee.dao.impl.CommentDaoImpl;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.DanmuImgsDaoImp;
import com.usee.dao.impl.RandomNameDaoImpl;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.dao.impl.UserDaoImpl;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Comment;
import com.usee.model.Danmu;
import com.usee.model.User;
import com.usee.model.UserTopic;
import com.usee.service.DanmuService;
import com.usee.utils.AmapAPIUtil;
import com.usee.utils.RandomNumber;
import com.usee.utils.StatusCode;
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
	private ColorDaoImpl colorDao;
	@Autowired
	private TopicDaoImpl topicDao;
	@Autowired
	private RandomNameDaoImpl randomNameDao;
	@Autowired
	private DanmuImgsDaoImp danmuImgsDaoImp;
	
	public static final int MAX_RANDOM_NAME_NUMBER = 4000;
	public static final int MAX_RANDOM_ICON_NUMBER = 6400;

	public RandomNumber randomNumber = new RandomNumber();
	public int randomUserIconId = 0;
//	public int randomUserNameId = randomNumber.getRandom(1, MAX_RANDOM_NAME_NUMBER);
	
	public void sendDammu(JSONObject danmu) {
		TimeUtil timeUtil = new TimeUtil();
        AmapAPIUtil amap = new AmapAPIUtil();

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

        //通过经纬度获取地址
        String lon = danmu.getString("lon");
        String lat = danmu.getString("lat");
        String result = amap.getLocationName(lon + "," + lat);
        JSONObject addressJson = JSONObject.fromObject(result);
        String address = addressJson.getJSONObject("regeocode").getString("formatted_address");

		// 通过userId和topicId从数据库中获取存在的UserTopic信息
		UserTopic existUserTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId);
		// 如果UserTopic存在记录,则用户不是第一次在此话题底下做操作
		if(existUserTopic != null){
			// 如果用户选择匿名发送,并且existUserTopic.getUserIcon()中存在字符串.png：则证明之前是实名发送,现在是匿名发送
			if(danmu.getBoolean("isannoymous") && existUserTopic.getUserIcon() != null && existUserTopic.getUserIcon().contains(".png")) {
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
			// 如果用户选择实名发送,并且existUserTopic.getUserIcon()为NULL：则证明之前是匿名发送,现在是实名发送
			else if((!danmu.getBoolean("isannoymous")) && existUserTopic.getUserIcon() == null ) {
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
		newDanmu.setStatus("1");
		newDanmu.setTopicId(danmu.getString("topicid"));
		newDanmu.setLon(danmu.getString("lon"));
		newDanmu.setLat(danmu.getString("lat"));
		newDanmu.setCreate_time(timeUtil.currentTimeStamp);
		newDanmu.setAddress(address);
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
		}
		else {
			int _pageNum = Integer.parseInt(pageNum);
			int _pageSize = Integer.parseInt(pageSize);
			list = danmuDao.getDanmuList(topicId, _pageNum, _pageSize);
		}
		
		// 获取弹幕图片
		for (Danmu danmu : list) {
			List<String> imgurls = danmuImgsDaoImp.getDanmuImgs(danmu.getId());
			danmu.setImgurls(imgurls.toArray(new String[imgurls.size()]));
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
		
		// 得到弹幕图片
		List<String> imgurls = danmuImgsDaoImp.getDanmuImgs(danmuId);
		
		danmuDetails.put("imgurls", imgurls.toArray(new String[imgurls.size()]));
		danmuDetails.put("nickname", nickname);
		danmuDetails.put("gender", danmuSender.getGender());
		danmuDetails.put("action", danmuDao.getActionbyUserIdandDanmuId(currentUserId, danmuId));
		danmuDetails.put("isfav", danmuDao.checkUserFavDanmu(currentUserId, danmuId));

		for (Comment comment : comments) {
            JSONObject jsonObject_usercomment = new JSONObject();
			User sender = userDao.getUser(comment.getSender());
			
			// 解决hibernate缓存问题,不修改原始数据,修改临时对象的数据
			User temp_sender_user = new User();
			temp_sender_user.setGender(sender.getGender());
			temp_sender_user.setNickname(sender.getNickname());
			temp_sender_user.setUserIcon(sender.getUserIcon());
			temp_sender_user.setUserID(sender.getUserID());
			
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
				temp_sender_user.setNickname(userName);
				temp_sender_user.setUserIcon(userIcon);
			}
			// 判断是否为评论其他人的评论
            if(comment.getReply_commentId() != null){
                Comment reply_comment = commentDao.getComment(comment.getReply_commentId());
                if(reply_comment.getIsanonymous() == 0){
                	// 如果评论其他的人的匿名评论
                	
                	// 得到randomID
    				int randomNameId1 = reply_comment.getRandomNameID();
    				// 根据randomID 得到userName
    				String userName1 = randomNameDao.getRandomNameById(randomNameId1);
    				
                	jsonObject_usercomment.put("replycomment_name", userName1);	//应该是receiver_name和receiver_gender
                } else {
                	// 如果评论其他的人的实名评论
                	jsonObject_usercomment.put("replycomment_name", receiver.getNickname());
                }
            }		

			jsonObject_usercomment.put("user", temp_sender_user);
			jsonObject_usercomment.put("comment", comment);

			jsonArray_usercomment.add(jsonObject_usercomment);
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
			System.out.println(danmuComment.get("reply_commentid"));
			comment.setReply_commentId(danmuComment.getInt("reply_commentid"));
		}
		comment.setType(danmuComment.getInt("type"));
		comment.setCreate_time(timeUtil.currentTimeStamp);
		comment.setIsanonymous(isanonymous);
        comment.setStatus(1);
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

        JSONObject json = new JSONObject();

		List<Object[]> danmuList = danmuDao.listFavDanmu(jsonObject.getString("userid"));
		JSONArray jsonArray = JSONArray.fromObject(danmuList);

        String time = jsonObject.getString("time");

		for (int i = 0; i < jsonArray.size(); i ++) {
			JSONObject tempJsonObject = jsonArray.getJSONObject(i);
			int danmuId = tempJsonObject.getInt("danmuID");

            Danmu danmu = danmuDao.getDanmu(danmuId);

//			// 进行decode
//			try {
//				messages = new String(Base64.getUrlDecoder().decode(messages),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}

            if(danmu.getStatus().equals("0")){
                tempJsonObject.put("messages", "该弹幕因举报过多已被屏蔽。");
            }
            else {
                tempJsonObject.put("messages", danmu.getMessages());
            }


			
			// 根据userID 得到user信息
			User user = userDao.getUser(danmu.getUserId());
			
			// 得到弹幕的昵称,根据弹幕的userIcon判断用户是匿名发送的还是实名发送的
			String nickname = null;
			if(danmu.getUserIcon() != null && danmu.getUserIcon().contains(".png")) {
				nickname = user.getNickname();
			} else {
				// 根据topicID和userID得到UserTopic
				UserTopic userTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(danmu.getUserId(), danmu.getTopicId());
				// 根据UserTopic得到randomID
				int randomNameId = userTopic.getRandomNameID();
				// 根据randomNameId 得到userName
				// 将user对象中的userIcon和nickname替换掉
				nickname = randomNameDao.getRandomNameById(randomNameId);
			}
			
			tempJsonObject.put("gender", user.getGender());
			tempJsonObject.put("nickname", nickname);
			tempJsonObject.put("userIcon", danmu.getUserIcon());
			
			// 根据danmuID得到话题名称
			String topicId = danmuDao.getTopicIdbyDanmuId(danmuId);
			String topicTitle = topicDao.getTopic(topicId).getTitle();
			tempJsonObject.put("topicTitle", topicTitle);

            List<Comment> commentList = commentDao.getCommentbyDanmuIdandCreatetime(danmuId, time);
            if(commentList.size() != 0){
                tempJsonObject.put("havecomment", true);
            }
            else {
                tempJsonObject.put("havecomment", false);
            }
		}
		json.put("favdanmu", jsonArray);
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

//    private void updateUserTopic(String userId, String topicId, JSONObject jsonObject) {
//        TimeUtil timeUtil = new TimeUtil();
//
//        int randomIconId = 0;
//        int randomNameId = 0;
//        String lastVisitTime = timeUtil.currentTimeStamp;
//        int frequency = userTopicDao.getLatestFrequency() + 1;
//        String userIcon = userId + ".png";
//
//        if(userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId) != null){
//            if(jsonObject.getBoolean("isannoymous")){
//                randomIconId = randomUserIconId;
//                randomNameId = randomUserNameId;
//                userIcon = randomIconId + ".png";
//            }
//            userTopicDao.updateUserTopic(userId, topicId, randomIconId, randomNameId, lastVisitTime, frequency, userIcon);
//        }
//        else{
//            UserTopic userTopic = new UserTopic();
//            userTopic.setUserId(userId);
//            userTopic.setTopicId(topicId);
//            userTopic.setFirstvisit_time(timeUtil.currentTimeStamp);
//            userTopic.setLastVisit_time(timeUtil.currentTimeStamp);
//            userTopic.setFrequency(0);
//            if(jsonObject.getBoolean("isannoymous")){
//                userTopic.setRandomNameID(randomUserNameId);
//                userTopic.setRandomIconID(randomUserIconId);
//            }
//            else{
//                userTopic.setRandomNameID(randomIconId);
//                userTopic.setRandomIconID(randomNameId);
//                userTopic.setUserIcon(userIcon);
//            }
//            userTopicDao.saveUserTopic(userTopic);
//        }
//    }

    public String getLatestDanmuList(JSONObject jsonObject){
        String topicId = jsonObject.getString("topicid");
        String startTime = jsonObject.getString("starttime");
        String T = jsonObject.getString("T");

        int endTime = Integer.parseInt(startTime) + Integer.parseInt(T);

        List<Danmu> list = new ArrayList<Danmu>();
        list = danmuDao.getLatestDanmuList(topicId, startTime, endTime+"");
        
		// 获取弹幕图片
		for (Danmu danmu : list) {
			List<String> imgurls = danmuImgsDaoImp.getDanmuImgs(danmu.getId());
			danmu.setImgurls(imgurls.toArray(new String[imgurls.size()]));
		}
        
        JSONArray array = JSONArray.fromObject(list);
        JSONObject object = new JSONObject();
        object.put("danmu", array.toString());

        return object.toString();
    }

    @Override
    public String haveNewComments(JSONObject jsonObject) {
        int danmuId = jsonObject.getInt("danmuid");
        String time = jsonObject.getString("time");
        List<Comment> list = commentDao.getCommentbyDanmuIdandCreatetime(danmuId, time);

        JSONObject object = new JSONObject();

        if(list.size() != 0){
            object.put("havecomment", true);
        }
        else {
            object.put("havecomment", false);
        }
        return object.toString();
    }

    @Override
    public String reportContent(JSONObject jsonObject) {
        TimeUtil timeUtil = new TimeUtil();

        String reporter = jsonObject.getString("userid");
        String contentUserId = jsonObject.getString("contentuserid");
        String contentId = jsonObject.getString("contentid");
        String contentType = jsonObject.getString("contenttype");
        int reportType = jsonObject.getInt("reporttype");
        String reportTime = timeUtil.currentTimeStamp;
        
        int result = danmuDao.updateReport(reporter, contentUserId, contentId, contentType, reportTime, reportType);

        JSONObject resultJson = new JSONObject();

        resultJson.put("result", result);

        return resultJson.toString();
    }

	@Override
	public boolean deleteComment(JSONObject jsonObject) {
		String sender = jsonObject.getString("userID");
		int commentID = jsonObject.getInt("commentID");
		// 根据评论ID和sender得到唯一的评论
		Comment comment = commentDao.getCommentByIdAndSender(commentID, sender);
		if(comment == null){
			return false;
		}
		
		// 先删除回复此条评论的评论
		deleteReplyComment(commentID);
		
		return commentDao.deleteComment(commentID);
	}
	
	// 删除回复评论的评论
	public void deleteReplyComment(int commentID){
		List<Comment> replyCommentList = new ArrayList<Comment>();
		replyCommentList = commentDao.getReplyCommentListbyReplycommentId(commentID);
		for (Comment reply_comment : replyCommentList) {
			if((commentDao.getReplyCommentListbyReplycommentId(reply_comment.getId())).size() > 0){
				deleteReplyComment(reply_comment.getId());
			}
			commentDao.deleteComment(reply_comment.getId());
		}
	}
	

	@Override
	public boolean deleteDanmu(JSONObject jsonObject) {
		String userID = jsonObject.getString("userID");
		int danmuID = jsonObject.getInt("danmuID");
		// 根据评论ID和sender得到唯一的评论
		Danmu danmu = danmuDao.getDanmuByDanmuIdAndUserId(danmuID, userID);
		if(danmu == null){
			return false;
		}
		
		// 先删除将此条弹幕作为外键的表中的列
		// comment表、updowndanmu表、user_danmu表、userfavdanmu表
		// 删除comment表
		List<Comment> CommentList = new ArrayList<Comment>();
		CommentList = commentDao.getCommentbyDanmuId(danmuID);
		for (Comment comment : CommentList) {
			// 先删除回复此条评论的评论
			deleteReplyComment(comment.getId());
			
			commentDao.deleteComment(comment.getId());
		}
		
		// 删除updowndanmu表
		danmuDao.deleteUpdownDanmu(danmuID);
		
		// 删除user_danmu表
		danmuDao.deleteUser_Danmu(danmuID);
		
		// 删除userfavdanmu表
		danmuDao.deleteUserFavDanmu(danmuID);
		
		return danmuDao.deleteDanmu(danmuID);
	}

	@Override
	public List<Map<String, String>> getIntervalDanmu(String topicID, String startTime, String endTime) {
		TimeUtil timeUtil = new TimeUtil();
		String _startTime = null;
		String _endTime = null;
		try {
			_startTime = timeUtil.date2Timestamp(startTime);
			_endTime = timeUtil.date2Timestamp(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Danmu> danmuList = danmuDao.getDanmuListByInterval(topicID, _startTime, _endTime);
		List<Map<String, String>> intervalDanmuList = new ArrayList<Map<String,String>>();
		for (Danmu danmu : danmuList) {
			Map<String, String> map = new HashMap<String, String>();
			if(danmu.getUserIcon().contains(".png")){
				// 证明此条弹幕为实名发送
				User user = userDao.getUser(danmu.getUserId());
				map.put("nickName", user.getNickname());
				map.put("message", danmu.getMessages());
			} else {
				// 证明此条弹幕为匿名发送
				UserTopic userTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(danmu.getUserId(), topicID);
				String nickName = randomNameDao.getRandomNameById(userTopic.getRandomNameID());
				map.put("nickName", nickName);
				map.put("message", danmu.getMessages());
			}
			intervalDanmuList.add(map);
			
		}
		
		return intervalDanmuList;
	}
	
	public String getNewDanmu(JSONObject jsonObject) {
		TimeUtil timeUtil = new TimeUtil();
        String time  = jsonObject.getString("createTime");
        time = time + " 8:00:00";
        String createTime = null;
		try {
			createTime = timeUtil.date2Timestamp(time) + "000";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println(createTime);
        
        List<Object[]> list = danmuDao.getNewDanmu(createTime);
        
        
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (Object[] string : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", (String) string[0]);
			map.put("danmu", (String) string[1]);
			listMap.add(map);
		}
        
        JSONObject resultJson = new JSONObject();

        resultJson.put("result", listMap);

        return resultJson.toString();
	}

	@Override
	public int checkDanmu(JSONObject danmu) {
		String userID = danmu.getString("userid");
		String topicID = danmu.getString("topicid");
		String messages = danmu.getString("messages");
		
		List<Danmu> danmuList = danmuDao.getDanmuByTopicIdAndUserId(userID, topicID);
		int num = 0;
		for (Danmu danmu2 : danmuList) {
			if(danmu2.getMessages().equals(messages)){
				num ++;
			} else {
				continue;
			}
		}
		
		if(num >= 3) {
			return StatusCode._405;
		} else {
			return StatusCode._200;
		}
	}

	@Override
	public int checkComment(JSONObject danmu) {
		
		return StatusCode._200;
	}

	@Override
	public List<Danmu> getHotDanmu(String topicID) {
		return danmuDao.getHotDanmu(topicID);
	}
}
