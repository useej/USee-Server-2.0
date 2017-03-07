package com.usee.service;

import java.util.List;

import com.usee.model.WxRoom;
import com.usee.model.WxDanmu;

public interface WxRoomService {

	public int addWxRoom(WxRoom wxRoom, String fileRootDir);
	
	public WxRoom getWxRoom(int id);
	
	public int sendDanmu(WxDanmu wxRoomDanmu);
	
	public List<WxDanmu> getDanmuByWxRoomId(int wxRoomId);
	
	public WxRoom getWxRoomByOwnerId(String ownerId);
}
