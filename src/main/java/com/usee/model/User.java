package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 此实体的定义应用了springmvc的注解，省去了传统的Hibernate跟数据库之间的映射配置文件
 * 
 * @author will
 *
 */
@Entity

@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private String userId;
	@Column
	private int gender;
	@Column
	private String nickname;
	@Column
	private String userIcon;
	@Column
	private String cellphone;
	@Column
	private String password;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
}
