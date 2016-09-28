package com.usee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.usee.dao.impl.DanmuImgsDaoImp;
import com.usee.model.DanmuImgs;
import com.usee.service.DanmuImgsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class DanmuImgsServiceImp implements DanmuImgsService {
	@Resource
	private DanmuImgsDaoImp DanmuImgsdao;

	public DanmuImgs saveDanmuImgs(JSONObject danmuImgs) {
		
		int danmuID = danmuImgs.getInt("danmuID");
		int views = 1;
		JSONArray imgurls = danmuImgs.getJSONArray("imgurls");
		
		System.out.println("------------" + imgurls);
		
		DanmuImgs newDanmuImgs = new DanmuImgs();
		newDanmuImgs.setDanmuID(danmuID);
		newDanmuImgs.setViews(views);
		String[] _imgurls = new String[imgurls.size()];
		
		for(int i=0; i<imgurls.size(); i++){
			DanmuImgs _newDanmuImgs = new DanmuImgs();
			_newDanmuImgs.setDanmuID(danmuID);
			_newDanmuImgs.setViews(views);
			String imgurl = imgurls.getString(i);
			_imgurls[i] = imgurl;
			_newDanmuImgs.setImgurl(imgurl);
			DanmuImgsdao.saveDanmuImgs(_newDanmuImgs);
		}
		
		newDanmuImgs.setImgurl(null);
		newDanmuImgs.setImgurls(_imgurls);
		return newDanmuImgs;
	}

	@Override
	public List<String> getDanmuImgs(int danmuID) {
		return DanmuImgsdao.getDanmuImgs(danmuID);
	}
	

}
