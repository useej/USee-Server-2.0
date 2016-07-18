package com.usee.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 此类为User实体类
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(length = 32)
	private String userID;

	// 用户性别. 1为男性,0为女性
	@Column(length = 2)
	private int gender;

	// 用户昵称
	@Column(length = 32)
	private String nickName;

	// 用户头像
	@Column(length = 100)
	private String userIcon;

	// 用户设备
	@Column(length = 15)
	private String cellphone;

	// 用户密码
	@Column(length = 32)
	private String password;
	
	private Date createTime;
	
	@Column(length = 32)
	private String qqOpenId;
	
	@Column(length = 32)
	private String weixinOpenId;
	
	@Column(length = 32)
	private String weiboOpenId;

	public String getUserID() {
		return userID;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getWeixinOpenID() {
		return weixinOpenId;
	}

	public void setWeixinOpenID(String weixinOpenID) {
		this.weixinOpenId = weixinOpenID;
	}

	public String getWeiboOpenId() {
		return weiboOpenId;
	}

	public void setWeiboOpenId(String weiboOpenId) {
		this.weiboOpenId = weiboOpenId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
