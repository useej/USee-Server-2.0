package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "usertopic_visit")
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class UserTopic_Visit {
	@Id
	@GeneratedValue(generator = "usertopicGenerator")    
	@GenericGenerator(name = "usertopicGenerator", strategy = "identity")
	@Column
	private int id;
	@Column
	private String userID;
	@Column
	private String topicID;
	@Column
	private String firstvisit_time;
	@Column
	private String lastVisit_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getTopicID() {
		return topicID;
	}
	public void setTopicID(String topicID) {
		this.topicID = topicID;
	}
	public String getFirstvisit_time() {
		return firstvisit_time;
	}
	public void setFirstvisit_time(String firstvisit_time) {
		this.firstvisit_time = firstvisit_time;
	}
	public String getLastVisit_time() {
		return lastVisit_time;
	}
	public void setLastVisit_time(String lastVisit_time) {
		this.lastVisit_time = lastVisit_time;
	}
	
	
}
