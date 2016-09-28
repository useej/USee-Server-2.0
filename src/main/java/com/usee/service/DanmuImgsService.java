package com.usee.service;

import java.util.List;

import com.usee.model.DanmuImgs;

import net.sf.json.JSONObject;

public interface DanmuImgsService {
	
	public DanmuImgs saveDanmuImgs(JSONObject danmuImgs);
	
	public List<String> getDanmuImgs(int danmuID);
	
}
