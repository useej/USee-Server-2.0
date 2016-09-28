package com.usee.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="danmuimgs")
public class DanmuImgs {
	@Id
	@GeneratedValue(generator = "danmuGenerator")    
	@GenericGenerator(name = "danmuGenerator", strategy = "identity")
	@Column
	private int id;
	
	@Column(length=32)
	private int danmuID;

	@Column(length=32)
	private int views;
	
	@Column
	private String imgurl;
	
	@Transient
	private String[] imgurls;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDanmuID() {
		return danmuID;
	}

	public void setDanmuID(int danmuID) {
		this.danmuID = danmuID;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	

	public String[] getImgurls() {
		return imgurls;
	}

	public void setImgurls(String[] imgurls) {
		this.imgurls = imgurls;
	}

	@Override
	public String toString() {
		return "DanmuImgs [id=" + id + ", danmuID=" + danmuID + ", views=" + views + ", imgurl=" + imgurl + ", imgurls="
				+ Arrays.toString(imgurls) + "]";
	}


}