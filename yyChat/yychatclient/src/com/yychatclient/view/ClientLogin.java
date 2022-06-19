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


	//ʵ���ߣ��������ߺ���ͼ�꣬����10������hmFriendList���������û����Ͷ�Ӧ�ĺ����б����
	public static HashMap<String,FriendList> hmFriendList=new HashMap<String,FriendList>();
    JLabel jLabel;//��ǩ

    //����������ťԪ��
    JButton jButton_Login;
    JButton jButton_register;
    JButton jButton_Cancel;
    JPanel jPanel_Action;//���

    JTabbedPane jTabbedPane;
    JPanel jPanel_YyNumber;
    JPanel jPanel_MyPhone;
    JPanel jPanel_Email;

    //����9�����
    JLabel jLabel_YyNumber;
    JLabel jLabel_Password;
    JLabel jLabel3;
    JLabel jLabel4;

    //�ı���
    JTextField jTextField;
    //�����
    JPasswordField jPasswordField;
    //��ť
    JButton jButton_Clear;
    JCheckBox jCheckBox1;
    JCheckBox jCheckBox2;

    public ClientLogin(){//���췽��

        //jLabel = new JLabel("ѹ��ͼƬ");
        jLabel = new JLabel(new ImageIcon("yyChat/yychatclient/src/images/head.gif"));
        this.add(jLabel,"North");

        //�в����ѡ����
        jTabbedPane = new JTabbedPane();
        jPanel_YyNumber = new JPanel(new GridLayout(3,3));
        jLabel_YyNumber = new JLabel("YY����",JLabel.CENTER);//JLabel.CENTER������ʾ
        jLabel_Password = new JLabel("YY����",JLabel.CENTER);
        jLabel3 = new JLabel("��������",JLabel.CENTER);
        jLabel3.setForeground(Color.blue);//����������������ɫ
        jLabel4 = new JLabel("�������뱣��");
        jTextField = new JTextField();
        jPasswordField = new JPasswordField();
        jButton_Clear = new JButton("�������");
        jButton_Clear.addActionListener(this);
        jCheckBox1 = new JCheckBox("�����½");
        jCheckBox2 = new JCheckBox("��ס����");
        //9�������ӵ�YyNumber���
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
        jTabbedPane.add(jPanel_YyNumber,"YY������");
//        jTabbedPane.add(jPanel_MyPhone,"�ֻ�����");
//        jTabbedPane.add(jPanel_Email,"��������");
        this.add(jTabbedPane,"Center");

        jButton_Login = new JButton("��¼");
        jButton_Login.addActionListener(this);//ע���������ֻ����������Ķ���
        jButton_register = new JButton("ע��");

        //ʵ��ʮһ����user����ע�����û�������1����ע�ᰴť����Ӽ�����
        jButton_register.addActionListener(this);

        jButton_Cancel = new JButton("ȡ��");
        jPanel_Action = new JPanel();
        //������ť��ӵ���壬����һ�����ϵ������Ҫ���ⶨ��һ��jpanel����֯�������Ĳ���
        jPanel_Action.add(jButton_Login);
        jPanel_Action.add(jButton_register);
        jPanel_Action.add(jButton_Cancel);
        //�������ӵ�JFrame�ϲ�
        this.add(jPanel_Action,"South");

        this.setSize(350,240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//��close��ť���˳�����
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ClientLogin clientLogin = new ClientLogin();
    }

    //ʵ��ˣ����������ߺ���ͼ�꣬����3:����sendMessag()����
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
         //ʵ��ʮһ����user����ע�����û�������2�������Ӧע�ᰴť���¼�����
    	 if(e.getSource() == jButton_register){
             String userName = jTextField.getText().trim();
             String password = new String(jPasswordField.getPassword());
             User user=new User();
             user.setUserName(userName);
             user.setPassword(password);

             //ʵ��ʮһ����user����ע�����û�������5������userType
             user.setUserType(User.USER_REGISTER);
             //ʵ��ʮһ����user����ע�����û�������7������registerUser����ע��
             Boolean registerSuccess=new YychatClientConnection().registerUser(user);
             System.out.println("�������ݿ�ɹ�");
             if(registerSuccess){
    		   JOptionPane.showMessageDialog(this,userName+" ע��ɹ�");
             }
             else {
    		   JOptionPane.showMessageDialog(this,userName+" ��ͬ���û���ע��ʧ�ܣ�������ע��");
             }
    	 }
    	 //�������
         if (e.getSource() == jButton_Clear){
             jPasswordField.setText("");
         }
        if(e.getSource()==jButton_Login){
            String userName=jTextField.getText();
            String password=new String(jPasswordField.getPassword());
            User user=new User();
            user.setUserName(userName);
            user.setPassword(password);

          //ʵ��ʮһ����user����ע�����û�������6������userType
            user.setUserType(User.USER_LOGIN_VALIDATE);

            //ʵ��ʮ���������ݿ�userrelation�������º����б�����4���ͻ����õ����ѵ�����
            Message mess = new YychatClientConnection().loginValidate(user);
            if(mess.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {

                String allFriend =mess.getChatContent();//ȡ��ȫ�����ѵ�����
            //if(new YychatClientConnection().loginValidate1(user)) {

                String strangerList = mess.getChatContent2();


//                String onlineList = message.getOnLineFriend();
//                System.out.println(onlineList);
                //new FriendList(userName);
                System.out.println();

                //ʵ��ʮ���������ݿ�userrelation�������º����б�����5���Ѻ��ѵ����ִ��������б����
            	FriendList friendList=new FriendList(userName,allFriend,strangerList);

       		    //FriendList friendList=new FriendList(userName);
                //ʵ���ߣ��������ߺ���ͼ�꣬����11����hmFriendList�����û����Ͷ�Ӧ�ĺ����б����
                ClientLogin.hmFriendList.put(userName, friendList);

                System.out.println(userName+"��friendList���󱣴���ˣ�"+friendList);

                //ʵ���ߣ��������ߺ���ͼ�꣬����2���û���¼�ɹ��������������������Ϣ������ӷ���˻�ȡ�Լ������ߺ�������
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


		    //ʵ��ˣ����������ߺ���ͼ�꣬ʵ��˼·���û���¼�ɹ����������������Ϣ(ϣ������������ȫ�����ߺ�����������)
		    //ʵ��ˣ����������ߺ���ͼ�꣬����1���û���¼�ɹ����������������Ϣ
		    mess.setMessageType(Message.NEW_ONLINE_FRIEND_TO_SERVER);
		    sendMessage(YychatClientConnection.s,mess);

            this.dispose();
        }    else {
        	JOptionPane.showMessageDialog(this,"������������µ�¼��");
        }


    }

}
}
