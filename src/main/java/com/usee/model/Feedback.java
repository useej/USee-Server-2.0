package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "feedback")
public class Feedback {
	
	@Id
	@GeneratedValue(generator = "commentGenerator")    
	@GenericGenerator(name = "commentGenerator", strategy = "identity")
	@Column
	private int id;
	@Column
	private String time;
	@Column
	private String messages;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMessages() {
		return messages;
	}
	public void setMessages(String messages) {
		this.messages = messages;
	}
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", time=" + time + ", messages=" + messages + "]";
	}
	
	

}
