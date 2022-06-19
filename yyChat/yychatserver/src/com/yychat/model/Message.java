package com.yychat.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable,MessageType{
    String messageType;
    //ʵ���壺�ͻ��˷���������Ϣ��������������1������������Ա����,������get��set����
    String sender;
    String receiver;
    String chatContent;
    String chatContent2;

    //ʵ��ʮ��������������Ϣ��message���У�����1�����sendTime����������Ϣ��ʱ���ֶ�
    Date sendTime;

    public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

    //ʵ���ߣ��������ߺ���ͼ�꣬����7������µ�������
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
