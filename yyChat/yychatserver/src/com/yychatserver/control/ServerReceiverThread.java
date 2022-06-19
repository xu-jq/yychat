package com.yychatserver.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.yychat.model.Message;
import com.yychat.model.MessageType;

public class ServerReceiverThread extends Thread{
   Socket s;
	public ServerReceiverThread(Socket s) {
		this.s=s;
	}
	//ʵ���壺�ͻ��˷���������Ϣ��������������8����дrun()�������տͻ��˷�������Message����(mess)
	@Override
   public void run() {
	   try {
		   while(true) {
			    ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				Message mess=(Message)ois.readObject();
				String sender=mess.getSender();

				if(mess.getMessageType().equals(Message.MOVEE_STRANGER)){
					String moveFriend = mess.getChatContent();
					System.out.println("�ƶ��������ǣ�"+ moveFriend);
					if(DBUtil.seekUser(moveFriend)) {
						//Ϊ�棬˵�����û�����
						if(DBUtil.seekUseRelation(sender,moveFriend,"1")) {
							//Ϊ�棬˵���Ѿ��Ǻ��ѣ�����ɾ��
							DBUtil.moveFromUserRelation(sender, moveFriend,"1");
							String allFriend=DBUtil.seekAllFriend(sender);
							String strangerList = DBUtil.seekStranger(sender);
							mess.setChatContent(allFriend);
							mess.setChatContent2(strangerList);
							mess.setMessageType(Message.MOVEE_STRANGER_SUCCESS);
						}else {
							//Ϊ�٣�˵�������Ǻ��ѣ�����ɾ��
							mess.setMessageType(Message.MOVE_STRANGER_FAILURE);
						}
					}else {
						//Ϊ�٣�˵������û�������
						mess.setMessageType(Message.MOVE_FRIEND_FAILURE_NO_USER);
					}
					//������Ϣ���ͻ��˽��д���
					sendMessage(s,mess);
				}

				//�û����߸��º��������б�
			   if(mess.getMessageType().equals(MessageType.OFFLINE_FRIEND_TO_SERVER)){
				   YychatServer.hmSocket.remove(sender);
				   System.out.println(sender+"�����ˣ�׼��ת��OFFLINE_FRIEND��Ϣ");
				   //
				   Set onlineFriendSet = YychatServer.hmSocket.keySet();
				   Iterator it = onlineFriendSet.iterator();//��������������
				   while(it.hasNext()) {
					   String friendName=(String)it.next();//����sender���ѵ�����
					   if(!(friendName.equals(sender))) {
						   Socket receiverSocket = YychatServer.hmSocket.get(friendName);

						   mess.setReceiver(friendName);
						   mess.setMessageType(Message.OFFLINE_FRIEND);
						   sendMessage(receiverSocket,mess);
					   }
				   }

			   }

				//�ڷ������˴����û��˳��Ĵ���
			   if (mess.getMessageType().equals(MessageType.USER_EXIT_SERVER_THREAD_CLOSE)) {
			   		mess.setMessageType(MessageType.USER_EXIT_CLIENT_THREAD_CLOSE);
					sendMessage(s,mess);

				   System.out.println(sender + "�û��˳��ˣ����ڹر�������߳�");
//				   Message message = new Message();
//				   for (Map.Entry < String, Socket > entry: YychatServer.hmSocket.entrySet()) {
//					   System.out.println(entry.getKey());
//					   System.out.println(entry.getValue());
//				   }

				   s.close();
				   break;//�˳��̵߳�ѭ�����
			   }

				//����
				if(mess.getMessageType().equals(Message.SHAKE))
				{
					String receiver=mess.getReceiver();
					mess.setSendTime(new java.util.Date());
					DBUtil.saveMessage(sender,receiver,mess.getChatContent());
					System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getChatContent());
					Socket receiverSocket=(Socket)YychatServer.hmSocket.get(receiver);
					System.out.println("����������:"+receiver);
					ObjectOutputStream oos=new ObjectOutputStream(receiverSocket.getOutputStream());
					oos.writeObject(mess);
				}

				if (mess.getMessageType().equals(Message.UPDATE_PASSWORD)){
					String latterPassword = mess.getChatContent();
					System.out.println("�º��ѵ������ǣ�"+ latterPassword);
					DBUtil.updatePassword(sender,latterPassword);

					mess.setMessageType(Message.UPDATE_PASSWORD_SUCCESS);
					//������Ϣ���ͻ��˽��д���
					sendMessage(s,mess);
				}

				//ɾ������ ServerReceiverThread��
				if(mess.getMessageType().equals(Message.DELETE_FRIEND)) {
			    	String newFriendName=mess.getChatContent();
			    	System.out.println("�º��ѵ������ǣ�"+newFriendName);
			    	if(DBUtil.seekUser(newFriendName)) {
			    		//Ϊ�棬˵�����û�����
			    		if(DBUtil.seekUseRelation(sender,newFriendName,"1")) {
			    			//Ϊ�棬˵���Ѿ��Ǻ��ѣ�����ɾ��
			    			DBUtil.deleteFromUserRelation(sender, newFriendName,"1");
							String strangerList = DBUtil.seekStranger(sender);
							String allFriend=DBUtil.seekAllFriend(sender);
			    			mess.setChatContent(allFriend);
			    			mess.setChatContent2(strangerList);
			    			mess.setMessageType(Message.DELETE_FRIEND_SUCCESS);
			    		}else {
			    			//Ϊ�٣�˵�������Ǻ��ѣ�����ɾ��
			    			mess.setMessageType(Message.Delete_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
			    		}
			    	}else {
			    		//Ϊ�٣�˵������û�������
			    		mess.setMessageType(Message.DELETE_FRIEND_FAILURE_NO_USER);
			    	}
			    	//������Ϣ���ͻ��˽��д���
			    	sendMessage(s,mess);
			    }


				//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����5���ڷ������˽�������º��ѵ���Ϣ�����ж��Ƿ������Ϊ�º��ѡ�
				if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND)) {
					String newFriendName=mess.getChatContent();
					System.out.println("�º��ѵ������ǣ�"+newFriendName);
					if(DBUtil.seekUser(newFriendName)) {
						//Ϊ�棬˵�����û�����
						if(DBUtil.seekUseRelation(sender,newFriendName,"1")) {
							//Ϊ�棬˵���Ѿ��Ǻ��ѣ������ظ����
							mess.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
						}else {
							//Ϊ�٣�˵�������Ǻ��ѣ��������Ϊ����
							DBUtil.insertIntoUserRelation(sender,newFriendName,"1");
							String allFriend=DBUtil.seekAllFriend(sender);
							mess.setChatContent(allFriend);
							mess.setMessageType(Message.ADD_NEW_FRIEND_SUCCESS);
						}
					}else {
						//Ϊ�٣�˵������û�������
						mess.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_NO_USER);
					}
					//������Ϣ���ͻ��˽��д���
					sendMessage(s,mess);
				}

				//ʵ��ˣ����������ߺ���ͼ�꣬����4�����������յ�NEW_ONLINE_FRIEND_TO_SERVER��Ϣ����sender��ȫ�����ߺ��ѷ���
				if(mess.getMessageType().equals(Message.NEW_ONLINE_FRIEND_TO_SERVER)) {
					System.out.println(sender+"�����ˣ�׼��ת��NEW_ONLINE_FRIEND��Ϣ");
					//ʵ��ˣ����������ߺ���ͼ�꣬����5���õ�senderȫ�����ߺ�������
					Set onlineFriendSet=YychatServer.hmSocket.keySet();
					Iterator it=onlineFriendSet.iterator();//��������������
					while(it.hasNext()) {
						 String friendName=(String)it.next();//����sender���ѵ�����
						 if(!(friendName.equals(sender))) {
							 Socket receiverSocket = YychatServer.hmSocket.get(friendName);

							 //ʵ��ˣ����������ߺ���ͼ�꣬����6�������ߺ��ѷ�����Ϣ
							 mess.setReceiver(friendName);
							 mess.setMessageType(Message.NEW_ONLINE_FRIEND);
							 sendMessage(receiverSocket,mess);
						 }
					}
				}

				//ʵ���ߣ��������ߺ���ͼ�꣬����4�����������յ����������ߺ������ֵ���Ϣ
				if(mess.getMessageType().equals(Message.REQUEST_ONLINE_FRIEND)) {
					System.out.println("�������յ� "+sender+" �����������������ߺ������ֵ���Ϣ");

					//ʵ���ߣ��������ߺ���ͼ�꣬����5��������Ҫ�õ����ߺ�������
					Set onlineFriendSet=YychatServer.hmSocket.keySet();
					Iterator it=onlineFriendSet.iterator();//��������������
					String onLineFriend="";
					while(it.hasNext()) {
						   String friendName=(String)it.next();
						  if(!(friendName.equals(sender))) onLineFriend=" "+friendName+onLineFriend;
					}
					System.out.println(sender+"���ߺ����У�"+onLineFriend);

					//ʵ���ߣ��������ߺ���ͼ�꣬����6�������ߺ������ַ��͵��ͻ���
					mess.setOnLineFriend(onLineFriend);
					mess.setMessageType(Message.RESPONSE_ONLINE_FRIEND);
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(mess);
 				}


				if(mess.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
					String receiver=mess.getReceiver();

					//ʵ��ʮ��������������Ϣ��message���У�����2���ڷ����������÷���������Ϣ��ʱ��
					mess.setSendTime(new java.util.Date());

					//ʵ��ʮ��������������Ϣ��message���У�����3������������Ϣ�����ݿ�message����
					DBUtil.saveMessage(sender,receiver,mess.getChatContent());

					System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getChatContent());
					//ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������1����ô�ҵ����ն�Ӧ��Socket����
					//����ǰ�汣��ĺõ��û����Ͷ�Ӧ��socket����ȡ�������ߵ�socket����
					Socket receiverSocket=(Socket)YychatServer.hmSocket.get(receiver);
					System.out.println("����������:"+receiver);

					//ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������4��������ת��������Ϣ�����շ���
					ObjectOutputStream oos=new ObjectOutputStream(receiverSocket.getOutputStream());
					oos.writeObject(mess);
				}
		   }

	   } catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
	   }
   }


	//ʵ��ˣ����������ߺ���ͼ�꣬����7������sendMessage()����
   public void sendMessage(Socket s,Message mess) {
	    ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(mess);
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
}
