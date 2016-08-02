package com.usee.service.impl;

import org.springframework.stereotype.Service;

import com.usee.service.JPushService;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

@Service
public class JPushServiceImpl implements JPushService {

	public static final String APPKER = "95c015cf38b73a31e11c4a8f";
	public static final String MASTER_SECRET = "16348a1d13307587bfd34e2f";

	private static String title = "USee";
	private static String alert;
	private static String alias;

	public void push(String userID, String message) {
		
		alias = userID;
		alert = message;

		// 设置appkey、master secret以及消息过期时间
		@SuppressWarnings("deprecation")
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKER, 60 * 60 * 24);

		// 构建一个 PushPayload 对象
		PushPayload payload = buildPushObject_android_alias_alertWithTitle();

		try {
			PushResult result = jpushClient.sendPush(payload);
			//LOG.info("Got result - " + result);
			System.out.println("Got result - " + result);

		} catch (APIConnectionException e) {
			//LOG.error("Connection error. Should retry later. ", e);
			e.printStackTrace();

		} catch (APIRequestException e) {
			e.printStackTrace();
			//LOG.error("Error response from JPush server. Should review and fix it. ", e);
			//LOG.info("HTTP Status: " + e.getStatus());
			//LOG.info("Error Code: " + e.getErrorCode());
			//LOG.info("Error Message: " + e.getErrorMessage());
			//LOG.info("Msg ID: " + e.getMsgId());
		}
	}

	// 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知
	public static PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll(alert);
	}

	// 构建推送对象：所有平台，所有用户，通知内容为 ALERT
	public static PushPayload buildPushObject_all_alias_alert() {
		return PushPayload.newBuilder()
				.setPlatform(Platform.all()) //设置接受的平台  
				.setAudience(Audience.all()) //Audience设置为all，说明采用广播方式推送，所有用户都可以接收到 
				.setNotification(Notification.alert(alert))
				.build();
	}

	// 构建推送对象：平台是 Android，目标是 alias 为  alias 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE
	public static PushPayload buildPushObject_android_alias_alertWithTitle() {
		return PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.alias(alias))
				.setNotification(Notification.android(alert, title, null))
				.build();
	}

}
