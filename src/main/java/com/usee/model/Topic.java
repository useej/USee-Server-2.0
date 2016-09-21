package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="topic")
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Topic {
	@Id
	@Column
	private String id;

	@Column(length=10)
	private String title;

	@Column(length=70)
	private String description;
	
	@Column
	private double lon;
	
	@Column
	private double lat;
	
	@Column(length=3)
	private int radius;

	@Column(length=20)
	private String create_time;

	@Column(length=20)
	private String lastDanmu_time;
	
	@Column(length=32)
	private String userID;
	
	@Column(length=70)
	private String poi;
	
	@Column(length=10)
	private int danmuNum;
	
	@Column(length=1)
	private int expired ;
	
	@Column(length=5)
	private String type;

	@Column(length=20)
	private String delete_time;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getLastDanmu_time() {
		return lastDanmu_time;
	}

	public void setLastDanmu_time(String lastDanmu_time) {
		this.lastDanmu_time = lastDanmu_time;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPoi() {
		return poi;
	}

	public void setPoi(String poi) {
		this.poi = poi;
	}

	public int getDanmuNum() {
		return danmuNum;
	}

	public void setDanmuNum(int danmuNum) {
		this.danmuNum = danmuNum;
	}

	public int getExpired() {
		return expired;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDelete_time() {
		return delete_time;
	}

	public void setDelete_time(String delete_time) {
		this.delete_time = delete_time;
	}
	
}
