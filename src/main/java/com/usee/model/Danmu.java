package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "danmu")
public class Danmu {
	@Id
	@GeneratedValue(generator = "danmuGenerator")    
	@GenericGenerator(name = "danmuGenerator", strategy = "identity")
	@Column
	private int id;
	@Column
	private String userId;
	@Column
	private String devId;
	@Column
	private String status;
	@Column
	private String topicId;
	@Column
	private String lon;
	@Column
	private String lat;
	@Column
	private int praisenum;
	@Column
	private int downnum;
	@Column
	private int commentnum;
	@Column
	private int hitnum;
	@Column
	private String create_time;
	@Column
	private String delete_time;
	@Column
	private String address;
	@Column
	private int head;
	@Column
	private String messages;
	@Column
	private String userIcon;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public int getPraisenum() {
		return praisenum;
	}

	public void setPraisenum(int praisenum) {
		this.praisenum = praisenum;
	}

	public int getDownnum() {
		return downnum;
	}

	public void setDownnum(int downnum) {
		this.downnum = downnum;
	}

	public int getCommentnum() {
		return commentnum;
	}

	public void setCommentnum(int commentnum) {
		this.commentnum = commentnum;
	}

	public int getHitnum() {
		return hitnum;
	}

	public void setHitnum(int hitnum) {
		this.hitnum = hitnum;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getDelete_time() {
		return delete_time;
	}

	public void setDelete_time(String delete_time) {
		this.delete_time = delete_time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	@Override
	public String toString() {
		return " {\"id\":" + id + ", \"userId\":\"" + userId
				+ "\", \"devId\":\"" + devId + "\", \"status\":\"" + status
				+ "\", \"topicId\":\"" + topicId + "\", \"lon\":\"" + lon
				+ "\", \"lat\":\"" + lat + "\", \"praisenum\":" + praisenum
				+ ", \"downnum\":" + downnum + ", \"commentnum\":"
				+ commentnum + ", \"hitnum\":" + hitnum
				+ ", \"create_time\":\"" + create_time + "\", \"delete_time\":\""
				+ delete_time + "\", \"address\":\"" + address + "\", \"head\":"
				+ head + ", \"messages\":\"" + messages + "\", \"userIcon\":\""
				+ userIcon + "\"}";
	}

}
