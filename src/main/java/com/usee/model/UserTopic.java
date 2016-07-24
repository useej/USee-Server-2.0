package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "user_topic")
public class UserTopic {
	@Id
	@GeneratedValue(generator = "usertopicGenerator")    
	@GenericGenerator(name = "usertopicGenerator", strategy = "identity")
	@Column
	private int id;
	@Column
	private String userId;
	@Column
	private String topicId;
	@Column
	private String firstvisit_time;
	@Column
	private int randomNameID;
	@Column
	private int randomIconID;
	@Column
	private String lastVisit_time;
	@Column
	private int frequency;
	@Column
	private int dislike;
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
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getFirstvisit_time() {
		return firstvisit_time;
	}
	public void setFirstvisit_time(String firstvisit_time) {
		this.firstvisit_time = firstvisit_time;
	}
	public int getRandomNameID() {
		return randomNameID;
	}
	public void setRandomNameID(int randomNameID) {
		this.randomNameID = randomNameID;
	}
	public int getRandomIconID() {
		return randomIconID;
	}
	public void setRandomIconID(int randomIconID) {
		this.randomIconID = randomIconID;
	}
	public String getLastVisit_time() {
		return lastVisit_time;
	}
	public void setLastVisit_time(String lastVisit_time) {
		this.lastVisit_time = lastVisit_time;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public int getDislike() {
		return dislike;
	}
	public void setDislike(int dislike) {
		this.dislike = dislike;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
}
