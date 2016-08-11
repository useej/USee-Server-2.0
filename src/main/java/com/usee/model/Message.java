package com.usee.model;

public class Message {
	private String nickname;

	private int gender;

	private String userIcon;

	private String content;

	private int type;

	private String create_time;

	private int danmuId;
	
	private int commentId;
	
	private String sender;
	
	private String danmuUserID;
	
	private String topicTitle;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getDanmuId() {
		return danmuId;
	}

	public void setDanmuId(int danmuId) {
		this.danmuId = danmuId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDanmuUserID() {
		return danmuUserID;
	}

	public void setDanmuUserID(String danmuUserID) {
		this.danmuUserID = danmuUserID;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	@Override
	public String toString() {
		return "Message [nickname=" + nickname + ", gender=" + gender + ", userIcon=" + userIcon + ", content="
				+ content + ", type=" + type + ", create_time=" + create_time + ", danmuId=" + danmuId + ", commentId="
				+ commentId + ", sender=" + sender + ", danmuUserID=" + danmuUserID + ", topicTitle=" + topicTitle
				+ "]";
	}

}
