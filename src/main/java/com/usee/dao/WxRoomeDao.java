package com.usee.dao;

import java.util.List;

import com.usee.model.WxRoom;
import com.usee.model.WxDanmu;

public interface WxRoomeDao {

	public WxRoom getWxRoom(int id);

	public int addWxRoom(WxRoom wxRoom);
	
	public int saveDanmu(WxDanmu wxDanmu);
	
	public List<WxDanmu> getDanmuByWxRoomId(int wxRoomId);
	
	public WxRoom getWxRoomByOwnerId(String ownerId);
}
