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
	//实验五：客户端发送聊天信息到服务器，步骤8：重写run()，来接收客户端发送来的Message对象(mess)
	@Override
   public void run() {
	   try {
		   while(true) {
			    ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				Message mess=(Message)ois.readObject();
				String sender=mess.getSender();

				if(mess.getMessageType().equals(Message.MOVEE_STRANGER)){
					String moveFriend = mess.getChatContent();
					System.out.println("移动的名字是："+ moveFriend);
					if(DBUtil.seekUser(moveFriend)) {
						//为真，说明该用户存在
						if(DBUtil.seekUseRelation(sender,moveFriend,"1")) {
							//为真，说明已经是好友，可以删除
							DBUtil.moveFromUserRelation(sender, moveFriend,"1");
							String allFriend=DBUtil.seekAllFriend(sender);
							String strangerList = DBUtil.seekStranger(sender);
							mess.setChatContent(allFriend);
							mess.setChatContent2(strangerList);
							mess.setMessageType(Message.MOVEE_STRANGER_SUCCESS);
						}else {
							//为假，说明还不是好友，不能删除
							mess.setMessageType(Message.MOVE_STRANGER_FAILURE);
						}
					}else {
						//为假，说明这个用户不存在
						mess.setMessageType(Message.MOVE_FRIEND_FAILURE_NO_USER);
					}
					//发送消息到客户端进行处理
					sendMessage(s,mess);
				}

				//用户下线更新好友在线列表
			   if(mess.getMessageType().equals(MessageType.OFFLINE_FRIEND_TO_SERVER)){
				   YychatServer.hmSocket.remove(sender);
				   System.out.println(sender+"下线了，准备转发OFFLINE_FRIEND消息");
				   //
				   Set onlineFriendSet = YychatServer.hmSocket.keySet();
				   Iterator it = onlineFriendSet.iterator();//创建迭代器对象
				   while(it.hasNext()) {
					   String friendName=(String)it.next();//就是sender好友的名字
					   if(!(friendName.equals(sender))) {
						   Socket receiverSocket = YychatServer.hmSocket.get(friendName);

						   mess.setReceiver(friendName);
						   mess.setMessageType(Message.OFFLINE_FRIEND);
						   sendMessage(receiverSocket,mess);
					   }
				   }

			   }

				//在服务器端处理用户退出的代码
			   if (mess.getMessageType().equals(MessageType.USER_EXIT_SERVER_THREAD_CLOSE)) {
			   		mess.setMessageType(MessageType.USER_EXIT_CLIENT_THREAD_CLOSE);
					sendMessage(s,mess);

				   System.out.println(sender + "用户退出了！正在关闭其服务线程");
//				   Message message = new Message();
//				   for (Map.Entry < String, Socket > entry: YychatServer.hmSocket.entrySet()) {
//					   System.out.println(entry.getKey());
//					   System.out.println(entry.getValue());
//				   }

				   s.close();
				   break;//退出线程的循环结果
			   }

				//抖动
				if(mess.getMessageType().equals(Message.SHAKE))
				{
					String receiver=mess.getReceiver();
					mess.setSendTime(new java.util.Date());
					DBUtil.saveMessage(sender,receiver,mess.getChatContent());
					System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getChatContent());
					Socket receiverSocket=(Socket)YychatServer.hmSocket.get(receiver);
					System.out.println("接收者名字:"+receiver);
					ObjectOutputStream oos=new ObjectOutputStream(receiverSocket.getOutputStream());
					oos.writeObject(mess);
				}

				if (mess.getMessageType().equals(Message.UPDATE_PASSWORD)){
					String latterPassword = mess.getChatContent();
					System.out.println("新好友的名字是："+ latterPassword);
					DBUtil.updatePassword(sender,latterPassword);

					mess.setMessageType(Message.UPDATE_PASSWORD_SUCCESS);
					//发送消息到客户端进行处理
					sendMessage(s,mess);
				}

				//删除好友 ServerReceiverThread类
				if(mess.getMessageType().equals(Message.DELETE_FRIEND)) {
			    	String newFriendName=mess.getChatContent();
			    	System.out.println("新好友的名字是："+newFriendName);
			    	if(DBUtil.seekUser(newFriendName)) {
			    		//为真，说明该用户存在
			    		if(DBUtil.seekUseRelation(sender,newFriendName,"1")) {
			    			//为真，说明已经是好友，可以删除
			    			DBUtil.deleteFromUserRelation(sender, newFriendName,"1");
							String strangerList = DBUtil.seekStranger(sender);
							String allFriend=DBUtil.seekAllFriend(sender);
			    			mess.setChatContent(allFriend);
			    			mess.setChatContent2(strangerList);
			    			mess.setMessageType(Message.DELETE_FRIEND_SUCCESS);
			    		}else {
			    			//为假，说明还不是好友，不能删除
			    			mess.setMessageType(Message.Delete_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
			    		}
			    	}else {
			    		//为假，说明这个用户不存在
			    		mess.setMessageType(Message.DELETE_FRIEND_FAILURE_NO_USER);
			    	}
			    	//发送消息到客户端进行处理
			    	sendMessage(s,mess);
			    }


				//实验十二：添加好友和更新好友列表，步骤5：在服务器端接收添加新好友的消息，并判断是否能添加为新好友。
				if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND)) {
					String newFriendName=mess.getChatContent();
					System.out.println("新好友的名字是："+newFriendName);
					if(DBUtil.seekUser(newFriendName)) {
						//为真，说明该用户存在
						if(DBUtil.seekUseRelation(sender,newFriendName,"1")) {
							//为真，说明已经是好友，不能重复添加
							mess.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
						}else {
							//为假，说明还不是好友，可以添加为好友
							DBUtil.insertIntoUserRelation(sender,newFriendName,"1");
							String allFriend=DBUtil.seekAllFriend(sender);
							mess.setChatContent(allFriend);
							mess.setMessageType(Message.ADD_NEW_FRIEND_SUCCESS);
						}
					}else {
						//为假，说明这个用户不存在
						mess.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_NO_USER);
					}
					//发送消息到客户端进行处理
					sendMessage(s,mess);
				}

				//实验八：激活新上线好友图标，步骤4：服务器接收到NEW_ONLINE_FRIEND_TO_SERVER消息，向sender的全部在线好友发送
				if(mess.getMessageType().equals(Message.NEW_ONLINE_FRIEND_TO_SERVER)) {
					System.out.println(sender+"上线了，准备转发NEW_ONLINE_FRIEND消息");
					//实验八：激活新上线好友图标，步骤5：拿到sender全部在线好友名字
					Set onlineFriendSet=YychatServer.hmSocket.keySet();
					Iterator it=onlineFriendSet.iterator();//创建迭代器对象
					while(it.hasNext()) {
						 String friendName=(String)it.next();//就是sender好友的名字
						 if(!(friendName.equals(sender))) {
							 Socket receiverSocket = YychatServer.hmSocket.get(friendName);

							 //实验八：激活新上线好友图标，步骤6：向在线好友发送消息
							 mess.setReceiver(friendName);
							 mess.setMessageType(Message.NEW_ONLINE_FRIEND);
							 sendMessage(receiverSocket,mess);
						 }
					}
				}

				//实验七：激活在线好友图标，步骤4：服务器接收到请求获得在线好友名字的消息
				if(mess.getMessageType().equals(Message.REQUEST_ONLINE_FRIEND)) {
					System.out.println("服务器收到 "+sender+" 发送来的请求获得在线好友名字的消息");

					//实验七：激活在线好友图标，步骤5：服务器要拿到在线好友名字
					Set onlineFriendSet=YychatServer.hmSocket.keySet();
					Iterator it=onlineFriendSet.iterator();//创建迭代器对象
					String onLineFriend="";
					while(it.hasNext()) {
						   String friendName=(String)it.next();
						  if(!(friendName.equals(sender))) onLineFriend=" "+friendName+onLineFriend;
					}
					System.out.println(sender+"在线好友有："+onLineFriend);

					//实验七：激活在线好友图标，步骤6：把在线好友名字发送到客户端
					mess.setOnLineFriend(onLineFriend);
					mess.setMessageType(Message.RESPONSE_ONLINE_FRIEND);
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(mess);
 				}


				if(mess.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
					String receiver=mess.getReceiver();

					//实验十三：保存聊天信息到message表中，步骤2：在服务器端设置发送聊天信息的时间
					mess.setSendTime(new java.util.Date());

					//实验十三：保存聊天信息到message表中，步骤3：保存聊天信息在数据库message表中
					DBUtil.saveMessage(sender,receiver,mess.getChatContent());

					System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getChatContent());
					//实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤1：怎么找到接收对应的Socket对象
					//利用前面保存的好的用户名和对应的socket对象，取出接收者的socket对象。
					Socket receiverSocket=(Socket)YychatServer.hmSocket.get(receiver);
					System.out.println("接收者名字:"+receiver);

					//实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤4：服务器转发聊天信息到接收方。
					ObjectOutputStream oos=new ObjectOutputStream(receiverSocket.getOutputStream());
					oos.writeObject(mess);
				}
		   }

	   } catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
	   }
   }


	//实验八：激活新上线好友图标，步骤7：新增sendMessage()方法
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
