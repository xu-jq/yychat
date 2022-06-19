package com.yychat.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable,MessageType{
    String messageType;
    //实验五：客户端发送聊天信息到服务器，步骤1：定义三个成员变量,并生成get和set方法
    String sender;
    String receiver;
    String chatContent;
    String chatContent2;

    //实验十三：保存聊天信息到message表中，步骤1：添加sendTime发送聊天信息的时间字段
    Date sendTime;

    public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

    //实验七：激活在线好友图标，步骤7：添加新的属性名
    String onLineFriend;

	public String getOnLineFriend() {
		return onLineFriend;
	}

	public void setOnLineFriend(String onLineFriend) {
		this.onLineFriend = onLineFriend;
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

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getChatContent2() {
		return chatContent2;
	}

	public void setChatContent2(String chatContent2) {
		this.chatContent2 = chatContent2;
	}
}
