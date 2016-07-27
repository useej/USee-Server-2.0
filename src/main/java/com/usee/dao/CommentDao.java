package com.usee.dao;

import java.util.List;

import com.usee.model.Comment;

public interface CommentDao {
	public void saveComment(Comment comment);
	
	public Comment getComment(int id);
	
	public List<Comment> getCommentbyDanmuId(int danmuId);
	
	public List<String> getCommentSenderbyDanmuId(int danmuId);
}
