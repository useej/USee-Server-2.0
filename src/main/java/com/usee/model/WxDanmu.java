	package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wxdanmu")
public class WxDanmu {
	
	@Column
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	private Integer wxRoomId;
	
	@Column
	private String userId;
	
	@Column
	private String create_time;
	
	@Column
	private String devId;
	
	@Column
	private String messages;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWxRoomId() {
		return wxRoomId;
	}

	public void setWxRoomId(Integer wxRoomId) {
		this.wxRoomId = wxRoomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

}
