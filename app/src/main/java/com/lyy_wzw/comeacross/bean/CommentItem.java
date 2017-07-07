package com.lyy_wzw.comeacross.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;


public class CommentItem extends BmobObject implements Serializable{

	private CAUser user;
	private CAUser toReplyUser;
	private String content;
	private String footPrintId;

	public String getFootPrintId() {
		return footPrintId;
	}

	public void setFootPrintId(String footPrintId) {
		this.footPrintId = footPrintId;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public CAUser getUser() {
		return user;
	}
	public void setUser(CAUser user) {
		this.user = user;
	}
	public CAUser getToReplyUser() {
		return toReplyUser;
	}
	public void setToReplyUser(CAUser toReplyUser) {
		this.toReplyUser = toReplyUser;
	}
	@Override
	public String toString() {
		return "CommentItem{" +
				", user=" + user +
				", toReplyUser=" + toReplyUser +
				", content='" + content + '\'' +
				", footPrintId='" + footPrintId + '\'' +
				'}';
	}

}
