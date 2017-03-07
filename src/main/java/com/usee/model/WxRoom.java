package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wxroom")
public class WxRoom {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(length=10)
	private String ownerName;

	@Column(length=20)
	private String create_time;

	@Column(length=32)
	private String ownerId;

	@Column(length=10)
	private int danmuNum;
	
	@Column(length = 100)
	private String ownerIcon;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public int getDanmuNum() {
		return danmuNum;
	}

	public void setDanmuNum(int danmuNum) {
		this.danmuNum = danmuNum;
	}

	public String getOwnerIcon() {
		return ownerIcon;
	}

	public void setOwnerIcon(String ownerIcon) {
		this.ownerIcon = ownerIcon;
	}


}
