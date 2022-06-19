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

//单继承
public class FriendList extends JFrame implements WindowListener, ActionListener, MouseListener{//1.第一步:实现ActionListener接口

		public static List<User> onlineUsers = new ArrayList<>();

		//2.第一步:鼠标点击MouseListener
		//实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤9：创建HashMap对象，用来保存FriendChat对象
		public static HashMap<String,FriendChat> hmFriendChat=new HashMap();

		//定义第一张卡片
		JPanel myFriendPanel;
		//实验十二：添加好友和更新好友列表，步骤2：定义添加好友的按钮变量，作为成员变量方便在其他方法中访问。
		JPanel addFriendJPanel;
		JButton addFriendJButton;
		JButton myFriendJButton;//myFriendJButton为第一张卡片好友列表的按钮;

		//中部
		JScrollPane jScrollPane;//滚动条面板
		JPanel myFriendListJPanel;
		final int friendCount=50;//常量
		JLabel myFriendJLabel[]=new JLabel[friendCount];//创建对象数组，50个数组元素

		//定义南部组件
		JButton strangerJButton1;//陌生人stranger按钮
		JButton blackListJButton;//黑名单按钮
		JButton deleteFriend;
		JButton updatePasswordButton;//修改密码按钮

		JPanel stranger_blackList_JPanel;

		//******************************************************************************//

		//定义第二张卡片
		JPanel myStrangerPanel;
		JPanel friend_Stranger_Panel;
		JButton myFriendJButton1;//第二张卡片定义名字避免相同取名为 myFriendJButton1 为第二张卡片好友列表的按钮;
		JButton strangerJButton;//第二张卡片中陌生人列表按钮取名为 strangerJButton;

		//中部
		JScrollPane strangerJScrollPane;//滚动条面板
		JPanel strangerListJPanel;
		final int STRANGERDCOUNT=20;//常量
		JLabel strangerJLabel[]=new JLabel[STRANGERDCOUNT];//创建对象数组，20个数组元素

		//定义南部组件
		JButton blackListJButton1;
		JButton deleteFriend1;
		JPanel blackListJButton1_deleteFriend1;
		JButton moveFriendButton;

		CardLayout cardLayout;

		String userName;//成员变量

		public static String allFriend;

		//实验十：利用数据库userrelation表来更新好友列表，步骤6：修改FriendList的构造方法
		public FriendList(String userName,String allFriend,String strangerList){

			//public FriendList(String userName){
			this.userName=userName;

			//创建第一张卡片（我的好友卡片）
			myFriendPanel=new JPanel(new BorderLayout());//默认的是流式布局FlowLayout
			//实现好友列表的北部

			//实验十二：添加好友和更新好友列表，步骤1：增加添加好友的按钮，并注册动作监听器。
			addFriendJPanel=new JPanel(new GridLayout(2,1));//网格布局
			addFriendJButton=new JButton("添加好友");
			addFriendJButton.addActionListener(this);
			myFriendJButton=new JButton("我的好友");
			addFriendJPanel.add(addFriendJButton);
			addFriendJPanel.add(myFriendJButton);
			myFriendPanel.add(addFriendJPanel,"North");

			//myFriendPanel.add(myFriendJButton,"North");

			//实验十：利用数据库userrelation表来更新好友列表，步骤7：利用allFriend来更新好友图标
			//实验十二：添加好友和更新好友列表，步骤10：提取代码生成方法
			myFriendListJPanel=new JPanel();
			updateFriendList(allFriend);


			System.out.println("好友列表" + this.allFriend);

			/*//实现好友卡片的中部
			myFriendListJPanel=new JPanel(new GridLayout(MYFRIENDCOUNT,1));//网格布局去管理50个好友
			for(int i=1;i<MYFRIENDCOUNT;i++){
			//myFriendJLabel[i]=new JLabel(i+"号好友",new ImageIcon("src/images/duck.gif"),JLabel.LEFT);
			myFriendJLabel[i]=new JLabel(i+"",new ImageIcon("src/images/duck.gif"),JLabel.LEFT);

				//实验七：激活在线好友图标，步骤1：把除用户自己之外的图标设置为非激活状态
				//if(Integer.valueOf(userName)!=i) myFriendJLabel[i].setEnabled(false);

			myFriendJLabel[i].addMouseListener(this);//2.第二步
			myFriendListJPanel.add(myFriendJLabel[i]);
			}  */

			jScrollPane=new JScrollPane(myFriendListJPanel);
			myFriendPanel.add(jScrollPane,"Center");

			//实现好友卡片的南部
			strangerJButton1=new JButton("陌生人");
			strangerJButton1.addActionListener(this);//1.第二步:1.注册Action动作事件
			blackListJButton=new JButton("黑名单");
			deleteFriend = new JButton("删除好友");
			deleteFriend.addActionListener(this);
			updatePasswordButton = new JButton("修改密码");
			updatePasswordButton.addActionListener(this);
			moveFriendButton = new JButton("移动好友");
			moveFriendButton.addActionListener(this);

			stranger_blackList_JPanel=new JPanel(new GridLayout(3,1));
			stranger_blackList_JPanel.add(strangerJButton1);
			stranger_blackList_JPanel.add(blackListJButton);
			stranger_blackList_JPanel.add(deleteFriend);
			stranger_blackList_JPanel.add(updatePasswordButton);
			stranger_blackList_JPanel.add(moveFriendButton);
			myFriendPanel.add(stranger_blackList_JPanel,"South");

			//************************************************************************************//

			//创建第二张卡片（我的黑名单卡片）
			myStrangerPanel=new JPanel(new BorderLayout());
			//实现黑名单列表的北部
			friend_Stranger_Panel=new JPanel(new GridLayout(2,1));//用的是网格布局GridLayout
			myFriendJButton1=new JButton("我的好友");
			myFriendJButton1.addActionListener(this);//第二步:2.注册Action动作事件
			strangerJButton=new JButton("陌生人");
			friend_Stranger_Panel.add(myFriendJButton1);
			friend_Stranger_Panel.add(strangerJButton);
			myStrangerPanel.add(friend_Stranger_Panel,"North");

			strangerListJPanel=new JPanel();
			updateStrangerList(strangerList);
			//实现黑名单列表的中部
//			strangerListJPanel=new JPanel(new GridLayout(STRANGERDCOUNT,1));
//			for (int i=0;i<STRANGERDCOUNT;i++){
//				strangerJLabel[i]=new JLabel(i+"号好友",new ImageIcon("yychatclient/src/images/tortoise.gif"),JLabel.LEFT);
//				strangerListJPanel.add(strangerJLabel[i]);
//			}
			strangerJScrollPane=new JScrollPane(strangerListJPanel);
			myStrangerPanel.add(strangerJScrollPane,"Center");



			//实现黑名单列表的南部
			blackListJButton1=new JButton("黑名单");
			deleteFriend1 = new JButton("删除好友");
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
			this.setTitle(userName+"的好友列表");
			this.setIconImage(new ImageIcon("yyChat/yychatclient/src/images/duck2.gif").getImage());
//			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);//位置关系为空的话就是说让卡片生成在屏幕的正中间

			this.addWindowListener(this);

			this.setVisible(true);
		}

		public void updateStrangerList(String stranger){
			System.out.println("更新陌生人列表面板");
			strangerListJPanel.removeAll();
			String friendName[]=stranger.split(" ");
			int friendCount=friendName.length;

//			String onlineName[]=onlineFriend.split(" ");
//			HashMap<String, Integer> onlineMap = new HashMap<>();
//			for (int i = 0;i < onlineName.length;i++){
//				onlineMap.put(onlineName[i],1);
//			}



			strangerListJPanel.setLayout(new GridLayout(friendCount,1));//网格布局去管理50个好友
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

		//实验十二：添加好友和更新好友列表，步骤11：修改updateFriendList方法
		public void updateFriendList(String allFriend) {
			System.out.println("更新好友列表面板");
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

			myFriendListJPanel.setLayout(new GridLayout(friendCount,1));//网格布局去管理50个好友
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

		 //实验八：激活新上线好友图标，步骤10：新增激活新上线好友图标方法
		 public void setEnabledNewOnlineFriendIcon(String newOnlineFriend){
			//myFriendJLabel[Integer.valueOf(newOnlineFriend)].setEnabled(true);
			 String friendName[] = allFriend.split(" ");
			 int count=friendName.length;
			 System.out.println("好友列表");
			 System.out.println("数量" + count);
			 for(int i=1;i<count;i++){
				 System.out.println(myFriendJLabel[i]);

				 //System.out.println("激活退退退");

				 if (myFriendJLabel[i].getText().equals(newOnlineFriend)){
					 System.out.println(friendName[i]);
					 System.out.println("激活" + i);
					 myFriendJLabel[i].setEnabled(true);
				 }
			 }
		  }

		//实验七：激活在线好友图标，步骤12：添加激活在线好友图标方法
		public void setEnabledOnlineFriendIcon(String onlineFriend) {
			System.out.println("激活在线好友图标");

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
			System.out.println("好友列表");
			System.out.println("数量" + count);
			for(int i=1;i<count;i++){
				System.out.println(myFriendJLabel[i]);

				//System.out.println("激活退退退");

				if (onlineMap.get(myFriendJLabel[i].getText()) != null){
					System.out.println(friendName[i]);
					System.out.println("激活" + i);
					myFriendJLabel[i].setEnabled(true);
				}
			}
		}

	   public void setEnabledOfflineFriendIcon(String offlineFriend) {
		   //myFriendJLabel[Integer.valueOf(newOnlineFriend)].setEnabled(true);
		   String friendName[] = allFriend.split(" ");
		   int count=friendName.length;
		   System.out.println("好友列表");
		   System.out.println("数量" + count);
		   for(int i=1;i<count;i++){
			   System.out.println(myFriendJLabel[i]);
			   //System.out.println("激活退退退");

			   if (myFriendJLabel[i].getText().equals(offlineFriend)){
				   System.out.println(friendName[i]);
				   System.out.println("激活" + i);
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
		public void actionPerformed(ActionEvent e) {//第三步:实现动作响应代码

			if (e.getSource() == moveFriendButton){
				String moveFriend = JOptionPane.showInputDialog("请输入要移动至陌生人的好友姓名：");
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
				String latterPassword = JOptionPane.showInputDialog("请输入新密码：");
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

			//删除好友 FriendList类
			if (e.getSource() == deleteFriend || e.getSource() == deleteFriend1){
				String FriendName = JOptionPane.showInputDialog("请输入好友的名字：");
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


			//实验十二：添加好友和更新好友列表，步骤3：增加处理添加好友的按钮事件代码，从键盘输入新好友的名字，并且发送到服务器。
			if(e.getSource()==addFriendJButton) {
				String newFriendName=JOptionPane.showInputDialog("请输入新好友的名字：");
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
			   if (e.getClickCount()==2){//鼠标点击次数等于2
			   		JLabel jLabel=(JLabel) e.getSource();
					String receiver=jLabel.getText();
				 	//new FriendChat(userName,receiver);
				    //实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤10：保存FriendChat对象
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
		System.out.println(userName + "准备关闭客户端");

		//向服务器发送关闭客户端消息
		Message mess = new Message();
		mess.setSender(userName);
		mess.setReceiver("Server");
		ObjectOutputStream oos;

		mess.setMessageType(MessageType.OFFLINE_FRIEND_TO_SERVER);
		try {
			oos = new ObjectOutputStream(YychatClientConnection.s.getOutputStream());
			oos.writeObject(mess);//向服务器发送消息
		}catch (IOException e){
			e.printStackTrace();
		}

		mess.setMessageType(MessageType.USER_EXIT_SERVER_THREAD_CLOSE);
		try {
			oos = new ObjectOutputStream(YychatClientConnection.s.getOutputStream());
			oos.writeObject(mess);//向服务器发送消息
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
