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
    ////ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������2������HashMap�������������û����Ͷ�Ӧ��Socket����
	public static HashMap<String,Socket> hmSocket=new HashMap();

	public YychatServer(){
    	ServerSocket ss;
     try {
        ss = new ServerSocket(3456);
        System.out.println("����������������3456....");
         while (true) {
             Socket s = ss.accept();//�ȴ��ͻ��˵����ӣ���������
             System.out.println("�������ӳɹ�" + s);

             InputStream is = s.getInputStream();
             ObjectInputStream ois = new ObjectInputStream(is);
             User user = (User) ois.readObject();
             String password = user.getPassword();
             String userName=user.getUserName();
             System.out.println("�û�����" + user.getUserName());
             System.out.println("���룺" + user.getPassword());
             //ʵ��ʮһ����user����ע�����û�������10�����������յ�user����
             String userType=user.getUserType();

             //��½��֤���ͻ�����ô�����õ�����˵���֤���
             //Message��Ϣ��������������
             Message mess = new Message();

            //ʵ��ʮһ����user����ע�����û�������11�����������ò�ͬ��user���ͽ�����
             if(userType.equals(User.USER_REGISTER)) {
             //ʵ��ʮһ����user����ע�����û�������12����user���в�ѯuserName
            	 if(DBUtil.seekUser(userName)) {
            		 //��ͬ���û�������ע��
            		 mess.setMessageType(Message.USER_REGISTER_FAILURE);
            	 }
            	 else {
            		 //û��ͬ���û�������ע��
            		 DBUtil.insertIntoUser(userName,password);
            		 mess.setMessageType(Message.USER_REGISTER_SUCCESSS);
            	 }
            	//ʵ��ʮһ����user����ע�����û�������13������mess�ؿͻ���
            	 ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                 oos.writeObject(mess);
//                 s.close();//ע���ر�Socket����
             }

            if(userType.equals(User.USER_LOGIN_VALIDATE)) {
            	//ʵ��ţ�ͨ�����ݿ�����û���¼��֤������1��������������
                //Class.forName("com.mysql.cj.jdbc.Driver");

                 /*//ʵ��ţ�ͨ�����ݿ�����û���¼��֤������2���������Ӷ���
                 //String url="jdbc:mysql://127.0.0.1:3306/yychat2021x";
                 String url="jdbc:mysql://127.0.0.1:3306/yychat2021x?useUnicode=true&characterEncoding=UTF-8";
                 String dbUserName="root";
                 String dbPassword="20020601";
                 Connection conn=DriverManager.getConnection(url,dbUserName,dbPassword);

                 //ʵ��ţ�ͨ�����ݿ�����û���¼��֤������3:ʹ��SQL��ѯ�������ѯ����user��
                 String user_Login_Sql="select * from user where username=? and password=?";
                 PreparedStatement ptmt=conn.prepareStatement(user_Login_Sql);
                 ptmt.setString(1,userName);
                 ptmt.setString(2,password);
                 ResultSet rs=ptmt.executeQuery();//����һ�������

               //ʵ��ţ�ͨ�����ݿ�����û���¼��֤������4:���ò�ѯ�Ľ�����ж��Ƿ������¼
                 boolean loginSuccess=rs.next();//rs������м�¼��loginSuccessΪ�棬����Ϊ��*/

               //ʵ��ţ�ͨ�����ݿ�����û���¼��֤������6:����loginValidate����ʵ�ֵ�¼��֤
                 boolean loginSuccess=DBUtil.loginValidate(userName,password);

                // if (password.equals("0601")) {
                	 if (loginSuccess) {
                //ʵ��ʮ���������ݿ�userrelation�������º����б�����1���ڷ������˶�ȡ�û��ĺ�������
                	String allFrinend=DBUtil.seekAllFriend(userName);

                	String strangerList = DBUtil.seekStranger(userName);
                	mess.setChatContent2(strangerList);
               //ʵ��ʮ���������ݿ�userrelation�������º����б�����3���Ѻ������ַ��ͻؿͻ���
                 mess.setChatContent(allFrinend);//����ȫ�����ѵ�����

                 mess.setMessageType(Message.LOGIN_VALIDATE_SUCCESS);




                         //�п���ͬʱ���ж���ͻ�֮�����죬���ǵķ�������ҪΪ����ͻ����񣨽��ղ�ת����Ϣ��
                //ʵ���壺�ͻ��˷���������Ϣ��������������6��Ϊÿһ����¼�ɹ����û�����һ�������̣߳����ղ�ת����Ϣ��
                 new ServerReceiverThread(s).start();//�̳߳�
                //ʵ������������ת��������Ϣ���ͻ��˲��ں��������������ʾ������3�������û����Ͷ�Ӧ��Socket����
    			hmSocket.put(userName,s);
                 } else {
                     mess.setMessageType(Message.LOGIN_VALIDATE_FAILURE);
                }

               //ʵ���壺�ͻ��˷���������Ϣ��������������7��ע�͵�s.close();
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
