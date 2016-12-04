package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "topic_type")
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TopicType {
	@Id
	@GeneratedValue(generator = "topictypeGenerator")    
	@GenericGenerator(name = "topictypeGenerator", strategy = "identity")
	@Column
	private int id;
	@Column
	private String topicID;
	@Column
	private int typeID;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopicid(){
		return topicID;
	}
	public void setTopicid(String topicID){
		this.topicID = topicID;
	}
	public int getTypeid() {
		return id;
	}
	public void setTypeid(int typeID) {
		this.typeID = typeID;
	}
	@Override
	public String toString() {
		return "UserTopic [id=" + id + ", topicID=" + topicID + ", typeID=" + typeID + "]";
	}
	
}
