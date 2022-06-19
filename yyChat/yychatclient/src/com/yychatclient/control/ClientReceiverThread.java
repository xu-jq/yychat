package com.yychatclient.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.yychat.model.Message;
import com.yychatclient.view.ClientLogin;
import com.yychatclient.view.FriendChat;
import com.yychatclient.view.FriendList;

//ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������6�������ͻ��˵Ľ������߳���

public class ClientReceiverThread extends Thread{
	Socket s;
	public ClientReceiverThread(Socket s) {
		  this.s=s;
	}
	public void run() {
     //ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������7�����շ�����ת����������Ϣ
    ObjectInputStream ois;
	try {
		while(true) {
			ois = new ObjectInputStream(s.getInputStream());
			Message mess=(Message)ois.readObject();
			String sender=mess.getSender();
			String receiver=mess.getReceiver();



			//��������λİ���� ClientReceiverThread��
			if (mess.getMessageType().equals(Message.MOVEE_STRANGER_SUCCESS)){
				JOptionPane.showMessageDialog(null,"�Ƴ�������İ���˳ɹ���");
				//��Ӻ��Ѻ͸��º����б����º���ͼ��
				FriendList friendList = ClientLogin.hmFriendList.get(sender);
				String allFriend = mess.getChatContent();
				String strangerList = mess.getChatContent2();
				friendList.updateFriendList(allFriend);
				friendList.updateStrangerList(strangerList);
			}

			if(mess.getMessageType().equals(Message.MOVE_STRANGER_FAILURE))
			{
				JOptionPane.showMessageDialog(null,"û�и��û�,�Ƴ�ʧ�ܣ�");
			}

			if(mess.getMessageType().equals(Message.MOVE_FRIEND_FAILURE_NO_USER))
			{
				JOptionPane.showMessageDialog(null,"�����Ǻ���,�Ƴ�ʧ�ܣ�");
			}

			if(mess.getMessageType().equals(Message.OFFLINE_FRIEND)){
				System.out.println(sender+"�����ˣ�׼����������ͼ��");
				//
				FriendList friendList = ClientLogin.hmFriendList.get(receiver);
				friendList.setEnabledOfflineFriendIcon(sender);
			}

			//�ڿͻ��˴����û��˳��Ĵ���
			if(mess.getMessageType().equals(Message.USER_EXIT_CLIENT_THREAD_CLOSE)){
				System.out.println("�ر�" + mess.getSender() + "�û������߳�");
				s.close();
				break;
			}

			if(mess.getMessageType().equals(Message.UPDATE_PASSWORD_SUCCESS)){
				JOptionPane.showMessageDialog(null,"�޸ĳɹ���");
			}


			//����
			if(mess.getMessageType().equals(Message.SHAKE))
			{
				System.out.println(mess.getSender()+"��"+mess.getReceiver()+"���Ͷ�������");
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
			//ɾ������ ClientReceiverThread��
            if (mess.getMessageType().equals(Message.DELETE_FRIEND_SUCCESS)){
                JOptionPane.showMessageDialog(null,"ɾ�����ѳɹ���");
                //��Ӻ��Ѻ͸��º����б����º���ͼ��
                FriendList friendList = ClientLogin.hmFriendList.get(sender);
                String allFriend = mess.getChatContent();
				String strangerList = mess.getChatContent2();
				friendList.updateStrangerList(strangerList);
				friendList.updateFriendList(allFriend);
            }

            if(mess.getMessageType().equals(Message.DELETE_FRIEND_FAILURE_NO_USER))
            {
            	 JOptionPane.showMessageDialog(null,"û�и��û�,ɾ��ʧ�ܣ�");
            }

            if(mess.getMessageType().equals(Message.Delete_NEW_FRIEND_FAILURE_ALREADY_FRIEND))
            {
            	 JOptionPane.showMessageDialog(null,"�����Ǻ���,ɾ��ʧ�ܣ�");
            }
			//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����8���ڿͻ�����������Ӻ����Ƿ�ɹ��Ĵ���
			if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_NO_USER)) {
				JOptionPane.showMessageDialog(null,"���û������ڣ���Ӻ���ʧ�ܣ�");
			}

			if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND)) {
				JOptionPane.showMessageDialog(null,"���û��Ѿ��Ǻ��ѣ��벻Ҫ�ظ���ӣ�");
			}

			if(mess.getMessageType().equals(Message.ADD_NEW_FRIEND_SUCCESS)) {
				JOptionPane.showMessageDialog(null,"����º��ѳɹ���");
				//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����9�����º����б�ͼ��
				FriendList friendList=ClientLogin.hmFriendList.get(sender);
				String allFriend=mess.getChatContent();
				friendList.updateFriendList(allFriend);
			}

			//ʵ��ˣ����������ߺ���ͼ�꣬����8�����������ߺ��ѵ�����
			if(mess.getMessageType().equals(Message.NEW_ONLINE_FRIEND)) {
				System.out.println(sender+"�����ˣ�׼����������ͼ��");
				//ʵ��ˣ����������ߺ���ͼ�꣬����9����receiver��FriendList�������漤��sender��ͼ��
				FriendList friendList=ClientLogin.hmFriendList.get(receiver);
				friendList.setEnabledNewOnlineFriendIcon(sender);
			}

			//ʵ���ߣ��������ߺ���ͼ�꣬����8�����շ����������������ߺ�������
			if(mess.getMessageType().equals(Message.RESPONSE_ONLINE_FRIEND)){
				System.out.println(sender+"���ߺ��ѵ����֣�"+mess.getOnLineFriend());
				System.out.println("�������ߺ�������");
				System.out.println(mess.getOnLineFriend());
				//ʵ���ߣ��������ߺ���ͼ�꣬����9������Ҫ�õ�sender�ĺ����б���󣬼������ߺ���ͼ��
				FriendList friendList=ClientLogin.hmFriendList.get(sender);
			    System.out.println("friendListΪ��"+friendList);
				friendList.setEnabledOnlineFriendIcon(mess.getOnLineFriend());
//				friendList.setOnlineFriendList(mess.getOnLineFriend());
			}

			if(mess.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
				System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getChatContent());

				//ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������8���ڽ��շ��ĺ������������ʾ������ת������������Ϣ
				//����õ����շ��ĺ�������������
				//String sender=mess.getSender();
				//String receiver=mess.getReceiver();
				FriendChat friendChat=FriendList.hmFriendChat.get(receiver+"to"+sender);
				//receiver��sender��˳���ܴ�

				//ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������11:��mess��friendChat����ʾ����
				friendChat.append(mess);
			}
		}
	} catch (IOException | ClassNotFoundException e) {
		e.printStackTrace();
	}


	}
}
