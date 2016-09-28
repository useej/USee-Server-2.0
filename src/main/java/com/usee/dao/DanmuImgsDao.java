package com.usee.dao;

import java.util.List;

import com.usee.model.DanmuImgs;

public interface DanmuImgsDao {
	public void saveDanmuImgs(DanmuImgs DanmuImgs);
	
	public List<String> getDanmuImgs(int danmuID);
}
