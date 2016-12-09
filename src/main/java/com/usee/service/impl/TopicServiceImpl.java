package com.usee.service.impl;

import com.usee.dao.ColorDao;
import com.usee.dao.RandomNameDao;
import com.usee.dao.impl.CommentDaoImpl;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.dao.impl.TopicTypeDaoImp;
import com.usee.dao.impl.TopicimgDaoImp;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Comment;
import com.usee.model.Danmu;
import com.usee.model.Topic;
import com.usee.model.TopicType;
import com.usee.model.UserTopic;
import com.usee.model.UserTopic_Visit;
import com.usee.service.TopicService;
import com.usee.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

	@Resource
	private TopicDaoImpl topicdao;
	@Resource
	private TopicTypeDaoImp topictypedao;
	@Resource
	private DanmuDaoImp danmudao;
	@Resource
	private UserTopicDaoImp userTopicDao;
	@Autowired
	private RandomNameDao randomNameDao;
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private CommentDaoImpl commentDao;
	@Autowired
	private TopicimgDaoImp topicimgDaoImp;

	private static final String DEFAULT_USERICON = "0.png";

	public static final int MAX_RANDOM_NAME_NUMBER = 590;
	public static final int MAX_RANDOM_ICON_NUMBER = 6400;
    public static final int HOT_WORDS_NUM = 10;
    public static final int HOT_TOPIC_TITLE_NUM = 20;

	public RandomNumber randomNumber = new RandomNumber();
	public int randomUserIconId = 0;

	public Topic getTopic(String id) {
		Topic topic = new Topic();
		topic = topicdao.getTopic(id);
		// 获取话题图片
        List<String> imgurls = topicimgDaoImp.gettopicimg(topic.getId());
        topic.setImgurls(imgurls.toArray(new String[imgurls.size()]));
		return topic;
	}


	public List<Topic> getAllTopic() {
		return topicdao.getAllTopic();
	}


	public void addTopic(Topic topic) {
		topicdao.addTopic(topic);
	}


	public boolean delTopic(String id) {
		return topicdao.delTopic(id);
	}


    public String getUserTopics(String userID) {
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List<String> userTopics = new ArrayList();
        list1 = topicdao.getUserTopicsID(userID);
        list2 = danmudao.getDanmubyUserId(userID);
		/*
		检查重复
		 */
        for (int i = 0; i < list2.size() - 1; i++)
        {
            for (int j = i + 1; j < list2.size(); j++)
            {
                if (list2.get(i).equals(list2.get(j)))
                {
                    list2.remove(j);
                    j--;
                }
            }
        }
        list1.addAll(list2);

        for(int i=0; i<list1.size();i++){
            String a = (String) list1.get(i);

            List<Topic> list = topicdao.getUserTopics(a);
            for (Topic topic : list) {
	            // 获取话题图片
	            List<String> imgurls = topicimgDaoImp.gettopicimg(topic.getId());
	            topic.setImgurls(imgurls.toArray(new String[imgurls.size()]));

				JSONObject topicJSON = JSONObject.fromObject(topic);
				userTopics.add(topicJSON.toString());
			}
        }

        JSONArray array = JSONArray.fromObject(userTopics);
        JSONArray array1 = JSONArray.fromObject(userTopics);
        int a1 = 0;
        int number = array.size();
        for(int i=0;i<number;i++){
            JSONObject tempJsonObject = array.getJSONObject(i);
            String topicId=tempJsonObject.getString("id");
            UserTopic usertopic =  userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicId);

            if(usertopic!=null){
                int like = usertopic.getDislike();
                if(like == 1){
                    array1.discard(i-a1);
                    a1=a1+1;
                }
            }
        }
        JSONObject object = new JSONObject();
        object.put("topic", array1.toString());


        return object.toString();
    }


    public String getNearbyTopics(double ux,double uy,int userRadius, String userID) {
        Distance a= new Distance();
        List list = new ArrayList();
        List<Topic> list1 = new ArrayList();
        list1 = topicdao.getAllTopic();

        for (Topic topic : list1) {
            // 获取话题图片
            List<String> imgurls = topicimgDaoImp.gettopicimg(topic.getId());
            topic.setImgurls(imgurls.toArray(new String[imgurls.size()]));
		}

        JSONArray array = JSONArray.fromObject(list1);
        JSONArray array1 = JSONArray.fromObject(list1);
        JSONArray array2 = JSONArray.fromObject(list1);
        JSONObject object = new JSONObject();
        int a2 = 0;
        int a4 = 0;

        for(int i=0;i<list1.size();i++){
            JSONObject tempJsonObject = array.getJSONObject(i);
            double a1;
            double a3;
            double topiclon = (Double) tempJsonObject.get("lon");
            double topiclat = (Double) tempJsonObject.get("lat");
            a1=topiclon-ux;
            a3=topiclat-uy;
            if((a1<-0.1||a1>0.1) && (a1<-0.1||a1>0.1)){
                array1.discard(i-a2);
                array2.discard(i-a2);
                a2=a2+1;
            }
        }
        int number = array1.size();
        for(int i=0;i<number;i++){
            JSONObject tempJsonObject = array1.getJSONObject(i);
            String topicId=tempJsonObject.getString("id");
            UserTopic usertopic =  userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicId);
            if(usertopic!=null){
                int like = usertopic.getDislike();
                if(like == 1){
                    array2.discard(i-a4);
                    a4=a4+1;
                }
            }
        }

        for(int i=0;i<array2.size();i++){
            JSONObject tempJsonObject = array2.getJSONObject(i);
            String topicId=tempJsonObject.getString("id");
            UserTopic usertopic =  userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicId);
            if(usertopic!= null){
        		tempJsonObject.put("isread", true);
            }
            else{
            	if(userTopicDao.getUserTopic_VisitByUserIdandTopicId(userID, topicId) != null) {
            		tempJsonObject.put("isread", true);
            	} else {
            		tempJsonObject.put("isread", false);
            	}
            }

        }


        object.put("topic", array2.toString());

        return object.toString();


    }

	public JSONObject getUserIconbyTopic(String userId, String topicId) {
        int randomUserNameId = randomNumber.getRandom(1, MAX_RANDOM_NAME_NUMBER);

		JSONObject jsonObject= new JSONObject();
		// 通过userId和topicId从数据库中获取存在的UserTopic信息
		UserTopic existUserTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId);
		// 如果用户不是第一次在此话题底下进行操作,并且用户之前匿名操作过
		if(existUserTopic != null && existUserTopic.getRandomNameID() != 0){

			int randomIconId = existUserTopic.getRandomIconID();
			// 得到随机头像 id
			int iconId = randomIconId / 100 + 1;
			// 得到随机头像的色值
			int iconColorId = randomIconId % 100 + 1;
			String iconCode = colorDao.getColorById(iconColorId);
			// 使用iconId和iconCode拼凑成一个userIcon
			String userIcon = iconId + "_" + iconCode; // 63_E6A473

			int randomNameID = existUserTopic.getRandomNameID();
			String username = randomNameDao.getRandomNameById(randomNameID);

			// 得到用户最近一次在此话题底下发的弹幕
			int latestDanmuId = danmudao.getLatestDanmuIdByUserIdAndTopicId(userId, topicId);
			// isAnonymous: 0为匿名,1为实名,2为不确定
			if(latestDanmuId == -1) {
				jsonObject.put("isAnonymous", 2);
			} else {
				Danmu latestDanmu = danmudao.getDanmu(latestDanmuId);
				if(latestDanmu.getUserIcon() != null && latestDanmu.getUserIcon().contains(".png")) {
					jsonObject.put("isAnonymous", 1);
				} else {
					jsonObject.put("isAnonymous", 0);
				}
			}

			jsonObject.put("randomIconId", randomIconId);
			jsonObject.put("iconname", userIcon);
			jsonObject.put("username", username);
			return jsonObject;

		}
		else {

			// get list of existing userIcons search in user_topic table
			List<Integer> existingList = userTopicDao.getuserRandomIconIdsbyTopic(topicId);
			// 得到随机数
			randomUserIconId = RandomNumber.getRandomNum(existingList,MAX_RANDOM_ICON_NUMBER);
			int randomIconId = randomUserIconId;
			// 得到随机头像 id
			int iconId = randomIconId / 100 + 1;
			// 得到随机头像的色值
			int iconColorId = randomIconId % 100 + 1;
			String iconCode = colorDao.getColorById(iconColorId);
			// 使用iconId和iconCode拼凑成一个userIcon
			String userIcon = iconId + "_" + iconCode; // 63_E6A473

			int randomNameId = randomUserNameId;
			String username = randomNameDao.getRandomNameById(randomNameId);

			// 得到用户最近一次在此话题底下发的弹幕
			int latestDanmuId = danmudao.getLatestDanmuIdByUserIdAndTopicId(userId, topicId);

			System.out.println("latestDanmuId = " + latestDanmuId);

			// isAnonymous: 0为匿名,1为实名,2为不确定
			if(latestDanmuId == -1) {
				jsonObject.put("isAnonymous", 2);
			} else {
				Danmu latestDanmu = danmudao.getDanmu(latestDanmuId);
				if(latestDanmu.getUserIcon().contains(".png")) {
					jsonObject.put("isAnonymous", 1);
				} else {
					jsonObject.put("isAnonymous", 0);
				}
			}

			jsonObject.put("randomIconId", randomIconId);
			jsonObject.put("iconname", userIcon);
			jsonObject.put("username", username);
			return jsonObject;
		}
	}


	public JSONObject getUserIconByComment(String userId, int danmuId) {
        int randomUserNameId = randomNumber.getRandom(1, MAX_RANDOM_NAME_NUMBER);

		JSONObject jsonObject= new JSONObject();
		// 根据 danmuId 得到 topicId
		String topicId = danmudao.getTopicIdbyDanmuId(danmuId);
		// 通过userId和topicId从数据库中获取存在的UserTopic信息
		UserTopic existUserTopic = userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userId, topicId);
		if(existUserTopic != null && existUserTopic.getRandomNameID() != 0){
			// 如果用户不是第一次在此话题底下进行操作,并且用户之前匿名操作过
			int randomIconId = existUserTopic.getRandomIconID();
			// 得到随机头像 id
			int iconId = randomIconId / 100 + 1;
			// 得到随机头像的色值
			int iconColorId = randomIconId % 100 + 1;
			String iconCode = colorDao.getColorById(iconColorId);
			String userIcon = iconId + "_" + iconCode; // 63_E6A473

			int randomNameID = existUserTopic.getRandomNameID();
			String username = randomNameDao.getRandomNameById(randomNameID);

			// 得到用户最近一次在此话题底下发的评论
			int latestCommentId = commentDao.getLatestCommentIdByUserIdAndDanmuId(userId, danmuId);

			if(latestCommentId == -1) {
				jsonObject.put("isAnonymous", 2);
			} else {
				Comment latestComment = commentDao.getComment(latestCommentId);
				if(latestComment.getIsanonymous() == 1) {
					jsonObject.put("isAnonymous", 1);
				} else {
					jsonObject.put("isAnonymous", 0);
				}
			}

			jsonObject.put("randomIconId", randomIconId);
			jsonObject.put("iconname", userIcon);
			jsonObject.put("username", username);
			return jsonObject;

		}
		else {

			// get list of existing userIcons search in user_topic table
			List<Integer> existingList = userTopicDao.getuserRandomIconIdsbyTopic(topicId);
			// 得到随机数
			randomUserIconId = RandomNumber.getRandomNum(existingList,MAX_RANDOM_ICON_NUMBER);
			int randomIconId = randomUserIconId;
			// 得到随机头像 id
			int iconId = randomIconId / 100 + 1;
			// 得到随机头像的色值
			int iconColorId = randomIconId % 100 + 1;
			String iconCode = colorDao.getColorById(iconColorId);
			String userIcon = iconId + "_" + iconCode; // 63_E6A473

			int randomNameId = randomUserNameId;
			String username = randomNameDao.getRandomNameById(randomNameId);

			// 得到用户最近一次在此话题底下发的评论
			int latestCommentId = commentDao.getLatestCommentIdByUserIdAndDanmuId(userId, danmuId);

			if(latestCommentId == -1) {
				jsonObject.put("isAnonymous", 2);
			} else {
				Comment latestComment = commentDao.getComment(latestCommentId);
				if(latestComment.getIsanonymous() == 1) {
					jsonObject.put("isAnonymous", 1);
				} else {
					jsonObject.put("isAnonymous", 0);
				}
			}

			jsonObject.put("randomIconId", randomIconId);
			jsonObject.put("iconname", userIcon);
			jsonObject.put("username", username);
			return jsonObject;
		}
	}

	public void updateUser_topic(String userID, String topicID) {
		TimeUtil timeutil = new TimeUtil();
		String lastVisitTime = timeutil.currentTimeStamp;
		if(userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicID) == null) {
			UserTopic_Visit userTopic_Visit = userTopicDao.getUserTopic_VisitByUserIdandTopicId(userID, topicID);
			if(userTopic_Visit == null) {
				UserTopic_Visit save_userTopic_Visit = new UserTopic_Visit();
				save_userTopic_Visit.setUserID(userID);
				save_userTopic_Visit.setTopicID(topicID);
				save_userTopic_Visit.setFirstvisit_time(lastVisitTime);
				save_userTopic_Visit.setLastVisit_time(lastVisitTime);
				userTopicDao.saveUserTopic_visit(save_userTopic_Visit);
			} else {
				userTopicDao.updateUserTopic_visit(userID, topicID, lastVisitTime);
			}
		} else {
			int frequency = userTopicDao.getLatestFrequency() + 1;
			userTopicDao.updateUserTopicLVTandFrequency(userID,topicID,lastVisitTime,frequency);
		}
	}


	public String createTopic(JSONObject topic){
		TimeUtil timeutil = new TimeUtil();
		String currentTime = timeutil.currentTimeStamp;
		String title = topic.getString("title");
		String description = topic.getString("description");
		int radius = topic.getInt("radius");
		Double lon = topic.getDouble("lon");
		Double lat = topic.getDouble("lat");
		String userid = topic.getString("userid");

		List list = new ArrayList();
		list=topicdao.getAllTopicId();
		int max=0;
		for(int i=0;i<list.size();i++){
		int	a = Integer.valueOf((String) list.get(i));
		if(a>max){
			max=a;
			}
		}
		max++;
		String newid = String.valueOf(max);
		Topic newtopic = new Topic();
		newtopic.setId(newid);
		newtopic.setTitle(title);
		newtopic.setDescription(description);
		newtopic.setRadius(radius);
		newtopic.setLon(lon);
		newtopic.setLat(lat);
		newtopic.setUserID(userid);
		newtopic.setPoi(null);
		newtopic.setCreate_time(currentTime);


		//增加内容分类
		//获取包含的话题类型,如果传入参数不含type，则为type赋默认值0，否则正常赋值并写入到topic_type表中
//		JSONArray typeIDs;
		if(topic.has("type")){
			String[] typeID = topic.getString("type").split(",");
			String type = Integer.toString(typeID.length);
//			int type = typeIDs.size();
			
//			String type = "";
//			String topicID = topicdao.addTopic(newtopic);
			newtopic.setType(type);
			topicdao.addTopic(newtopic);
			
//			String typeID[] =type.split(","); //获取typeID值
			for(int i=0; i<typeID.length; i++){
				TopicType newtopictype = new TopicType();
				newtopictype.setTopicid(newid);
				newtopictype.setTypeid(Integer.parseInt(typeID[i]));
				topictypedao.addTopictype(newtopictype);
			}
			

		}
		else{
			newtopic.setType("0");
			topicdao.addTopic(newtopic);
		}

		return newid;
	}


	public String searchTopic(String keyword) {
		List list1 = new ArrayList();
		List<String> userTopics = new ArrayList();
		list1 = topicdao.searchTopic(keyword);

		 JSONArray array = JSONArray.fromObject(list1);
		 JSONObject object = new JSONObject();
			object.put("topic", array.toString());
			return object.toString();
	}

    @Override
    public String getHotestTopics() {
        //PullwordApi pa = new PullwordApi();
        AnsjSegUtil asu = new AnsjSegUtil();

        List<String> list = topicdao.getTopicsbyDanmuNum(HOT_TOPIC_TITLE_NUM);

        String titles = "";
        for (String s:list) {
            titles += s + ",";
        }
        System.out.println(titles);
        //return pa.getHotwords(titles, threshold);
        return asu.getPhraseSpilted(titles, HOT_WORDS_NUM);
    }

    @Override
    public void dislikeTopic(String userID, List<String> topics) {
        TimeUtil timeutil = new TimeUtil();
        String currentTime = timeutil.currentTimeStamp;
        for(String topicId :topics){
            UserTopic usertopic =  userTopicDao.getUniqueUserTopicbyUserIdandTopicId(userID, topicId);
            if(usertopic!= null){
                userTopicDao.updateUserTopiclike(userID, topicId, 1);
            }
            else{
                UserTopic newuserTopic = new UserTopic();
                newuserTopic.setTopicId(topicId);
                newuserTopic.setUserId(userID);
                newuserTopic.setFirstvisit_time(currentTime);
                newuserTopic.setRandomNameID(0);
                newuserTopic.setRandomIconID(0);
                newuserTopic.setLastVisit_time(currentTime);
                newuserTopic.setFrequency(1);
                newuserTopic.setDislike(1);
                newuserTopic.setUserIcon(DEFAULT_USERICON);
                userTopicDao.saveUserTopic(newuserTopic);
            }
        }
    }

    public String checkVersion() {
        CheckVersion checkVersion = new CheckVersion();
        return checkVersion.getNewVersion();
    }

    public void likeTopic(String userID, List<String> topics) {
        for(int i=0;i<topics.size();i++){
            String topicId=topics.get(i);
            userTopicDao.updateUserTopiclike(userID, topicId, 0);
        }
    }


	public String getTopicTitleForWeb(String topicID) {
		return topicdao.getTopicTitleForWeb(topicID);
	}

	public String getTopicsbyType(String typeID){
		List<Topic> list = new ArrayList<Topic>();
//		typeID = "%"+typeID+"%";
		list = topicdao.getTopicsbyType(typeID);

		for (Topic topic : list) {
						// 获取话题图片
						List<String> imgurls = topicimgDaoImp.gettopicimg(topic.getId());
						topic.setImgurls(imgurls.toArray(new String[imgurls.size()]));
						changeTypeOfTopic(topic);
		}


		JSONArray array = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("topic", array.toString());

		return object.toString();
	}
	
	public void delTypeOfTopic(String typeID){
		topictypedao.delTypeOfTopic(typeID);
	}
	
	public void addTopicType(String topicID, String typeID){
		TopicType newtopictype = new TopicType();
		newtopictype.setTopicid(topicID);
		newtopictype.setTypeid(Integer.parseInt(typeID));
		topictypedao.addTopictype(newtopictype);
//		topictypedao.addTypeOfTopic(topicID, typeID);
	}


	@Override
	public String changeTypeOfTopic(Topic topic) {
		// TODO Auto-generated method stub
		JSONObject jsonObject= new JSONObject().fromObject(topic);
		String topicID = jsonObject.getString("id");
		String type = topictypedao.getTypeOfTopic(topicID);
		type = type.replace("[", "");
		type = type.replace("]", "");
//		type = """+type+""";
//		jsonObject.
		jsonObject.put("type", type);
		topic.setType(type);
		String topicStr = jsonObject.toString();
		return topicStr;
//		jsonObject.put("type", type);
	}



}
