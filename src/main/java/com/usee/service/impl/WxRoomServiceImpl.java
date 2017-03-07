package com.usee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usee.dao.impl.WxRoomDaoImpl;
import com.usee.model.WxDanmu;
import com.usee.model.WxRoom;
import com.usee.service.WxRoomService;
import com.usee.utils.URL2PictureUtil;

@Service
public class WxRoomServiceImpl implements WxRoomService {

	@Autowired
	WxRoomDaoImpl wxRoomDao;
	
	@Override
	public int addWxRoom(WxRoom wxRoom, String fileRootDir) {
		// 将用户头像保存到本地图片服务器
		URL2PictureUtil.download(wxRoom.getOwnerIcon(), "wx_" + wxRoom.getOwnerId(), fileRootDir);
		wxRoom.setOwnerIcon("wx_" + wxRoom.getOwnerId() + ".png");
		return wxRoomDao.addWxRoom(wxRoom);
	}

	@Override
	public WxRoom getWxRoom(int id) {
		return wxRoomDao.getWxRoom(id);
	}

	@Override
	public int sendDanmu(WxDanmu wxDanmu) {
		return wxRoomDao.saveDanmu(wxDanmu);
	}

	@Override
	public List<WxDanmu> getDanmuByWxRoomId(int wxRoomId) {
		return wxRoomDao.getDanmuByWxRoomId(wxRoomId);
	}

	@Override
	public WxRoom getWxRoomByOwnerId(String ownerId) {
		return wxRoomDao.getWxRoomByOwnerId(ownerId);
	}

}
