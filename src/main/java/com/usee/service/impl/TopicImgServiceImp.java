package com.usee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.qiniu.api.auth.AuthException;
import com.usee.dao.impl.TopicimgDaoImp;
import com.usee.model.Topicimg;
import com.usee.service.TopicImgService;
import com.usee.utils.QiniuServiceImpl;

import net.sf.json.JSONArray;
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
        qiniuService.setAccessKey("PbiwSKKCFf_7WKHv3DuMItdEkTp2jRpzozvA9_U5");
        //设置SecretKey
        qiniuService.setSecretKey("suH9RPao25xBFgSa6tS5y7UJ_TtMHQ65jkdFfxUB");
        //设置存储空间
        qiniuService.setBucketName("usee");
        //设置七牛域名
        qiniuService.setDomain("http://odyutrywf.qnssl.com");
        
		String uptoken = qiniuService.getUpToken();
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("uptoken", uptoken);
		resultJson.put("domain", "http://odyutrywf.qnssl.com");
		return resultJson.toString();
	}
	public Topicimg saveTopicimg(JSONObject topicimg) {
		
		String topicid = topicimg.getString("topicID");
		int views = 1;
		JSONArray imgurls = topicimg.getJSONArray("imgurls");
		
		Topicimg newtopicimg = new Topicimg();
		newtopicimg.setTopicid(topicid);
		newtopicimg.setViews(views);
		String[] _imgurls = new String[imgurls.size()];
		
		for(int i=0; i<imgurls.size(); i++){
			Topicimg _newtopicimg = new Topicimg();
			_newtopicimg.setTopicid(topicid);
			_newtopicimg.setViews(views);
			String imgurl = imgurls.getString(i);
			_imgurls[i] = imgurl;
			_newtopicimg.setImgurl(imgurl);
			topicimgdao.savetopicimg(_newtopicimg);
		}
		
		newtopicimg.setImgurl(null);
		newtopicimg.setImgurls(_imgurls);
		return newtopicimg;
	}

	@Override
	public List<String> getTopicimg(String topicID) {
		return topicimgdao.gettopicimg(topicID);
	}

	@Override
	public String getFirstTopicimg(String topicID) {
		return topicimgdao.getFirsttopicimg(topicID);
	}
	
	

}
