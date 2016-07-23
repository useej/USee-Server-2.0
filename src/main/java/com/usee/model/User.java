package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 此类为User实体类
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(generator="sid")
	@GenericGenerator(name="sid",strategy="assigned")
	@Column(length = 32)
	private String userID;

	// 用户性别. 1为男性,0为女性
	@Column(length = 2)
	private int gender;

	// 用户昵称
	@Column(length = 32)
	private String nickname;

	// 用户头像
	@Column(length = 100)
	private String userIcon;

	// 用户设备
	@Column(length = 15)
	private String cellphone;

	// 用户密码
	@Column(length = 32)
	private String password;
	
	@Column(length = 20)
	private String createTime;
	
	@Column(length = 32)
	private String openID_qq;
	
	@Column(length = 32)
	private String openID_wx;
	
	@Column(length = 32)
	private String openID_wb;

	public String getUserID() {
		return userID;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOpenID_qq() {
		return openID_qq;
	}

	public void setOpenID_qq(String openID_qq) {
		this.openID_qq = openID_qq;
	}

	public String getOpenID_wx() {
		return openID_wx;
	}

	public void setOpenID_wx(String openID_wx) {
		this.openID_wx = openID_wx;
	}

	public String getOpenID_wb() {
		return openID_wb;
	}

	public void setOpenID_wb(String openID_wb) {
		this.openID_wb = openID_wb;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", gender=" + gender + ", nickname=" + nickname + ", userIcon=" + userIcon
				+ ", cellphone=" + cellphone + ", password=" + password + ", createTime=" + createTime + ", openID_qq="
				+ openID_qq + ", openID_wx=" + openID_wx + ", openID_wb=" + openID_wb + "]";
	}
	
	

}
