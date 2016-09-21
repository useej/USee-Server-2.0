package com.usee.service.impl;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.qiniu.api.auth.AuthException;
import com.usee.dao.impl.DanmuDaoImp;
import com.usee.dao.impl.TopicDaoImpl;
import com.usee.dao.impl.TopicimgDaoImp;
import com.usee.dao.impl.UserTopicDaoImp;
import com.usee.model.Topicimg;
import com.usee.service.TopicImgService;
import com.usee.utils.QiniuServiceImpl;

import net.sf.json.JSONObject;

@Service
public class TopicImgServiceImp implements TopicImgService {
	@Resource
	private TopicimgDaoImp topicimgdao;
	
	public void init() {
	        
	    }
	
	public String getuptoken() throws AuthException, JSONException {
		QiniuServiceImpl qiniuService= new QiniuServiceImpl();
        //设置AccessKey
        qiniuService.setAccessKey("W3VCxhIZCNSidAA-oYBOYiFHMMlN0i4BGdrJaIB5");
        //设置SecretKey
        qiniuService.setSecretKey("HrJr_Rq5DHT7Lp1WVKVVsf6nP-UADXIcSylRvyFP");
        //设置存储空间
        qiniuService.setBucketName("usee");
        //设置七牛域名
        qiniuService.setDomain("odok0w4o2.bkt.clouddn.com");
        
		String uptoken1 = qiniuService.getUpToken();
		String uptoken = "{"+uptoken1+"}";
		System.out.println(uptoken);
		return uptoken;
	}
	public String saveTopicimg(JSONObject Topicimg) {
		
		String topicid = Topicimg.getString("topicid");
		int views = 1;
		String imgurl = Topicimg.getString("imgurl");
		Topicimg newtopicimg = new Topicimg();
		newtopicimg.setTopicid(topicid);
		newtopicimg.setViews(views);
		newtopicimg.setImgurl(imgurl);
		topicimgdao.savetopicimg(newtopicimg);
		
		return null;
	}
	

}
