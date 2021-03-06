package com.usee.dao;

import java.util.List;

import com.usee.model.Comment;

public interface CommentDao {
	public void saveComment(Comment comment);
	
	public Comment getComment(int id);
	
	public List<Comment> getCommentbyDanmuId(int danmuId);
	
	public List<String> getCommentSenderbyDanmuId(int danmuId);
	
	public int getLatestCommentIdByUserIdAndDanmuId(String sender, int danmuId);

    public Comment getCommentbyReplycommentId(int replycommentId);

    public List<Comment> getCommentbyDanmuIdandCreatetime(int danmuId, String createTime);
    
    public Comment getCommentByIdAndSender(int commentID, String sender);
    
    public List<Comment> getReplyCommentListbyReplycommentId(int replycommentId);
    
    public boolean deleteComment(int commentID);
}
