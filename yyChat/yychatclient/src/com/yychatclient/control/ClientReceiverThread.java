package com.yychatclient.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.yychat.model.Message;
import com.yychatclient.view.ClientLogin;
import com.yychatclient.view.FriendChat;
import com.yychatclient.view.FriendList;

//实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤6：创建客户端的接收者线程类

public class ClientReceiverThread extends Thread{
	Socket s;
	public ClientReceiverThread(Socket s) {
		  this.s=s;
	}
	public void run() {
     //实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤7：接收服务器转发过来的信息
    ObjectInputStream ois;
	try {
		while(true) {
			ois = new ObjectInputStream(s.getInputStream());
			Message mess=(Message)ois.readObject();
			String sender=mess.getSender();
			String receiver=mess.getReceiver();



			//将好友移位陌生人 ClientReceiverThread类
			if (mess.getMessageType().equals(Message.MOVEE_STRANGER_SUCCESS)){
				JOptionPane.showMessageDialog(null,"移除好友至陌生人成功！");
				//添加好友和更新好友列表，更新好友图标
				FriendList friendList = ClientLogin.hmFriendList.get(sender);
				String allFriend = mess.getChatContent();
				String strangerList = mess.getChatContent2();
				friendList.updateFriendList(allFriend);
				friendList.updateStrangerList(strangerList);
			}

			if(mess.getMessageType().equals(Message.MOVE_STRANGER_FAILURE))
			{
				JOptionPane.showMessageDialog(null,"没有该用户,移除失败！");
			}

			if(mess.getMessageType().equals(Message.MOVE_FRIEND_FAILURE_NO_USER))
			{
				JOptionPane.showMessageDialog(null,"还不是好友,移除失败！");
			}

			if(mess.getMessageType().equals(Message.OFFLINE_FRIEND)){
				System.out.println(sender+"下线了，准备下线他的图标");
				//
				FriendList friendList = ClientLogin.hmFriendList.get(receiver);
				friendList.setEnabledOfflineFriendIcon(sender);
			}

			//在客户端处理用户退出的代码
			if(mess.getMessageType().equals(Message.USER_EXIT_CLIENT_THREAD_CLOSE)){
				System.out.println("关闭" + mess.getSender() + "用户接收线程");
				s.close();
				break;
			}

			if(mess.getMessageType().equals(Message.UPDATE_PASSWORD_SUCCESS)){
				JOptionPane.showMessageDialog(null,"修改成功！");
			}


			//抖动
			if(mess.getMessageType().equals(Message.SHAKE))
			{
				System.out.println(mess.getSender()+"对"+mess.getReceiver()+"发送抖动功能");
				FriendChat friendChat= FriendList.hmFriendChat.get(receiver+"to"+sender);

				  int x = friendChat.getX();
                  int y = friendChat.getY();
                  for (int i = 0; i < 20; i++) {
                      if ((i & 1) == 0) {
                          x += 3;
                          y += 3;
                      } else {
                          x -= 3;
                          y -= 3;
                      }
                      friendChat.setLocation(x, y);
                      try {
                          Thread.sleep(50);
                      } catch (InterruptedException e1) {
                          e1.printStackTrace();
                      }
                  }
			}
			//删除好友 ClientReceiverThread类
            if (mess.getMessageType().equals(Message.DELETE_FRIEND_SUCCESS)){
                JOptionPane.showMessageDialog(null,"删除好友成功！");
                //添加好友和更新好友列表，更新好友图标
                FriendList friendList = ClientLogin.hmFriendList.get(sender);
                String allFriend = mess.getChatContent();
				String strangerList = mess.getChatContent2();
				friendList.updateStrangerList(strangerList);
				friendList.updateFriendList(allFriend);
            }

            if(mess.getMessageType().equals(Message.DELETE_FRIEND_FAILURE_NO_USER))
            {
            	 JOptionPane.showMessageDialog(null,"没有该用户,删除失败！");
            }

            if(mess.getMessageType().equals(Message.Delete_NEW_FRIEND_FAILURE_ALREADY_FRIEND))
            {
            	 JOptionPane.showMessageDialog(null,"还不是好友,删除失败！");
            }
			//实验十二：添加好友和更新好友列表，步骤8：在客户端来处理添加好友是否成功的代码
			if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_NO_USER)) {
				JOptionPane.showMessageDialog(null,"该用户不存在，添加好友失败！");
			}

			if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND)) {
				JOptionPane.showMessageDialog(null,"该用户已经是好友，请不要重复添加！");
			}

			if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND_SUCCESS)) {
				JOptionPane.showMessageDialog(null,"添加新好友成功！");
				//实验十二：添加好友和更新好友列表，步骤9：更新好友列表图标
				FriendList friendList=ClientLogin.hmFriendList.get(sender);
				String allFriend=mess.getChatContent();
				friendList.updateFriendList(allFriend);
			}

			//实验八：激活新上线好友图标，步骤8：接收新上线好友的名字
			if(mess.getMessageType().equals(Message.NEW_ONLINE_FRIEND)) {
				System.out.println(sender+"上线了，准备激活他的图标");
				//实验八：激活新上线好友图标，步骤9：在receiver的FriendList对象里面激活sender的图标
				FriendList friendList=ClientLogin.hmFriendList.get(receiver);
				friendList.setEnabledNewOnlineFriendIcon(sender);
			}

			//实验七：激活在线好友图标，步骤8：接收服务器发送来的在线好友名字
			if(mess.getMessageType().equals(Message.RESPONSE_ONLINE_FRIEND)){
				System.out.println(sender+"在线好友的名字："+mess.getOnLineFriend());
				System.out.println("激活在线好友名字");
				System.out.println(mess.getOnLineFriend());
				//实验七：激活在线好友图标，步骤9：首先要拿到sender的好友列表对象，激活在线好友图标
				FriendList friendList=ClientLogin.hmFriendList.get(sender);
			    System.out.println("friendList为："+friendList);
				friendList.setEnabledOnlineFriendIcon(mess.getOnLineFriend());
//				friendList.setOnlineFriendList(mess.getOnLineFriend());
			}

			if(mess.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
				System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getChatContent());

				//实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤8：在接收方的好友聊天界面显示服务器转发来的聊天信息
				//如何拿到接收方的好友聊天界面对象？
				//String sender=mess.getSender();
				//String receiver=mess.getReceiver();
				FriendChat friendChat=FriendList.hmFriendChat.get(receiver+"to"+sender);
				//receiver和sender的顺序不能错

				//实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤11:把mess在friendChat上显示处理
				friendChat.append(mess);
			}
		}
	} catch (IOException | ClassNotFoundException e) {
		e.printStackTrace();
	}


	}
}
