package com.usee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(generator = "commentGenerator")    
	@GenericGenerator(name = "commentGenerator", strategy = "identity")
	@Column
	private int id;
	@Column
	private int danmuId;
	@Column
	private String sender;
	@Column
	private String receiver;
	@Column
	private String content;
	@Column(nullable = true)
	private Integer reply_commentId;
	@Column
	private int type;
	@Column
	private String create_time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDanmuId() {
		return danmuId;
	}
	public void setDanmuId(int danmuId) {
		this.danmuId = danmuId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
//	public int getReply_commentId() {
//		if((Integer)reply_commentId == null){
//			return 0;
//		}
//		else {
//			return reply_commentId;
//		}
//	}
//	public void setReply_commentId(Integer reply_commentId) {
//		if(reply_commentId != null){
//			this.reply_commentId = reply_commentId;
//		}
//		else {
//			this.reply_commentId = 0;
//		}
//	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Integer getReply_commentId() {
		return reply_commentId;
	}
	public void setReply_commentId(Integer reply_commentId) {
		this.reply_commentId = reply_commentId;
	}
	
	@Override
	public String toString() {
		return " {\"id\":" + id + ", \"danmuId\":" + danmuId
				+ ", \"sender\":\"" + sender + "\", \"receiver\":\"" + receiver
				+ "\", \"content\":\"" + content + "\", \"replay_commentId\":"
				+ reply_commentId + ", \"type\":" + type
				+ ", \"create_time\":\"" + create_time + "\"}";
	}
	
}
