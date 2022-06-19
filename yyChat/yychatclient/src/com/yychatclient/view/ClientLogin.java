package com.yychatclient.view;

import javax.swing.*;

import com.yychat.model.Message;
import com.yychat.model.User;
import com.yychatclient.control.YychatClientConnection;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientLogin extends JFrame implements ActionListener{


	//实验七：激活在线好友图标，步骤10：创建hmFriendList用来保存用户名和对应的好友列表对象
	public static HashMap<String,FriendList> hmFriendList=new HashMap<String,FriendList>();
    JLabel jLabel;//标签

    //定义三个按钮元素
    JButton jButton_Login;
    JButton jButton_register;
    JButton jButton_Cancel;
    JPanel jPanel_Action;//面板

    JTabbedPane jTabbedPane;
    JPanel jPanel_YyNumber;
    JPanel jPanel_MyPhone;
    JPanel jPanel_Email;

    //定义9个组件
    JLabel jLabel_YyNumber;
    JLabel jLabel_Password;
    JLabel jLabel3;
    JLabel jLabel4;

    //文本框
    JTextField jTextField;
    //密码框
    JPasswordField jPasswordField;
    //按钮
    JButton jButton_Clear;
    JCheckBox jCheckBox1;
    JCheckBox jCheckBox2;

    public ClientLogin(){//构造方法

        //jLabel = new JLabel("压题图片");
        jLabel = new JLabel(new ImageIcon("yyChat/yychatclient/src/images/head.gif"));
        this.add(jLabel,"North");

        //中部添加选项卡组件
        jTabbedPane = new JTabbedPane();
        jPanel_YyNumber = new JPanel(new GridLayout(3,3));
        jLabel_YyNumber = new JLabel("YY号码",JLabel.CENTER);//JLabel.CENTER居中显示
        jLabel_Password = new JLabel("YY密码",JLabel.CENTER);
        jLabel3 = new JLabel("忘记密码",JLabel.CENTER);
        jLabel3.setForeground(Color.blue);//忘记密码字体变成蓝色
        jLabel4 = new JLabel("申请密码保护");
        jTextField = new JTextField();
        jPasswordField = new JPasswordField();
        jButton_Clear = new JButton("清除密码");
        jButton_Clear.addActionListener(this);
        jCheckBox1 = new JCheckBox("隐身登陆");
        jCheckBox2 = new JCheckBox("记住密码");
        //9个组件添加到YyNumber面板
        jPanel_YyNumber.add(jLabel_YyNumber);
        jPanel_YyNumber.add(jTextField);
        jPanel_YyNumber.add(jButton_Clear);
        jPanel_YyNumber.add(jLabel_Password);
        jPanel_YyNumber.add(jPasswordField);
        jPanel_YyNumber.add(jLabel3);
        jPanel_YyNumber.add(jCheckBox1);
        jPanel_YyNumber.add(jCheckBox2);
        jPanel_YyNumber.add(jLabel4);

        jPanel_MyPhone = new JPanel();
        jPanel_Email = new JPanel();
        jTabbedPane.add(jPanel_YyNumber,"YY聊天室");
//        jTabbedPane.add(jPanel_MyPhone,"手机号码");
//        jTabbedPane.add(jPanel_Email,"电子邮箱");
        this.add(jTabbedPane,"Center");

        jButton_Login = new JButton("登录");
        jButton_Login.addActionListener(this);//注册监听器，只监听本对象的动作
        jButton_register = new JButton("注册");

        //实验十一：在user表中注册新用户，步骤1：在注册按钮上添加监听器
        jButton_register.addActionListener(this);

        jButton_Cancel = new JButton("取消");
        jPanel_Action = new JPanel();
        //三个按钮添加到面板，超过一个以上的组件需要额外定义一个jpanel来组织多个组件的布局
        jPanel_Action.add(jButton_Login);
        jPanel_Action.add(jButton_register);
        jPanel_Action.add(jButton_Cancel);
        //把面板添加到JFrame南部
        this.add(jPanel_Action,"South");

        this.setSize(350,240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点close按钮会退出程序
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ClientLogin clientLogin = new ClientLogin();
    }

    //实验八：激活新上线好友图标，步骤3:新增sendMessag()方法
   public void sendMessage(Socket s,Message mess){
       OutputStream os;
       try {
           os = s.getOutputStream();
           ObjectOutputStream oos=new ObjectOutputStream(os);
           oos.writeObject(mess);
       } catch (IOException e1) {
           e1.printStackTrace();
       }
    }

    @Override
   public void actionPerformed(ActionEvent e) {
         //实验十一：在user表中注册新用户，步骤2：添加响应注册按钮的事件代码
    	 if(e.getSource() == jButton_register){
             String userName = jTextField.getText().trim();
             String password = new String(jPasswordField.getPassword());
             User user=new User();
             user.setUserName(userName);
             user.setPassword(password);

             //实验十一：在user表中注册新用户，步骤5：设置userType
             user.setUserType(User.USER_REGISTER);
             //实验十一：在user表中注册新用户，步骤7：调用registerUser进行注册
             Boolean registerSuccess=new YychatClientConnection().registerUser(user);
             System.out.println("连接数据库成功");
             if(registerSuccess){
    		   JOptionPane.showMessageDialog(this,userName+" 注册成功");
             }
             else {
    		   JOptionPane.showMessageDialog(this,userName+" 有同名用户，注册失败，请重新注册");
             }
    	 }
    	 //清除密码
         if (e.getSource() == jButton_Clear){
             jPasswordField.setText("");
         }
        if(e.getSource()==jButton_Login){
            String userName=jTextField.getText();
            String password=new String(jPasswordField.getPassword());
            User user=new User();
            user.setUserName(userName);
            user.setPassword(password);

          //实验十一：在user表中注册新用户，步骤6：设置userType
            user.setUserType(User.USER_LOGIN_VALIDATE);

            //实验十：利用数据库userrelation表来更新好友列表，步骤4：客户端拿到好友的名字
            Message mess = new YychatClientConnection().loginValidate(user);
            if(mess.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {

                String allFriend =mess.getChatContent();//取出全部好友的名字
            //if(new YychatClientConnection().loginValidate1(user)) {

                String strangerList = mess.getChatContent2();


//                String onlineList = message.getOnLineFriend();
//                System.out.println(onlineList);
                //new FriendList(userName);
                System.out.println();

                //实验十：利用数据库userrelation表来更新好友列表，步骤5：把好友的名字传给好友列表对象
            	FriendList friendList=new FriendList(userName,allFriend,strangerList);

       		    //FriendList friendList=new FriendList(userName);
                //实验七：激活在线好友图标，步骤11：在hmFriendList保存用户名和对应的好友列表对象
                ClientLogin.hmFriendList.put(userName, friendList);

                System.out.println(userName+"的friendList对象保存好了："+friendList);

                //实验七：激活在线好友图标，步骤2：用户登录成功后向服务器发送请求信息，请求从服务端获取自己的在线好友名字
                // Message mess=new Message();
                mess.setSender(userName);
                mess.setReceiver("Server");
                mess.setMessageType(Message.REQUEST_ONLINE_FRIEND);
                sendMessage(YychatClientConnection.s,mess);

           /*  OutputStream os;
		try {
			os = YychatClientConnection.s.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
	           oos.writeObject(mess);
		} catch (IOException e1) {
			e1.printStackTrace();
		}  */


		    //实验八：激活新上线好友图标，实现思路：用户登录成功后向服务器发送消息(希望服务器告诉全部在线好友我上线了)
		    //实验八：激活新上线好友图标，步骤1：用户登录成功后向服务器发送消息
		    mess.setMessageType(Message.NEW_ONLINE_FRIEND_TO_SERVER);
		    sendMessage(YychatClientConnection.s,mess);

            this.dispose();
        }    else {
        	JOptionPane.showMessageDialog(this,"密码错误！请重新登录！");
        }


    }

}
}
