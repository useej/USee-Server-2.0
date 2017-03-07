package com.usee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.dao.impl.TopicimgDaoImp;
import com.usee.dao.impl.UserDaoImpl;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Feedback;
import com.usee.model.Topic;
import com.usee.model.User;
import com.usee.model.UserTopic;
import com.usee.service.UserService;
import com.usee.utils.API;
import com.usee.utils.MD5Util;
import com.usee.utils.URL2PictureUtil;
import com.usee.utils.UUIDGeneratorUtil;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserDaoImpl userDao;
	
	@Autowired
	private UserTopicDaoImp userTopicDao;
	
	@Autowired
	private TopicDaoImpl topicDaoImpl;
	
	@Autowired
	private DanmuDaoImp danmuDaoImp;
	
	@Autowired
	private TopicimgDaoImp topicimgDaoImp;

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	public User getUser(String id) {
		return userDao.getUser(id);
	}

	
	public User getUserByOpenId(String tag, String openId) {
		return userDao.getUserByOpenId(tag, openId);
	}

	
	public User getUserByCellphone(String cellphone) {
		return userDao.getUserByCellphone(cellphone);
	}


	public void addUser(User user) {
		// 得到之前保存的user
		User addUser = userDao.getUserByCellphone(user.getCellphone());
		
		user.setUserID(addUser.getUserID());
		user.setCreateTime(new Date().getTime() + "");
		
		// 设置默认的昵称
		if(user.getNickname() == null) {
			user.setNickname(API.DEFAULT_NICKNAME);
		}
		// 设置默认的头像
		if(user.getUserIcon() == null) {
			user.setUserIcon(API.DEFAULT_USERICON);
		}
		
		// 设置默认的性别
		user.setGender(API.DEFAULT_GENDER);

		// 密码使用MD5加密
		String md5Password = MD5Util.getMD5(user.getPassword());
		user.setPassword(md5Password);
		
		userDao.addUser(user);
	}

	/*
	 * 更新用户信息
	 * @see com.usee.service.UserService#updateUser(com.usee.model.User)
	 */
	public boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	
	public boolean changePassword(User user) {
		// 密码使用MD5加密
		String md5Password = MD5Util.getMD5(user.getPassword());
		user.setPassword(md5Password);
		
		return userDao.changePassword(user);
	}

	public boolean updateUser_OAuth(User user) {
		return userDao.updateUser_OAuth(user);
	}

	public boolean updateUser_Cellphone(User user) {
		// 将密码使用MD5加密
		user.setPassword(MD5Util.getMD5(user.getPassword()));;
		return userDao.updateUser_Cellphone(user);
	}

	public boolean modifyPassword(User user) {
		// 密码使用MD5加密
		String md5Password = MD5Util.getMD5(user.getPassword());
		user.setPassword(md5Password);
		
		return userDao.modifyPassword(user);
	}

	@Override
	public void feedback(String messages) {
		String time = new Date().getTime() + "";
		Feedback feedback = new Feedback();
		feedback.setTime(time);
		feedback.setMessages(messages);
		userDao.feedback(feedback);
		
		System.out.println(feedback);
	}

	@Override
	public String getRealnameInfo(String userID) {
		JSONObject resultJson = new JSONObject();
		
		User user = userDao.getUser(userID);
		List<UserTopic> topicList = userTopicDao.getUserTopicbyUserId(userID);
		List<String> topicInfoList = new ArrayList<String>();
		for (UserTopic userTopic : topicList) {
			
			System.out.println(userTopic.toString());
			
			String topicID = userTopic.getTopicId();
			if(danmuDaoImp.hasRealnameDmByUserIdAndTopicId(userID, topicID)) {
				Topic topic = new Topic();
				topic = topicDaoImpl.getTopic(topicID);
				// 获取话题图片
	            List<String> imgurls = topicimgDaoImp.gettopicimg(topic.getId());
	            topic.setImgurls(imgurls.toArray(new String[imgurls.size()]));
	            
				JSONObject topicJSON = JSONObject.fromObject(topic);
				topicInfoList.add(topicJSON.toString());
			}
		}
		
		resultJson.put("userID", user.getUserID());
		resultJson.put("gender", user.getGender());
		resultJson.put("nickname", user.getNickname());
		resultJson.put("userIcon", user.getUserIcon());
		resultJson.put("topic", topicInfoList);
		return resultJson.toString();
	}

	@Override
	public void addUser_OAuth_UserInfo(User user, String fileRootDir) {
		user.setUserID(UUIDGeneratorUtil.getUUID());
		user.setCreateTime(new Date().getTime() + "");
		
		// 将用户头像保存到本地图片服务器
		URL2PictureUtil.download(user.getUserIcon(), user.getUserID(), fileRootDir);
		user.setUserIcon(user.getUserID() + ".png");
		// 设置默认的手机号
		user.setCellphone(API.DEFAULT_INFO);
		// 设置默认的密码
		user.setPassword(API.DEFAULT_INFO);
		user.setStatus("1");
		userDao.addUser_OAuth(user);
		return ;
	}

	@Override
	public String addUser_OAuth_Base(String openId) {
		User user = new User();
		user.setUserID(UUIDGeneratorUtil.getUUID());
		user.setCreateTime(new Date().getTime() + "");
		
		user.setOpenID_wx(openId);
		
		// 设置默认的性别
		user.setGender(API.DEFAULT_GENDER);
		// 设置默认的昵称
		user.setNickname(API.DEFAULT_NICKNAME);
		// 设置默认的头像
		user.setUserIcon(API.DEFAULT_USERICON);
		// 设置默认的手机号
		user.setCellphone(API.DEFAULT_INFO);
		// 设置默认的密码
		user.setPassword(API.DEFAULT_INFO);
		user.setStatus("1");
		userDao.addUser_OAuth(user);
		return user.getUserID();		
	}
	
	@Override
	public void updateUser_OAuth_UserInfo(User user, String fileRootDir) {
		
		// 将用户头像保存到本地图片服务器
		URL2PictureUtil.download(user.getUserIcon(), user.getUserID(), fileRootDir);
		user.setUserIcon(user.getUserID() + ".png");
		userDao.updateUser(user);
		return ;
	}

}
