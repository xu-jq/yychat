package com.yychatclient.view;
import javax.sound.midi.Receiver;
import javax.swing.*;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.OnlineUserListModel;
import com.yychat.model.User;
import com.yychatclient.control.YychatClientConnection;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterGraphics;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//���̳�
public class FriendList extends JFrame implements WindowListener, ActionListener, MouseListener{//1.��һ��:ʵ��ActionListener�ӿ�

		public static List<User> onlineUsers = new ArrayList<>();

		//2.��һ��:�����MouseListener
		//ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������9������HashMap������������FriendChat����
		public static HashMap<String,FriendChat> hmFriendChat=new HashMap();

		//�����һ�ſ�Ƭ
		JPanel myFriendPanel;
		//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����2��������Ӻ��ѵİ�ť��������Ϊ��Ա�������������������з��ʡ�
		JPanel addFriendJPanel;
		JButton addFriendJButton;
		JButton myFriendJButton;//myFriendJButtonΪ��һ�ſ�Ƭ�����б�İ�ť;

		//�в�
		JScrollPane jScrollPane;//���������
		JPanel myFriendListJPanel;
		final int friendCount=50;//����
		JLabel myFriendJLabel[]=new JLabel[friendCount];//�����������飬50������Ԫ��

		//�����ϲ����
		JButton strangerJButton1;//İ����stranger��ť
		JButton blackListJButton;//��������ť
		JButton deleteFriend;
		JButton updatePasswordButton;//�޸����밴ť

		JPanel stranger_blackList_JPanel;

		//******************************************************************************//

		//����ڶ��ſ�Ƭ
		JPanel myStrangerPanel;
		JPanel friend_Stranger_Panel;
		JButton myFriendJButton1;//�ڶ��ſ�Ƭ�������ֱ�����ͬȡ��Ϊ myFriendJButton1 Ϊ�ڶ��ſ�Ƭ�����б�İ�ť;
		JButton strangerJButton;//�ڶ��ſ�Ƭ��İ�����б�ťȡ��Ϊ strangerJButton;

		//�в�
		JScrollPane strangerJScrollPane;//���������
		JPanel strangerListJPanel;
		final int STRANGERDCOUNT=20;//����
		JLabel strangerJLabel[]=new JLabel[STRANGERDCOUNT];//�����������飬20������Ԫ��

		//�����ϲ����
		JButton blackListJButton1;
		JButton deleteFriend1;
		JPanel blackListJButton1_deleteFriend1;
		JButton moveFriendButton;

		CardLayout cardLayout;

		String userName;//��Ա����

		public static String allFriend;

		//ʵ��ʮ���������ݿ�userrelation�������º����б�����6���޸�FriendList�Ĺ��췽��
		public FriendList(String userName,String allFriend,String strangerList){

			//public FriendList(String userName){
			this.userName=userName;

			//������һ�ſ�Ƭ���ҵĺ��ѿ�Ƭ��
			myFriendPanel=new JPanel(new BorderLayout());//Ĭ�ϵ�����ʽ����FlowLayout
			//ʵ�ֺ����б�ı���

			//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����1��������Ӻ��ѵİ�ť����ע�ᶯ����������
			addFriendJPanel=new JPanel(new GridLayout(2,1));//���񲼾�
			addFriendJButton=new JButton("��Ӻ���");
			addFriendJButton.addActionListener(this);
			myFriendJButton=new JButton("�ҵĺ���");
			addFriendJPanel.add(addFriendJButton);
			addFriendJPanel.add(myFriendJButton);
			myFriendPanel.add(addFriendJPanel,"North");

			//myFriendPanel.add(myFriendJButton,"North");

			//ʵ��ʮ���������ݿ�userrelation�������º����б�����7������allFriend�����º���ͼ��
			//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����10����ȡ�������ɷ���
			myFriendListJPanel=new JPanel();
			updateFriendList(allFriend);


			System.out.println("�����б�" + this.allFriend);

			/*//ʵ�ֺ��ѿ�Ƭ���в�
			myFriendListJPanel=new JPanel(new GridLayout(MYFRIENDCOUNT,1));//���񲼾�ȥ����50������
			for(int i=1;i<MYFRIENDCOUNT;i++){
			//myFriendJLabel[i]=new JLabel(i+"�ź���",new ImageIcon("src/images/duck.gif"),JLabel.LEFT);
			myFriendJLabel[i]=new JLabel(i+"",new ImageIcon("src/images/duck.gif"),JLabel.LEFT);

				//ʵ���ߣ��������ߺ���ͼ�꣬����1���ѳ��û��Լ�֮���ͼ������Ϊ�Ǽ���״̬
				//if(Integer.valueOf(userName)!=i) myFriendJLabel[i].setEnabled(false);

			myFriendJLabel[i].addMouseListener(this);//2.�ڶ���
			myFriendListJPanel.add(myFriendJLabel[i]);
			}  */

			jScrollPane=new JScrollPane(myFriendListJPanel);
			myFriendPanel.add(jScrollPane,"Center");

			//ʵ�ֺ��ѿ�Ƭ���ϲ�
			strangerJButton1=new JButton("İ����");
			strangerJButton1.addActionListener(this);//1.�ڶ���:1.ע��Action�����¼�
			blackListJButton=new JButton("������");
			deleteFriend = new JButton("ɾ������");
			deleteFriend.addActionListener(this);
			updatePasswordButton = new JButton("�޸�����");
			updatePasswordButton.addActionListener(this);
			moveFriendButton = new JButton("�ƶ�����");
			moveFriendButton.addActionListener(this);

			stranger_blackList_JPanel=new JPanel(new GridLayout(3,1));
			stranger_blackList_JPanel.add(strangerJButton1);
			stranger_blackList_JPanel.add(blackListJButton);
			stranger_blackList_JPanel.add(deleteFriend);
			stranger_blackList_JPanel.add(updatePasswordButton);
			stranger_blackList_JPanel.add(moveFriendButton);
			myFriendPanel.add(stranger_blackList_JPanel,"South");

			//************************************************************************************//

			//�����ڶ��ſ�Ƭ���ҵĺ�������Ƭ��
			myStrangerPanel=new JPanel(new BorderLayout());
			//ʵ�ֺ������б�ı���
			friend_Stranger_Panel=new JPanel(new GridLayout(2,1));//�õ������񲼾�GridLayout
			myFriendJButton1=new JButton("�ҵĺ���");
			myFriendJButton1.addActionListener(this);//�ڶ���:2.ע��Action�����¼�
			strangerJButton=new JButton("İ����");
			friend_Stranger_Panel.add(myFriendJButton1);
			friend_Stranger_Panel.add(strangerJButton);
			myStrangerPanel.add(friend_Stranger_Panel,"North");

			strangerListJPanel=new JPanel();
			updateStrangerList(strangerList);
			//ʵ�ֺ������б���в�
//			strangerListJPanel=new JPanel(new GridLayout(STRANGERDCOUNT,1));
//			for (int i=0;i<STRANGERDCOUNT;i++){
//				strangerJLabel[i]=new JLabel(i+"�ź���",new ImageIcon("yychatclient/src/images/tortoise.gif"),JLabel.LEFT);
//				strangerListJPanel.add(strangerJLabel[i]);
//			}
			strangerJScrollPane=new JScrollPane(strangerListJPanel);
			myStrangerPanel.add(strangerJScrollPane,"Center");



			//ʵ�ֺ������б���ϲ�
			blackListJButton1=new JButton("������");
			deleteFriend1 = new JButton("ɾ������");
			deleteFriend1.addActionListener(this);

			blackListJButton1_deleteFriend1 = new JPanel(new GridLayout(2,1));
			blackListJButton1_deleteFriend1.add(blackListJButton);
			blackListJButton1_deleteFriend1.add(deleteFriend1);

			myStrangerPanel.add(blackListJButton1_deleteFriend1,"South");


			cardLayout=new CardLayout();
			this.setLayout(cardLayout);
			this.add(myFriendPanel,"1");
			this.add(myStrangerPanel,"2");
			cardLayout.show(this.getContentPane(),"1");
			//cardLayout.show(this.getContentPane(),"2");
			this.setSize(200,500);
			this.setTitle(userName+"�ĺ����б�");
			this.setIconImage(new ImageIcon("yyChat/yychatclient/src/images/duck2.gif").getImage());
//			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);//λ�ù�ϵΪ�յĻ�����˵�ÿ�Ƭ��������Ļ�����м�

			this.addWindowListener(this);

			this.setVisible(true);
		}

		public void updateStrangerList(String stranger){
			System.out.println("����İ�����б����");
			strangerListJPanel.removeAll();
			String friendName[]=stranger.split(" ");
			int friendCount=friendName.length;

//			String onlineName[]=onlineFriend.split(" ");
//			HashMap<String, Integer> onlineMap = new HashMap<>();
//			for (int i = 0;i < onlineName.length;i++){
//				onlineMap.put(onlineName[i],1);
//			}



			strangerListJPanel.setLayout(new GridLayout(friendCount,1));//���񲼾�ȥ����50������
			List<User> users = new ArrayList<>();
			for(int i=1;i<friendCount;i++){
				System.out.println(friendName[i]);
//				User user = new User();
//				user.setUserName(friendName[i]);
//				OnlineUserListModel.onlineUsers.add(user);

				strangerJLabel[i]=new JLabel(friendName[i],new ImageIcon("yyChat/yychatclient/src/images/tortoise.gif"),JLabel.LEFT);

//				if (!friendName[i].equals(userName)){
//					myFriendJLabel[i].setEnabled(false);
//				}

				strangerJLabel[i].addMouseListener(this);
				strangerListJPanel.add(strangerJLabel[i]);
			}


			strangerListJPanel.repaint();
			strangerListJPanel.revalidate();
		}

		//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����11���޸�updateFriendList����
		public void updateFriendList(String allFriend) {
			System.out.println("���º����б����");
			System.out.println(allFriend);

			this.allFriend = allFriend;
			myFriendListJPanel.removeAll();
			String friendName[]=allFriend.split(" ");
		    int friendCount=friendName.length;

//			String onlineName[]=onlineFriend.split(" ");
//			HashMap<String, Integer> onlineMap = new HashMap<>();
//			for (int i = 0;i < onlineName.length;i++){
//				onlineMap.put(onlineName[i],1);
//			}

			myFriendListJPanel.setLayout(new GridLayout(friendCount,1));//���񲼾�ȥ����50������
			List<User> users = new ArrayList<>();
			for(int i=1;i<friendCount;i++){
//				User user = new User();
//				user.setUserName(friendName[i]);
//				OnlineUserListModel.onlineUsers.add(user);

				myFriendJLabel[i]=new JLabel(friendName[i],new ImageIcon("yyChat/yychatclient/src/images/duck.gif"),JLabel.LEFT);

				if (!friendName[i].equals(userName)){
					myFriendJLabel[i].setEnabled(false);
				}

				myFriendJLabel[i].addMouseListener(this);
				myFriendListJPanel.add(myFriendJLabel[i]);
		    }


			myFriendListJPanel.repaint();
			myFriendListJPanel.revalidate();
		}
		public static void main(String[] args) {
		//FriendList friendList=new FriendList();
		}

		 //ʵ��ˣ����������ߺ���ͼ�꣬����10���������������ߺ���ͼ�귽��
		 public void setEnabledNewOnlineFriendIcon(String newOnlineFriend){
			//myFriendJLabel[Integer.valueOf(newOnlineFriend)].setEnabled(true);
			 String friendName[] = allFriend.split(" ");
			 int count=friendName.length;
			 System.out.println("�����б�");
			 System.out.println("����" + count);
			 for(int i=1;i<count;i++){
				 System.out.println(myFriendJLabel[i]);

				 //System.out.println("����������");

				 if (myFriendJLabel[i].getText().equals(newOnlineFriend)){
					 System.out.println(friendName[i]);
					 System.out.println("����" + i);
					 myFriendJLabel[i].setEnabled(true);
				 }
			 }
		  }

		//ʵ���ߣ��������ߺ���ͼ�꣬����12����Ӽ������ߺ���ͼ�귽��
		public void setEnabledOnlineFriendIcon(String onlineFriend) {
			System.out.println("�������ߺ���ͼ��");

			if(onlineFriend==null)
			{
				System.out.println("null");
			}
				String friendName[] = allFriend.split(" ");

			String onlineName[]=onlineFriend.split(" ");
			HashMap<String, Integer> onlineMap = new HashMap<>();
			for (int i = 0;i < onlineName.length;i++){
				onlineMap.put(onlineName[i],1);
			}
			int count=friendName.length;
			System.out.println("�����б�");
			System.out.println("����" + count);
			for(int i=1;i<count;i++){
				System.out.println(myFriendJLabel[i]);

				//System.out.println("����������");

				if (onlineMap.get(myFriendJLabel[i].getText()) != null){
					System.out.println(friendName[i]);
					System.out.println("����" + i);
					myFriendJLabel[i].setEnabled(true);
				}
			}
		}

	   public void setEnabledOfflineFriendIcon(String offlineFriend) {
		   //myFriendJLabel[Integer.valueOf(newOnlineFriend)].setEnabled(true);
		   String friendName[] = allFriend.split(" ");
		   int count=friendName.length;
		   System.out.println("�����б�");
		   System.out.println("����" + count);
		   for(int i=1;i<count;i++){
			   System.out.println(myFriendJLabel[i]);
			   //System.out.println("����������");

			   if (myFriendJLabel[i].getText().equals(offlineFriend)){
				   System.out.println(friendName[i]);
				   System.out.println("����" + i);
				   myFriendJLabel[i].setEnabled(false);
			   }
		   }
	   }

	   public void setOnlineFriendList(String onLineFriend) {
		   String onlineName[]=onLineFriend.split(" ");
		   for (int i = 0;i < onlineName.length;i++){
			   User user = new User();
			   user.setUserName(onLineFriend);
			   FriendList.onlineUsers.add(user);
		   }
	   }


		@Override
		public void actionPerformed(ActionEvent e) {//������:ʵ�ֶ�����Ӧ����

			if (e.getSource() == moveFriendButton){
				String moveFriend = JOptionPane.showInputDialog("������Ҫ�ƶ���İ���˵ĺ���������");
				Message mess = new Message();
				mess.setSender(userName);
				mess.setReceiver("Server");
				mess.setChatContent(moveFriend);
				mess.setMessageType(Message.MOVEE_STRANGER);

				OutputStream os;
				try {
					os = YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(mess);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}

			if (e.getSource() == updatePasswordButton){
				String latterPassword = JOptionPane.showInputDialog("�����������룺");
				Message mess = new Message();
				mess.setSender(userName);
				mess.setReceiver("Server");
				mess.setChatContent(latterPassword);
				mess.setMessageType(Message.UPDATE_PASSWORD);

				OutputStream os;
				try {
					os = YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(mess);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}

			}

			//ɾ������ FriendList��
			if (e.getSource() == deleteFriend || e.getSource() == deleteFriend1){
				String FriendName = JOptionPane.showInputDialog("��������ѵ����֣�");
				Message mess = new Message();
				mess.setSender(userName);
				mess.setReceiver("Server");
				mess.setChatContent(FriendName);
				mess.setMessageType(Message.DELETE_FRIEND);

				OutputStream os;
				try {
					os = YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(mess);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}


				mess=new Message();
				mess.setSender(userName);
				mess.setReceiver("Server");
				mess.setMessageType(Message.REQUEST_ONLINE_FRIEND);
				try {
					os = YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(mess);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}


			//ʵ��ʮ������Ӻ��Ѻ͸��º����б�����3�����Ӵ�����Ӻ��ѵİ�ť�¼����룬�Ӽ��������º��ѵ����֣����ҷ��͵���������
			if(e.getSource()==addFriendJButton) {
				String newFriendName=JOptionPane.showInputDialog("�������º��ѵ����֣�");
				Message mess=new Message();
				mess.setSender(userName);
				mess.setReceiver("Server");
				mess.setChatContent(newFriendName);
				mess.setMessageType(Message.ADD_NEW_FRIEND);
				OutputStream os;
				try {
					os = YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos=new ObjectOutputStream(os);
					oos.writeObject(mess);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				mess=new Message();
				mess.setSender(userName);
				mess.setReceiver("Server");
				mess.setMessageType(Message.REQUEST_ONLINE_FRIEND);
				try {
					os = YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(mess);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
			if(e.getSource()==strangerJButton1) cardLayout.show(this.getContentPane(),"2");
			if(e.getSource()==myFriendJButton1) cardLayout.show(this.getContentPane(),"1");
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			   if (e.getClickCount()==2){//�������������2
			   		JLabel jLabel=(JLabel) e.getSource();
					String receiver=jLabel.getText();
				 	//new FriendChat(userName,receiver);
				    //ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������10������FriendChat����
					FriendChat friendChat = new FriendChat(userName,receiver);
			   		hmFriendChat.put(userName+"to"+receiver, friendChat);
				}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		JLabel jLabel=(JLabel) e.getSource();
		jLabel.setForeground(Color.red);
		}

		@Override
		public void mouseExited(MouseEvent e) {
		JLabel jLabel=(JLabel) e.getSource();
		jLabel.setForeground(Color.blue);
		}
		@Override
		public void mousePressed(MouseEvent e) {

		}
		@Override
		public void mouseReleased(MouseEvent e) {

		}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.out.println(userName + "׼���رտͻ���");

		//����������͹رտͻ�����Ϣ
		Message mess = new Message();
		mess.setSender(userName);
		mess.setReceiver("Server");
		ObjectOutputStream oos;

		mess.setMessageType(MessageType.OFFLINE_FRIEND_TO_SERVER);
		try {
			oos = new ObjectOutputStream(YychatClientConnection.s.getOutputStream());
			oos.writeObject(mess);//�������������Ϣ
		}catch (IOException e){
			e.printStackTrace();
		}

		mess.setMessageType(MessageType.USER_EXIT_SERVER_THREAD_CLOSE);
		try {
			oos = new ObjectOutputStream(YychatClientConnection.s.getOutputStream());
			oos.writeObject(mess);//�������������Ϣ
		}catch (IOException e){
			e.printStackTrace();
		}



		System.exit(0);

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}


}
