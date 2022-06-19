package com.yychatserver.control;

import com.yychat.model.Message;
import com.yychat.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class YychatServer {
    ////实验六：服务器转发聊天消息到客户端并在好友聊天界面上显示，步骤2：创建HashMap对象，用来保存用户名和对应的Socket对象
	public static HashMap<String,Socket> hmSocket=new HashMap();

	public YychatServer(){
    	ServerSocket ss;
     try {
        ss = new ServerSocket(3456);
        System.out.println("服务器启动，监听3456....");
         while (true) {
             Socket s = ss.accept();//等待客户端的连接，程序阻塞
             System.out.println("建立连接成功" + s);

             InputStream is = s.getInputStream();
             ObjectInputStream ois = new ObjectInputStream(is);
             User user = (User) ois.readObject();
             String password = user.getPassword();
             String userName=user.getUserName();
             System.out.println("用户名：" + user.getUserName());
             System.out.println("密码：" + user.getPassword());
             //实验十一：在user表中注册新用户，步骤10：服务器接收到user类型
             String userType=user.getUserType();

             //登陆验证，客户端怎么才能拿到服务端的验证结果
             //Message消息类来解决这个问题
             Message mess = new Message();

            //实验十一：在user表中注册新用户，步骤11：服务器利用不同的user类型进操作
             if(userType.equals(User.USER_REGISTER)) {
             //实验十一：在user表中注册新用户，步骤12：在user表中查询userName
            	 if(DBUtil.seekUser(userName)) {
            		 //有同名用户，不能注册
            		 mess.setMessageType(Message.USER_REGISTER_FAILURE);
            	 }
            	 else {
            		 //没有同名用户，可以注册
            		 DBUtil.insertIntoUser(userName,password);
            		 mess.setMessageType(Message.USER_REGISTER_SUCCESSS);
            	 }
            	//实验十一：在user表中注册新用户，步骤13：发送mess回客户端
            	 ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                 oos.writeObject(mess);
//                 s.close();//注册后关闭Socket对象
             }

            if(userType.equals(User.USER_LOGIN_VALIDATE)) {
            	//实验九：通过数据库完成用户登录验证，步骤1：加载驱动程序
                //Class.forName("com.mysql.cj.jdbc.Driver");

                 /*//实验九：通过数据库完成用户登录验证，步骤2：建立连接对象
                 //String url="jdbc:mysql://127.0.0.1:3306/yychat2021x";
                 String url="jdbc:mysql://127.0.0.1:3306/yychat2021x?useUnicode=true&characterEncoding=UTF-8";
                 String dbUserName="root";
                 String dbPassword="20020601";
                 Connection conn=DriverManager.getConnection(url,dbUserName,dbPassword);

                 //实验九：通过数据库完成用户登录验证，步骤3:使用SQL查询语句来查询数据user表
                 String user_Login_Sql="select * from user where username=? and password=?";
                 PreparedStatement ptmt=conn.prepareStatement(user_Login_Sql);
                 ptmt.setString(1,userName);
                 ptmt.setString(2,password);
                 ResultSet rs=ptmt.executeQuery();//返回一个结果集

               //实验九：通过数据库完成用户登录验证，步骤4:利用查询的结果来判断是否允许登录
                 boolean loginSuccess=rs.next();//rs中如果有记录，loginSuccess为真，否则为假*/

               //实验九：通过数据库完成用户登录验证，步骤6:调用loginValidate方法实现登录验证
                 boolean loginSuccess=DBUtil.loginValidate(userName,password);

                // if (password.equals("0601")) {
                	 if (loginSuccess) {
                //实验十：利用数据库userrelation表来更新好友列表，步骤1：在服务器端读取用户的好友名字
                	String allFrinend=DBUtil.seekAllFriend(userName);

                	String strangerList = DBUtil.seekStranger(userName);
                	mess.setChatContent2(strangerList);
               //实验十：利用数据库userrelation表来更新好友列表，步骤3：把好友名字发送回客户端
                 mess.setChatContent(allFrinend);//保存全部好友的名字

                 mess.setMessageType(Message.LOGIN_VALIDATE_SUCCESS);




                         //有可能同时会有多个客户之间聊天，我们的服务器需要为多个客户服务（接收并转发消息）
                //实验五：客户端发送聊天信息到服务器，步骤6：为每一个登录成功的用户创建一个服务线程（接收并转发消息）
                 new ServerReceiverThread(s).start();//线程池
                //实验六：服务器转发聊天信息到客户端并在好友聊天界面上显示，步骤3：保存用户名和对应的Socket对象
    			hmSocket.put(userName,s);
                 } else {
                     mess.setMessageType(Message.LOGIN_VALIDATE_FAILURE);
                }

               //实验五：客户端发送聊天信息到服务器，步骤7：注释掉s.close();
                 //s.close();
                 //ss.close();
                }
             ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             oos.writeObject(mess);
            }


        }catch (IOException |ClassNotFoundException e){
            e.printStackTrace();

        }
    }
}
