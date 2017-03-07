package com.usee.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usee.model.User;
import com.usee.model.WxDanmu;
import com.usee.model.WxRoom;
import com.usee.service.impl.UserServiceImpl;
import com.usee.service.impl.WxRoomServiceImpl;
import com.usee.service.impl.WxServiceImpl;
import com.usee.utils.API;
import com.usee.utils.TimeUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/wx")
public class WxController {
	@Autowired
	private WxServiceImpl wxService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private WxRoomServiceImpl wxRoomService;
	
	
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ResponseBody
	public String checkSignature(@RequestParam String signature, 
								 @RequestParam String timestamp, 
								 @RequestParam String nonce, 
								 @RequestParam String echostr) {
		 String token= "USee4321" ;
         String tmpStr= getSHA1(token, timestamp, nonce);
         if (tmpStr.equals(signature)) {
             return echostr;
         } else {
             return null;
         }
	}
	
	
	@RequestMapping(value = "/login/base", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> login_base(@RequestParam String code, @RequestParam String state) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		Map<String, String> dataMap = wxService.getToken(code);
		String openId = dataMap.get("openid");
		
		// 微信登录
		User validateUser = userService.getUserByOpenId("openID_wx", openId);
		String userId = "";
		// 假如数据库中没有对应的用户信息,则证明用户第一次登录
		if(validateUser == null) {
			userId = userService.addUser_OAuth_Base(openId);
		} else {
			userId = validateUser.getUserID();
		}
		resultMap.put("userId", userId);
		return resultMap;
	}
	
	/*
	 * 用户登录并且创建用户专属的 wxRoom
	 * 返回创建好的 wxRoomId
	 */
	@RequestMapping(value = "/login/userinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Integer> login_userInfo(@RequestParam String code, @RequestParam String state, HttpServletRequest request) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		
		// 获取用户的 access_token 和 openid
		Map<String, String> token_data = wxService.getToken(code);
		
		User validateUser = userService.getUserByOpenId("openID_wx", token_data.get("openid"));
		String old_userIcon = validateUser.getUserIcon();
		// 得到用户信息
		User user = wxService.getUserInfo(token_data.get("access_token"), token_data.get("openid"));
		validateUser.setUserIcon(user.getUserIcon());
		validateUser.setNickname(user.getNickname());
		validateUser.setGender(user.getGender());
		
		// 假如 userIon 为默认的,则证明 USee 没有获取过用户的信息,更新用户信息
		if(old_userIcon.equals(API.DEFAULT_USERICON)) {
			userService.updateUser_OAuth_UserInfo(validateUser, request.getSession().getServletContext().getRealPath("/"));
		}
		validateUser.setUserIcon(validateUser.getUserID() + ".png");
		
		System.out.println("User--------> " + validateUser);
		
		// 为用户创建一个 wxRoom
		WxRoom wxRoom = wxRoomService.getWxRoomByOwnerId(validateUser.getUserID());
		int wxRoomId = 0;
		if(wxRoom == null) {
			wxRoom = new WxRoom();
			wxRoom.setOwnerId(validateUser.getUserID());
			wxRoom.setOwnerName(validateUser.getNickname());
			wxRoom.setCreate_time(new TimeUtil().currentTimeStamp);
			wxRoom.setOwnerIcon(user.getUserIcon());
			wxRoomId = wxRoomService.addWxRoom(wxRoom, request.getSession().getServletContext().getRealPath("/"));
		} else {
			wxRoomId = wxRoom.getId();
		}
		
		
		resultMap.put("wxRoomId", wxRoomId);
		return resultMap;
	}
	
	@RequestMapping(value = "/wxroom/getwxroom", method = RequestMethod.POST)
	@ResponseBody
	public WxRoom getWxRoom(@RequestBody String json) {
		JSONObject wxRoomJson = JSONObject.fromObject(json);
		int wxRoomId = wxRoomJson.getInt("wxRoomId");
		WxRoom wxRoom = wxRoomService.getWxRoom(wxRoomId);
		
		return wxRoom;
	}
	
	@RequestMapping(value = "/wxroom/senwxdanmu", method = RequestMethod.POST)
	@ResponseBody
	public WxDanmu sendWxDanmu(@RequestBody String json) {
		JSONObject wxDanmuJson = JSONObject.fromObject(json);
		
		WxDanmu wxDanmu = new WxDanmu();
		wxDanmu.setDevId(wxDanmuJson.getString("devId"));
		wxDanmu.setWxRoomId(wxDanmuJson.getInt("wxRoomId"));
		wxDanmu.setUserId(wxDanmuJson.getString("userId"));
		wxDanmu.setMessages(wxDanmuJson.getString("messages"));
		wxDanmu.setCreate_time(new TimeUtil().currentTimeStamp);
		
		int wxDanmuId = wxRoomService.sendDanmu(wxDanmu);
		wxDanmu.setId(wxDanmuId);
		return wxDanmu;
	}
	
	@RequestMapping(value = "/wxroom/getwxdanmus", method = RequestMethod.POST)
	@ResponseBody
	public List<WxDanmu> getDmBywxRoom(@RequestBody String json) {
		List<WxDanmu> resultList = new ArrayList<WxDanmu>();
		
		JSONObject wxRoomIdJson = JSONObject.fromObject(json);
		int wxRoomId = wxRoomIdJson.getInt("wxRoomId");
		resultList = wxRoomService.getDanmuByWxRoomId(wxRoomId);
		
		return resultList;
	}
	
	/**
     * 用SHA1算法生成安全签名
     * @param token 票据
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param encrypt 密文
     * @return 安全签名
     * @throws NoSuchAlgorithmException 
     * @throws AesException 
     */
    public  String getSHA1(String token, String timestamp, String nonce) {
            String[] array = new String[] { token, timestamp, nonce };
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 3; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
    }
}
