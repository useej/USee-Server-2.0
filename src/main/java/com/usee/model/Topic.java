package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="topic")
public class Topic {

	@Id
	@Column(length=32)
	private String  id;
	
	@Column(length=10)
	private String title;

	@Column(length=70)
	private String description;
	
	@Column
	private double lon;
	
	@Column
	private double lat;
	
	@Column(length=3)
	private int radiu;

	@Column(length=20)
	private String create_time;

	@Column(length=20)
	private String lastDanmu_time;
	
	@Column(length=32)
	private String userID;

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

	public int getRadiu() {
		return radiu;
	}

	public void setRadiu(int radiu) {
		this.radiu = radiu;
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
	
}
